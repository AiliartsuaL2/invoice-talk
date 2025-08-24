package com.hocheoltech.invoicetalk.page

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping



@Controller
class PageController {
    @GetMapping("/page/v1/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/page/v1/dashboard")
    fun dashboard(): String {
        return "dashboard"
    }
}
