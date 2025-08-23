package com.hocheoltech.invoicetalk.global.utils

// TODO 추후 security 연동 후 설정
fun getCurrentUsername(): String? {
//    val authentication: Authentication? = SecurityContextHolder.getContext().authentication
//
//    authentication ?: return null
//
//    var username: String? = null
//    if (authentication.principal is UserDetails) {
//        username = (authentication.principal as UserDetails).username
//    } else if (authentication.principal is String) {
//        username = authentication.principal.toString()
//    }
//
//    if (username == "anonymousUser") return null

    return "server"
}
