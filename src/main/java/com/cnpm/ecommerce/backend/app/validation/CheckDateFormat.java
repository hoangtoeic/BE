package com.cnpm.ecommerce.backend.app.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckDateValidator.class)
@Documented
public @interface CheckDateFormat {

	String message() default "Invalid date, ex:12/03/2000";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String pattern();
}
