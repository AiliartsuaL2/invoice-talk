package com.hocheoltech.invoicetalk.invoice.entity

import com.hocheoltech.invoicetalk.global.entity.BaseEntity
import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.global.error.ErrorCode
import com.hocheoltech.invoicetalk.user.entity.User
import jakarta.persistence.*

@Entity
class Invoice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    // 택배사명
    var courierName: String,
    // 송장상태 (대기, 완료, 에러)
    @Enumerated(EnumType.STRING)
    var status: InvoiceStatus,
    // 송장번호
    var number: String,
    // 상품명
    var name: String,
    // 발송지
    var sendAddress: String,
    // 목적지
    var receiveAddress: String,
    // 받는 사람
    var receiverName: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToMany(mappedBy = "invoice", orphanRemoval = true, cascade = [CascadeType.PERSIST])
    val histories: MutableSet<InvoiceStatusHistory> = mutableSetOf(),
): BaseEntity() {
    fun addHistory(history: InvoiceStatusHistory) {
        this.histories.add(history)
    }

    fun updateStatus(
        afterStatus: InvoiceStatus,
        statusHistory: InvoiceStatusHistory,
    ) {
        if (this.status == afterStatus) {
            throw IllegalStateException(ErrorCode.ALREADY_UPDATED_STATUS.message)
        }
        this.status = afterStatus
        this.histories.add(statusHistory)
    }
}
