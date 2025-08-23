package com.hocheoltech.invoicetalk.user.controller

import com.hocheoltech.invoicetalk.user.dto.PostLogin
import com.hocheoltech.invoicetalk.user.dto.PostUser
import com.hocheoltech.invoicetalk.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {
    @PostMapping("/api/v1/users")
    fun createUser(
        @Valid @RequestBody
        request: PostUser.Request,
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.create(request))
    }

    @PostMapping("/api/v1/users/login")
    fun createUser(
        @Valid @RequestBody
        request: PostLogin.Request,
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.login(request))
    }
}
