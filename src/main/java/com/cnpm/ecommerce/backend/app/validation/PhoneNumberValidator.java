package com.cnpm.ecommerce.backend.app.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

	private Pattern pattern;
	private Matcher matcher;
	
	private static final String PHONE_NUMBER_PATTERN = "^\\d{10}$";
	
	@Override
	public boolean isValid(String phone, ConstraintValidatorContext context) {
		pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
		if(phone == null) {
			return false;
		}
		matcher = pattern.matcher(phone);
		return matcher.matches();
	}
}
