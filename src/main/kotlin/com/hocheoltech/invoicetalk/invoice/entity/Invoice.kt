package com.hocheoltech.invoicetalk.invoice.entity

import com.hocheoltech.invoicetalk.global.entity.BaseEntity
import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.user.entity.User
import jakarta.persistence.*

@Entity
class Invoice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val status: InvoiceStatus,
    val number: String,
    val name: String,
    val sendAddress: String,
    val receiveAddress: String,
    val receiverName: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
): BaseEntity()
