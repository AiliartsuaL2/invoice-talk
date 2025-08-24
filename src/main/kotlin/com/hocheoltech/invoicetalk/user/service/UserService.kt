package com.hocheoltech.invoicetalk.user.service

import com.hocheoltech.invoicetalk.global.error.ErrorCode
import com.hocheoltech.invoicetalk.user.dto.PostLogin
import com.hocheoltech.invoicetalk.user.dto.PostUser
import com.hocheoltech.invoicetalk.user.mapper.UserMapper
import com.hocheoltech.invoicetalk.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {
    @Transactional
    fun create(request: PostUser.Request) {
        // TODO 임시, Security 연동 후 수정 필요
        if (userRepository.findByUsername(request.username!!) != null) {
            throw IllegalArgumentException(ErrorCode.DUPLICATE_USERNAME.message)
        }
        val user = userMapper.toEntity(request)
        userRepository.save(user)
    }

    fun login(request: PostLogin.Request): PostLogin.Response {
        // TODO 임시, Security 연동 후 수정 필요
        val user = userRepository.findByUsername(request.username!!)
            ?: throw IllegalArgumentException(ErrorCode.NOT_EXISTS_USER.message)
        if (!user.checkPassword(request.password!!)) {
            throw IllegalArgumentException(ErrorCode.NOT_EXISTS_USER.message)
        }
        return PostLogin.Response(user.id!!)
    }
}
