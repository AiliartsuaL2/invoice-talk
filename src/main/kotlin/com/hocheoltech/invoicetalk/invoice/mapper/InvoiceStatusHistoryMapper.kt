package com.hocheoltech.invoicetalk.invoice.mapper

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import com.hocheoltech.invoicetalk.invoice.entity.InvoiceStatusHistory
import org.mapstruct.Mapper

@Mapper
abstract class InvoiceStatusHistoryMapper {
    fun createInvoice(invoice: Invoice): InvoiceStatusHistory {
        return InvoiceStatusHistory(
            beforeStatus = null,
            afterStatus = InvoiceStatus.PENDING,
            invoice = invoice,
        )
    }

    fun updateInvoice(
        invoice: Invoice,
        status: InvoiceStatus,
    ): InvoiceStatusHistory {
        return InvoiceStatusHistory(
            beforeStatus = invoice.status,
            afterStatus = status,
            invoice = invoice,
        )
    }
}
