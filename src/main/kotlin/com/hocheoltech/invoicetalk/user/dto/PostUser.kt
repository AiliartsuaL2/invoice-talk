package com.hocheoltech.invoicetalk.user.dto

import jakarta.validation.constraints.NotBlank

class PostUser {
    data class Request(
        @field:NotBlank
        val username: String?,
        @field:NotBlank
        val password: String?,
        @field:NotBlank
        val name: String?,
        @field:NotBlank
        val address: String?,
    )
}
