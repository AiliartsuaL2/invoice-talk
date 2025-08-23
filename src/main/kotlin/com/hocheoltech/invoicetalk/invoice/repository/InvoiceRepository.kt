package com.hocheoltech.invoicetalk.invoice.repository

import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import org.springframework.data.jpa.repository.JpaRepository

interface InvoiceRepository: JpaRepository<Invoice, Long> {
}
