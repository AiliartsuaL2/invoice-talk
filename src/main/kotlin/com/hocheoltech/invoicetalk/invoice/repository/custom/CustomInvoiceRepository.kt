package com.hocheoltech.invoicetalk.invoice.repository.custom

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.invoice.entity.Invoice

interface CustomInvoiceRepository {
    fun findForScan(
        userId: Long,
        status: InvoiceStatus,
        number: String,
        invoiceId: Long?,
    ): List<Invoice>
}
