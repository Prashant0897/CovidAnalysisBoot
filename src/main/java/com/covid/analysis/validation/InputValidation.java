package com.covid.analysis.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.covid.analysis.exception.InvalidValidationParameterException;

@Component
public class InputValidation {

	public boolean validationDriver(String value, String validType) throws InvalidValidationParameterException {
		
		boolean result = false;
		switch (validType) {
			case "State":
				result = stateValidator(value);
				break;
			default:
				throw new InvalidValidationParameterException("Invalid Validation Parameters..");
		}
		return result;
	}
	
	public static boolean stateValidator(String state) {
		
		String alphaRegex = "^[a-zA-Z]*$";
		Pattern pattern = Pattern.compile(alphaRegex);
		
		Matcher matcher = pattern.matcher(state);
		return matcher.matches();
	}
	
}
