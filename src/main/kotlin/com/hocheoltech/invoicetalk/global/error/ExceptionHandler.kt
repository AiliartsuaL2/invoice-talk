package com.hocheoltech.invoicetalk.global.error

import com.hocheoltech.invoicetalk.global.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class ExceptionHandler {
    val log = logger()

    /**
     * @RequestParams - 값이 없을 때 발생
     */
    @ExceptionHandler(
        MissingServletRequestParameterException::class,
    )
    protected fun handleMissingServletRequestParameterException(
        e: MissingServletRequestParameterException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleMissingServletRequestParameterException", e)
        val response: ErrorBody = ErrorBody.of(
            ErrorCode.INVALID_INPUT_VALUE,
            e.parameterName,
            null,
            e.message,
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * @RequestParam - 타입 불일치일 때 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatchException(
        e: MethodArgumentTypeMismatchException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleMethodArgumentTypeMismatchException", e)
        val response: ErrorBody = ErrorBody.of(
            ErrorCode.INVALID_TYPE_VALUE,
            e.propertyName,
            e.value,
            e.message,
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    /**
     *  @RequestBody, @RequestPart - @Valid 통과 못할 때 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleMethodArgumentNotValidException", e)
        val errorBody: ErrorBody = ErrorBody.of(
            ErrorCode.INVALID_INPUT_VALUE,
            e.bindingResult,
        )
        return ResponseEntity<ErrorBody>(errorBody, HttpStatus.BAD_REQUEST)
    }

    /**
     *  @RequestBody, @RequestPart - @Valid 통과 못할 때 발생 2
     */
    @ExceptionHandler(HandlerMethodValidationException::class)
    protected fun handleHandlerMethodValidationException(
        e: HandlerMethodValidationException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleHandlerMethodValidationException", e)
        val errorBody: ErrorBody = ErrorBody.of(ErrorCode.INVALID_INPUT_VALUE)
        return ResponseEntity<ErrorBody>(errorBody, HttpStatus.BAD_REQUEST)
    }

    /**
     *  @RequestBody, @RequestPart - 타입 불일치일 때 발생
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadableException(
        e: HttpMessageNotReadableException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleHttpMessageNotReadableException", e)
        val response: ErrorBody = ErrorBody.of(
            ErrorCode.INVALID_INPUT_VALUE,
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(
        e: HttpRequestMethodNotSupportedException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleHttpRequestMethodNotSupportedException", e)
        val response: ErrorBody = ErrorBody.of(
            ErrorCode.METHOD_NOT_ALLOWED,
        )
        return ResponseEntity(response, HttpStatus.METHOD_NOT_ALLOWED)
    }

    /**
     * @CookieValue 쿠키 없을 때
     */
    @ExceptionHandler(MissingRequestCookieException::class)
    protected fun handleMissingRequestCookieException(
        e: MissingRequestCookieException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleMissingRequestCookieException", e)
        val response: ErrorBody = ErrorBody.of(
            ErrorCode.UNAUTHORIZED,
        )
        return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
    }

    /**
     * 리소스가 없을 때
     */
    @ExceptionHandler(NoResourceFoundException::class)
    protected fun handleNoResourceFoundException(
        e: NoResourceFoundException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleNoResourceFoundException", e)
        val response: ErrorBody = ErrorBody.of(
            ErrorCode.NOT_FOUND_RESOURCE,
        )
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    /**
     * 입력값 잘못되었을 때
     */
    @ExceptionHandler(IllegalArgumentException::class)
    protected fun handleIllegalArgumentException(
        e: IllegalArgumentException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleIllegalArgumentException", e)
        val message = e.message ?: ErrorCode.INVALID_INPUT_VALUE.message
        val response: ErrorBody = ErrorBody.of(
            400, message
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    /**
     * 상태 잘못되었을 때
     */
    @ExceptionHandler(IllegalStateException::class)
    protected fun handleIllegalStateException(
        e: IllegalStateException,
    ): ResponseEntity<ErrorBody> {
        log.info("handleIllegalStateException", e)
        val message = e.message ?: ErrorCode.INVALID_INPUT_VALUE.message
        val response: ErrorBody = ErrorBody.of(
            400, message
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}
