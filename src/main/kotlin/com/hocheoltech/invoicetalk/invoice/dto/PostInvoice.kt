package com.hocheoltech.invoicetalk.invoice.dto

import jakarta.validation.constraints.NotBlank

class PostInvoice {
    data class Request(
        @field:NotBlank
        val courierName: String?,
        @field:NotBlank
        val number: String?,
        @field:NotBlank
        val name: String?,
        @field:NotBlank
        val sendAddress: String?,
        @field:NotBlank
        val receiveAddress: String?,
        @field:NotBlank
        val receiverName: String?,
    )
}
