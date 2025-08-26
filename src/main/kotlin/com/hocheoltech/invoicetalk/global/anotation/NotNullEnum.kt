package com.hocheoltech.invoicetalk.global.anotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [EnumValidator::class])
annotation class NotNullEnum(
    val message: String = "Cannot be null",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class EnumValidator : ConstraintValidator<NotNullEnum, Any> {
    override fun isValid(
        value: Any?,
        context: ConstraintValidatorContext,
    ): Boolean {
        return value != null
    }
}
