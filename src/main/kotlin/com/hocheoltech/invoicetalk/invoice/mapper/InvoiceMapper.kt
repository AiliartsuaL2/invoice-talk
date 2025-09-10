package com.hocheoltech.invoicetalk.invoice.mapper

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.invoice.dto.GetInvoice
import com.hocheoltech.invoicetalk.invoice.dto.PutInvoice
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoiceScan
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import com.hocheoltech.invoicetalk.invoice.excel.InvoiceExcelUpload
import com.hocheoltech.invoicetalk.user.entity.User
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
abstract class InvoiceMapper {
    @Mapping(target = "user", expression = "java(user)")
    @Mapping(target = "status", expression = "java(InvoiceStatus.PENDING)")
    @Mapping(target = "histories", expression = "java(new java.util.LinkedHashSet<>())")
    abstract fun toEntity(
        request: PutInvoice.Request,
        @Context user: User,
    ): Invoice

    @Mapping(target = "status", expression = """java(GetInvoice.ResponseStatus.from(invoice))""")
    abstract fun toResponse(invoice: Invoice): GetInvoice.Response

    @Mapping(target = "status", expression = """java(GetInvoice.ResponseStatus.from(invoice, statusType))""")
    abstract fun toResponse(invoice: Invoice, statusType: GetInvoice.Request.StatusType): GetInvoice.Response

    abstract fun toScanResponse(invoice: Invoice): PostInvoiceScan.Response

    fun toEntity(request: InvoiceExcelUpload, user: User): Invoice {
        return Invoice(
            courierName = request.courierName!!,
            status = InvoiceStatus.PENDING,
            number = request.number!!,
            productName = request.productName!!,
            sendAddress = user.address,
            receiveAddress = request.receiveAddress!!,
            receiverName = request.receiverName!!,
            scannedAt = null,
            user = user,
        )
    }
}
