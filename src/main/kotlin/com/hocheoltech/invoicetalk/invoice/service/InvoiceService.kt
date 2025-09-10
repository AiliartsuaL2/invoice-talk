package com.hocheoltech.invoicetalk.invoice.service

import com.hocheoltech.invoicetalk.global.enums.ExcelUploadType
import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.global.error.ErrorCode
import com.hocheoltech.invoicetalk.invoice.dto.*
import com.hocheoltech.invoicetalk.invoice.excel.Cafe24ExcelUploader
import com.hocheoltech.invoicetalk.invoice.excel.CoupangExcelUploader
import com.hocheoltech.invoicetalk.invoice.excel.NaverExcelUploader
import com.hocheoltech.invoicetalk.invoice.mapper.InvoiceMapper
import com.hocheoltech.invoicetalk.invoice.mapper.InvoiceStatusHistoryMapper
import com.hocheoltech.invoicetalk.invoice.repository.InvoiceRepository
import com.hocheoltech.invoicetalk.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalTime

@Service
@Transactional(readOnly = true)
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val userRepository: UserRepository,
    private val invoiceMapper: InvoiceMapper,
    private val invoiceStatusHistoryMapper: InvoiceStatusHistoryMapper,
    private val coupangExcelUploader: CoupangExcelUploader,
    private val naverExcelUploader: NaverExcelUploader,
    private val cafe24ExcelUploader: Cafe24ExcelUploader,
) {
    /**
     * 송장 단건 등록
     *  - 택배사별로 송장번호는 unique하게 관리
     */
    @Transactional
    fun saveInvoice(
        userId: Long,
        request: PutInvoice.Request
    ) {
        val user = userRepository.getReferenceById(userId)
        checkDuplicateNumberAtSameCourier(userId, request)

        if (request.id == null) {
            val invoice = invoiceMapper.toEntity(request, user)
            val history = invoiceStatusHistoryMapper.createInvoice(invoice)
            invoice.addHistory(history)
            invoiceRepository.save(invoice)
        } else {
            val invoice = invoiceRepository.findByUserIdAndId(userId, request.id)
                ?: throw IllegalArgumentException(ErrorCode.NOT_EXISTS_INVOICE.message)
            invoice.modify(request)
        }
    }

    private fun checkDuplicateNumberAtSameCourier(
        userId: Long,
        request: PutInvoice.Request,
    ) {
        // 수정 케이스의 경우 id에 미해당하는 송장 조건 추가
        if (invoiceRepository.existsSameNumber(
                invoiceId = request.id,
                userId = userId,
                courierName = request.courierName!!,
                number = request.number!!,
            )
        ) {
            throw IllegalArgumentException(ErrorCode.ALREADY_EXISTS_INVOICE_BY_COURIER_NUMBER.message)
        }
    }

    @Transactional
    fun createInvoices(
        userId: Long,
        type: ExcelUploadType,
        excelFile: MultipartFile
    ) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw IllegalArgumentException(ErrorCode.NOT_EXISTS_USER.message)

        val uploadedData = when (type) {
            ExcelUploadType.COUPANG -> coupangExcelUploader.upload(excelFile)
            ExcelUploadType.NAVER -> naverExcelUploader.upload(excelFile)
            ExcelUploadType.CAFE24 -> cafe24ExcelUploader.upload(excelFile)
        }
        val invoices = uploadedData.map {
            invoiceMapper.toEntity(it, user)
        }
        invoiceRepository.saveAll(invoices)
    }

    fun getInvoices(
        userId: Long,
        request: GetInvoice.Request
    ): List<GetInvoice.Response> {
        return invoiceRepository.findWithHistoryByUserIdAndStatusInOrderByCreatedAtDesc(
            userId,
            request.toInvoiceStatus(),
            request.toPageable(),
        ).map { invoiceMapper.toResponse(it, request.status!!) }
    }

    /**
     * 송장 번호로 스캔 진행
     *  - 송장 번호로 등록된 데이터가 1개인 경우 : 상태 변경 처리
     *  - 송장 번호로 등록된 데이터가 2개 이상인 경우 : 선택할 정보들을 고르고 다시 요청하도록 응답해줌
     */
    @Transactional
    fun scanInvoice(
        userId: Long,
        request: PostInvoiceScan.Request
    ): List<PostInvoiceScan.Response> {
        val invoices = invoiceRepository.findForScan(
            userId = userId,
            number = request.invoiceNumber,
            status = request.type.toBeforeInvoiceStatus(),
            invoiceId = request.id,
        )
        return when (invoices.size) {
            0 -> throw IllegalArgumentException(ErrorCode.NOT_EXISTS_INVOICE.message)
            1 -> {
                val invoice = invoices.first()
                val statusHistory =
                    invoiceStatusHistoryMapper.updateInvoice(invoice, request.type.toAfterInvoiceStatus())
                invoice.updateStatus(
                    afterStatus = request.type.toAfterInvoiceStatus(),
                    statusHistory = statusHistory,
                )
                listOf(invoiceMapper.toScanResponse(invoice))
            }

            else -> invoices.map { invoiceMapper.toScanResponse(it) }
        }
    }

    fun getInvoice(
        userId: Long,
        invoiceId: Long
    ): GetInvoice.Response {
        return invoiceRepository.findByUserIdAndId(
            userId,
            invoiceId,
        )?.let {
            invoiceMapper.toResponse(it)
        } ?: throw IllegalArgumentException(ErrorCode.NOT_EXISTS_INVOICE.message)
    }

    fun getInvoiceCount(userId: Long): GetInvoiceCount.Response {
        val queryResults = invoiceRepository.findInvoiceCountByStatus(userId)
        val today = LocalDate.now()
        val todayProcessCount = invoiceRepository.countByUserIdAndScannedAtBetween(
            userId = userId,
            startAt = today.atStartOfDay(),
            endAt = today.atTime(LocalTime.MAX)
        )
        val countByStatus = queryResults.associate { Pair(it.status, it.count) }
        val pendingCount = countByStatus[InvoiceStatus.PENDING] ?: 0
        val successCount = countByStatus[InvoiceStatus.SUCCESS] ?: 0
        val errorCount = countByStatus[InvoiceStatus.ERROR] ?: 0
        return GetInvoiceCount.Response(
            todayProcessCount = todayProcessCount,
            allCount = (pendingCount + successCount + errorCount),
            pendingCount = pendingCount,
            successCount = successCount,
            errorCount = errorCount,
        )
    }

    @Transactional
    fun deleteInvoice(
        userId: Long,
        request: DeleteInvoice.Request,
    ) {
        val invoice = invoiceRepository.findByUserIdAndId(
            userId = userId,
            invoiceId = request.id!!,
        ) ?: throw IllegalArgumentException(ErrorCode.NOT_EXISTS_INVOICE.message)
        invoiceRepository.delete(invoice)
    }
}
