package com.hocheoltech.invoicetalk.invoice.repository

import com.hocheoltech.invoicetalk.invoice.entity.InvoiceStatusHistory
import org.springframework.data.jpa.repository.JpaRepository

interface InvoiceStatusHistoryRepository: JpaRepository<InvoiceStatusHistory, Long> {
}
