package com.hocheoltech.invoicetalk.invoice.repository.custom

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.invoice.dto.GetInvoiceCount
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import org.springframework.data.domain.Pageable

interface CustomInvoiceRepository {
    fun findForScan(
        userId: Long,
        status: InvoiceStatus,
        number: String,
        invoiceId: Long?,
    ): List<Invoice>

    fun findInvoiceCountByStatus(
        userId: Long,
    ): List<GetInvoiceCount.QueryResult>

    fun existsSameNumber(
        invoiceId: Long?,
        userId: Long,
        courierName: String,
        number: String,
    ): Boolean

    fun findByUserIdAndStatusIn(
        userId: Long,
        status: Set<InvoiceStatus>,
        pageable: Pageable,
        isProcessed: Boolean,
    ): List<Invoice>
}
