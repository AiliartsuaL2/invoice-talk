package com.hocheoltech.invoicetalk.user.entity

import com.hocheoltech.invoicetalk.global.entity.BaseEntity
import com.hocheoltech.invoicetalk.global.utils.BCryptUtils
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val username: String,
    val password: String,
    val name: String,
    val address: String,
    @OneToMany(mappedBy = "user")
    val invoice: MutableSet<Invoice> = mutableSetOf(),
): BaseEntity() {
    fun checkPassword(password: String): Boolean {
        return BCryptUtils.verifyPassword(password, this.password)
    }
}
