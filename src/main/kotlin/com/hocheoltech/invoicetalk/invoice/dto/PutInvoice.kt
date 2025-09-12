package com.hocheoltech.invoicetalk.invoice.dto

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import jakarta.validation.constraints.NotBlank

class PutInvoice {
    data class Request(
        val id: Long?,
        @field:NotBlank
        val courierName: String?,
        val number: String?,
        @field:NotBlank
        val productName: String?,
        @field:NotBlank
        val sendAddress: String?,
        @field:NotBlank
        val receiveAddress: String?,
        @field:NotBlank
        val receiverName: String?,
        val status: InvoiceStatus = InvoiceStatus.PENDING,
    )
}
