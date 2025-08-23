package com.hocheoltech.invoicetalk.global.utils

object StringUtils {
    fun String?.isNotNullOrBlank(): Boolean = !this.isNullOrBlank()
}
