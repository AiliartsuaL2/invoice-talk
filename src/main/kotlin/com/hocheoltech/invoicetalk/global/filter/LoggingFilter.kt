package com.hocheoltech.invoicetalk.global.filter

import com.bixolon.asp.globals.filter.logging.CachingRequestWrapper
import com.bixolon.asp.globals.filter.logging.CachingResponseWrapper
import com.hocheoltech.invoicetalk.global.utils.getCurrentUsername
import com.hocheoltech.invoicetalk.global.utils.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.util.StreamUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.io.InputStream
import java.util.*

class LoggingFilter : OncePerRequestFilter() {
    private val log = logger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        MDC.put("traceId", UUID.randomUUID().toString())

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response)
        } else {
            doFilterWrapped(
                CachingRequestWrapper(request),
                CachingResponseWrapper(response),
                filterChain,
            )
        }

        MDC.clear()
    }

    private fun doFilterWrapped(
        request: CachingRequestWrapper,
        response: CachingResponseWrapper,
        filterChain: FilterChain,
    ) {
        val startTime = System.currentTimeMillis()

        try {
            logRequest(request)
            filterChain.doFilter(request, response)
        } finally {
            logResponse(response, startTime)
            response.copyBodyToResponse()
        }
    }

    private fun logRequest(request: CachingRequestWrapper) {
        val userId = request.getHeader("user-id")?.takeIf { it.isNotBlank() && it != "null" } ?: "X"

        log.info(
            "[{} {}][{}][{}][{}]",
            request.method,
            request.requestURI,
            getCurrentUsername(),
            userId,
            request.queryString,
        )

        log.info("Request Content Type - {}", request.contentType)

        log.info(
            "Request Parameter - {}",
            request.parameterMap.toSortedMap().map {
                it.key to it.value.joinToString()
            },
        )

        log.info(
            "Request Payload - {}",
            getContent(request.inputStream)
                ?.lineSequence()
                ?.joinToString("") { it.trim() },
        )
    }

    private fun logResponse(
        response: CachingResponseWrapper,
        startTime: Long,
    ) {
        val status = response.status
        log.info("Response Status - {}", status)
        log.info("Elapsed Time - {}ms", System.currentTimeMillis() - startTime)

        if (status != HttpStatus.OK.value()) {
            log.info("Response Payload - {}", getContent(response.contentInputStream))
        }
    }

    @Throws(IOException::class)
    private fun getContent(inputStream: InputStream): String? {
        val content = StreamUtils.copyToByteArray(inputStream)
        return if (content.isNotEmpty()) {
            String(content)
        } else {
            null
        }
    }
}
