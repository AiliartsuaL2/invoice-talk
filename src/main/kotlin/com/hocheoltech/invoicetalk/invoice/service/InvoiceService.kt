package com.hocheoltech.invoicetalk.invoice.service

import com.hocheoltech.invoicetalk.global.error.ErrorCode
import com.hocheoltech.invoicetalk.invoice.dto.GetInvoice
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoiceScan
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoice
import com.hocheoltech.invoicetalk.invoice.mapper.InvoiceMapper
import com.hocheoltech.invoicetalk.invoice.mapper.InvoiceStatusHistoryMapper
import com.hocheoltech.invoicetalk.invoice.repository.InvoiceRepository
import com.hocheoltech.invoicetalk.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val userRepository: UserRepository,
    private val invoiceMapper: InvoiceMapper,
    private val invoiceStatusHistoryMapper: InvoiceStatusHistoryMapper,
) {
    /**
     * 송장 단건 등록
     *  - 택배사별로 송장번호는 unique하게 관리
     */
    @Transactional
    fun createInvoice(
        userId: Long,
        request: PostInvoice.Request
    ) {
        val user = userRepository.getReferenceById(userId)
        val invoice = invoiceMapper.toEntity(request, user)
        val history = invoiceStatusHistoryMapper.createInvoice(invoice)
        invoice.addHistory(history)
        invoiceRepository.save(invoice)
    }

    @Transactional
    fun createInvoices(
        userId: Long,
        request: List<PostInvoice.Request>
    ) {
        // TODO 엑셀 업로드로 구현
    }

    fun getInvoice(userId: Long): List<GetInvoice.Response> {
        return invoiceRepository.findByUserId(userId)
            .map { invoiceMapper.toResponse(it) }
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
    ): PostInvoiceScan.Response {
        val invoices = invoiceRepository.findForScan(
            userId = userId,
            status = request.type.toBeforeInvoiceStatus(),
            number = request.invoiceNumber,
            invoiceId = request.id,
        )
        val response = when (invoices.size) {
            0 -> throw IllegalArgumentException(ErrorCode.NOT_EXISTS_INVOICE.message)
            1 -> {
                val invoice = invoices.first()
                val statusHistory = invoiceStatusHistoryMapper.updateInvoice(invoice, request.type.toAfterInvoiceStatus())
                invoice.updateStatus(
                    afterStatus = request.type.toAfterInvoiceStatus(),
                    statusHistory = statusHistory,
                )
                emptyList()
            }
            else -> invoices.map { invoiceMapper.toScanResponse(it) }
        }
        return PostInvoiceScan.Response(response)
    }
}
