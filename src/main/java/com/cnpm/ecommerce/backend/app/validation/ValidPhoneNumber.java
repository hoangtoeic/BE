package com.cnpm.ecommerce.backend.app.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidPhoneNumber {

	String message() default "Invalid phone number";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

}
