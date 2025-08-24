package com.hocheoltech.invoicetalk.invoice.repository

import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import com.hocheoltech.invoicetalk.invoice.repository.custom.CustomInvoiceRepository
import org.springframework.data.jpa.repository.JpaRepository

interface InvoiceRepository: JpaRepository<Invoice, Long>, CustomInvoiceRepository {
    fun findByUserId(userId: Long): List<Invoice>
}
