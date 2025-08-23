package com.hocheoltech.invoicetalk.invoice.entity

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import jakarta.persistence.*

@Entity
class InvoiceStatusHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val invoiceId: Long,
    @Enumerated(EnumType.STRING)
    val beforeStatus: InvoiceStatus?,
    @Enumerated(EnumType.STRING)
    val afterStatus: InvoiceStatus,
)
