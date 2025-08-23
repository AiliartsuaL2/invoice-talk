package com.hocheoltech.invoicetalk.user.dto

import jakarta.validation.constraints.NotBlank

class PostLogin {
    data class Request(
        @field:NotBlank
        val username: String?,
        @field:NotBlank
        val password: String?,
    )
}
