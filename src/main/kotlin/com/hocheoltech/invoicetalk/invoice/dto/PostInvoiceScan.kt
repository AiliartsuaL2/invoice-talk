package com.hocheoltech.invoicetalk.invoice.dto

import com.hocheoltech.invoicetalk.global.anotation.NotNullEnum
import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

class PostInvoiceScan {
    data class Request(
        val id: Long?,
        @field:NotBlank
        val invoiceNumber: String,
        @field:NotNullEnum
        val type: UpdateType,
    ) {
        enum class UpdateType {
            SEND,
            CANCEL,
            ;

            fun toBeforeInvoiceStatus(): InvoiceStatus {
                return if (this == SEND) {
                    InvoiceStatus.PENDING
                } else {
                    InvoiceStatus.SUCCESS
                }
            }

            fun toAfterInvoiceStatus(): InvoiceStatus {
                return if (this == SEND) {
                    InvoiceStatus.SUCCESS
                } else {
                    InvoiceStatus.PENDING
                }
            }
        }
    }

    data class Response(
        val invoiceInfo: List<InvoiceInfo>
    ) {
        data class InvoiceInfo(
            val id: Long,
            val status: InvoiceStatus,
            val courierName: String,
            val number: String,
            val name: String,
            val createdAt: LocalDateTime,
        )
    }
}
