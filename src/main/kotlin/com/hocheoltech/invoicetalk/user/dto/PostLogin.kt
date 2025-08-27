package com.hocheoltech.invoicetalk.user.dto

import jakarta.validation.constraints.NotBlank

class PostLogin {
    data class Request(
        @field:NotBlank
        val username: String?,
        @field:NotBlank
        val password: String?,
    )
     // TODO 임시 (토큰 생성 전까지)
     data class Response(
         val userId: Long,
         val name: String,
     )
}
