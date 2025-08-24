package com.hocheoltech.invoicetalk.invoice.dto

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import java.time.LocalDateTime

class GetInvoice {
    data class Response(
        val id: Long,
        val status: InvoiceStatus,
        val courierName: String,
        val number: String,
        val name: String,
        val sendAddress: String,
        val receiveAddress: String,
        val receiverName: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
    )
}
