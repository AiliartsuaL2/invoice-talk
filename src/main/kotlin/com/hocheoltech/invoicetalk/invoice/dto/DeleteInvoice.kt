package com.hocheoltech.invoicetalk.invoice.dto

import org.jetbrains.annotations.NotNull

class DeleteInvoice {
    data class Request(
        @field:NotNull
        val id: Long?,
    )
}
