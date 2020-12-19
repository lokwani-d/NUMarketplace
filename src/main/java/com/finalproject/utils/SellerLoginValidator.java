package com.finalproject.utils;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.finalproject.POJO.Seller;
import com.finalproject.POJO.User;

/**
 * 
 * @author Deepak_Lokwani
 * 
 * NUID: 001316769
 * 
 * Project name: Finalproject
 * Package name: com.finalproject.utils
 *
 */
public class SellerLoginValidator implements Validator {

	public SellerLoginValidator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Seller.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object command, Errors errors) {
		// TODO Auto-generated method stub
		Seller seller= (Seller) command;
		seller.setEmail(filter(seller.getEmail()));
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "emptyEmail","Email must be provided!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "emptyPassword","Password must be provided!");
	}
	public String filter(String value) {
		value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
        value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
        value = value.replaceAll("(?i)<script>", "");
        value = value.replaceAll("(?i)</script>", "");
        value=  value.replaceAll("(?i)\\bor\\b","");
        value= value.replaceAll("(?i)\\bdelete\\b","");
        value=  value.replaceAll("(?i)\\band\\b","");
        value=  value.replaceAll("(?i)\\bupdate\\b","");
        value=value.replaceAll("(?i)\\binsert\\b","");
        value=value.replaceAll("(?i)\\bwhere\\b","");
        value=value.replaceAll("(?i)\\bselect\\b","");
        return value.trim();
	}
}
