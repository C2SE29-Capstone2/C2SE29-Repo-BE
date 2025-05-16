package org.example.systemeduai.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeConstraint {
    String message() default "Age must be between 3 and 6 years old based on date of birth";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
