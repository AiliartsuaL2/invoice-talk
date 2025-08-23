package com.hocheoltech.invoicetalk.user.mapper

import com.hocheoltech.invoicetalk.user.dto.PostUser
import com.hocheoltech.invoicetalk.user.entity.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface UserMapper {
    @Mapping(
        target = "password",
        expression = """
           java(com.hocheoltech.invoicetalk.global.utils.BCryptUtils.INSTANCE.hashPassword(request.getPassword())) 
        """,
    )
    @Mapping(target = "invoice", expression = "java(new java.util.LinkedHashSet<>())")
    fun toEntity(request: PostUser.Request): User
}
