package com.cnpm.ecommerce.backend.app.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidUsername {

	String message() default "Username must be longer than 3 and not use special chars";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
