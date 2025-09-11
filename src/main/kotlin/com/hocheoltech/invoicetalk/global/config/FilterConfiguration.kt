package com.hocheoltech.invoicetalk.global.config

import com.hocheoltech.invoicetalk.global.filter.LoggingFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfiguration {
    @Bean
    fun loggingFilter(): FilterRegistrationBean<LoggingFilter> {
        val registrationBean = FilterRegistrationBean<LoggingFilter>()

        registrationBean.setName("logging-filter")
        registrationBean.filter = LoggingFilter()
        registrationBean.order = 1

        registrationBean.addUrlPatterns(
            "/api/*",
        )

        return registrationBean
    }
}
