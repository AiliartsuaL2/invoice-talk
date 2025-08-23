package com.hocheoltech.invoicetalk.global.error

import com.hocheoltech.invoicetalk.global.utils.StringUtils.isNotNullOrBlank
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

data class ErrorBody(
    val status: Int,
    val code: String,
    val message: String,
    val errors: List<Any>?,
) {
    companion object {
        fun of(
            code: ErrorCode,
            bindingResult: BindingResult,
        ): ErrorBody {
            val fieldErrors: MutableList<FieldError> = mutableListOf()
            val objectErrors: MutableList<ObjectError> = mutableListOf()

            bindingResult.allErrors.forEach {
                if (it is FieldError) {
                    fieldErrors.add(it)
                } else {
                    objectErrors.add(it)
                }
            }

            val errors = bindingResult.allErrors.mapNotNull {
                if (it is FieldError) {
                    CustomFieldError.of(it)
                } else {
                    CustomObjectError.of(it)
                }
            }

            return ErrorBody(
                code.statusCode,
                code.toString(),
                code.message,
                errors,
            )
        }

        fun of(
            code: ErrorCode,
            field: String?,
            value: Any?,
            reason: String,
        ): ErrorBody {
            return ErrorBody(code.statusCode, code.toString(), code.message, CustomFieldError.of(field, value, reason))
        }

        @JvmStatic
        fun of(
            code: ErrorCode,
            additionalMessage: String? = null,
        ): ErrorBody {
            var message = code.message
            if (additionalMessage.isNotNullOrBlank()) {
                message = "$message\n$additionalMessage"
            }
            return ErrorBody(code.statusCode, code.toString(), message, null)
        }

        @JvmStatic
        fun of(
            code: Int,
            message: String,
        ): ErrorBody {
            return ErrorBody(code, code.toString(), message, null)
        }
    }

    class CustomFieldError(
        val field: String?,
        val value: Any?,
        val reason: String?,
    ) {
        companion object {
            fun of(
                field: String?,
                value: Any?,
                reason: String?,
            ): List<CustomFieldError> {
                return listOf(CustomFieldError(field, value, reason))
            }

            fun of(fieldError: FieldError): CustomFieldError {
                return CustomFieldError(
                    fieldError.field,
                    fieldError.rejectedValue?.toString(),
                    fieldError.defaultMessage,
                )
            }
        }
    }

    class CustomObjectError(
        val reason: String?,
    ) {
        companion object {
            fun of(message: String?): List<CustomObjectError>? {
                return message?.let {
                    listOf(CustomObjectError(message))
                }
            }

            fun of(objectError: ObjectError): CustomObjectError {
                return CustomObjectError(objectError.defaultMessage)
            }
        }
    }
}
