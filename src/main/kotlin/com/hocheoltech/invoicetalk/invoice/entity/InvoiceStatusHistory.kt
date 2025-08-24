package com.hocheoltech.invoicetalk.invoice.entity

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import jakarta.persistence.*

@Entity
class InvoiceStatusHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val beforeStatus: InvoiceStatus?,
    @Enumerated(EnumType.STRING)
    val afterStatus: InvoiceStatus,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    val invoice: Invoice,
)
