package com.hocheoltech.invoicetalk.user.repository

import com.hocheoltech.invoicetalk.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
