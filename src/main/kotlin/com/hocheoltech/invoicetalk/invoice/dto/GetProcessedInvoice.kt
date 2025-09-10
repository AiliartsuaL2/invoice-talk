package com.hocheoltech.invoicetalk.invoice.dto

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import java.time.LocalDateTime

class GetProcessedInvoice {
    data class Response(
        val id: Long,
        val status: ResponseStatus,
        val courierName: String,
        val number: String,
        val productName: String,
        val sendAddress: String,
        val receiveAddress: String,
        val receiverName: String,
        val scannedAt: LocalDateTime?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime?,
    )

    enum class ResponseStatus {
        PENDING,
        SUCCESS,
        CANCELED,
        ERROR;
        companion object {
            @JvmStatic
            fun from(invoice: Invoice): ResponseStatus {
                // 대기중 상태인데, 스캔이 한번이라도 된 경우
                return if (invoice.status == InvoiceStatus.PENDING && invoice.scannedAt != null) {
                    CANCELED
                } else {
                    ResponseStatus.valueOf(invoice.status.name)
                }
            }
        }
    }
}
