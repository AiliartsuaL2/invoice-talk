package com.bixolon.asp.globals.filter.logging

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class CachingRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val encoding: Charset
    private val rawData: ByteArray
    private val parameterMap = mutableMapOf<String, Array<String>>()

    init {
        var characterEncoding = request.characterEncoding
        if (characterEncoding.isBlank()) {
            characterEncoding = StandardCharsets.UTF_8.name()
        }

        this.encoding = Charset.forName(characterEncoding)

        parameterMap.putAll(super.getParameterMap())

        request.inputStream.use {
            this.rawData = it.readBytes()
        }
    }

    override fun getInputStream(): ServletInputStream {
        return CachedServletInputStream(this.rawData)
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(this.inputStream, this.encoding))
    }

    override fun getParameterMap(): MutableMap<String, Array<String>> {
        return this.parameterMap
    }

    private class CachedServletInputStream(contents: ByteArray?) : ServletInputStream() {
        private val buffer: ByteArrayInputStream = ByteArrayInputStream(contents)

        @Throws(IOException::class)
        override fun read(): Int {
            return buffer.read()
        }

        override fun isFinished(): Boolean {
            return buffer.available() == 0
        }

        override fun isReady(): Boolean {
            return true
        }

        override fun setReadListener(listener: ReadListener) {
            throw UnsupportedOperationException()
        }
    }
}
