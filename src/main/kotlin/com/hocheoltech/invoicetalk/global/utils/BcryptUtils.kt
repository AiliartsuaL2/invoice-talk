package com.hocheoltech.invoicetalk.global.utils

import org.mindrot.jbcrypt.BCrypt

object BCryptUtils {
    fun hashPassword(plainPassword: String): String {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt())
    }

    fun verifyPassword(
        plainPassword: String,
        hashedPassword: String,
    ): Boolean {
        return BCrypt.checkpw(plainPassword, hashedPassword)
    }
}
