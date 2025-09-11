package com.bixolon.asp.globals.filter.logging

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.util.ContentCachingResponseWrapper

class CachingResponseWrapper(response: HttpServletResponse) : ContentCachingResponseWrapper(response)
