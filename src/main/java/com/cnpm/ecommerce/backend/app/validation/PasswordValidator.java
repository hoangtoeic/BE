package com.cnpm.ecommerce.backend.app.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

	private Pattern pattern;
	private Matcher matcher;
	
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		pattern = Pattern.compile(PASSWORD_PATTERN);
		if(password == null) {
			return true;
		}
		matcher = pattern.matcher(password);
		return matcher.matches();
	}

}
