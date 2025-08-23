package com.hocheoltech.invoicetalk.global.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null

    @CreatedBy
    @Column(updatable = false)
    var createdBy: String? = null

    @LastModifiedDate
    @Column(insertable = false)
    var updatedAt: LocalDateTime? = null

    @LastModifiedBy
    @Column(insertable = false)
    var updatedBy: String? = null
}
