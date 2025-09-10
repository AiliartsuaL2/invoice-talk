package com.hocheoltech.invoicetalk.invoice.repository

import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import com.hocheoltech.invoicetalk.invoice.repository.custom.CustomInvoiceRepository
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface InvoiceRepository: JpaRepository<Invoice, Long>, CustomInvoiceRepository {
    fun findByUserIdAndId(userId: Long, invoiceId: Long): Invoice?

    fun countByUserIdAndScannedAtBetween(
        userId: Long,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
    ): Long
}
