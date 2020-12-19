package com.finalproject.utils;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.finalproject.POJO.GroceryProduct;
import com.finalproject.POJO.TechProduct;

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
public class GroceryProductValidator implements Validator {

	public GroceryProductValidator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return GroceryProduct.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		GroceryProduct product = (GroceryProduct) target;
		product.setName(filter(product.getName()));
		product.setDescription(filter(product.getDescription()));
		product.setUnit(filter(product.getUnit()));
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "emptyName","Name must be provided!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "emptyEmail","Description must be provided!");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "unit", "emptyConfPassword","Unit must be provided!");
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
