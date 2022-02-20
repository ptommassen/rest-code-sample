package software.uniqore.storedemo.rest.validators

import software.uniqore.storedemo.rest.models.UpdateInventoryLineModel
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UpdateInventoryLineModelValidator::class])
annotation class IdOrNameNeeded(
    val message: String = "Either id or name needs to be filled",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class UpdateInventoryLineModelValidator : ConstraintValidator<IdOrNameNeeded, UpdateInventoryLineModel> {
    override fun isValid(value: UpdateInventoryLineModel, context: ConstraintValidatorContext?): Boolean {
        return value.id != null || value.name != null
    }
}