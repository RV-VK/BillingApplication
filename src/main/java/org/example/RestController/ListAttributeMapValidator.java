package org.example.RestController;

import org.example.Service.InvalidTemplateException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class ListAttributeMapValidator {
	public void validate(HashMap<String, String> listAttributes, Class<?> callerClass) throws InvalidTemplateException {
		String NUMBER_REGEX = "^\\d+$";
		List<String> productAttributes = Arrays.asList("id", "code", "name", "unitcode", "type", "price", "stock");
		List<String> userAttributes = Arrays.asList("id", "usertype", "username", "password", "firstname", "lastname", "phonenumber");
		List<String> purchaseAttributes = Arrays.asList("id", "date", "invoice");
		List<String> saleAttributes = Arrays.asList("id", "date");
		if(! listAttributes.get("Pagelength").matches(NUMBER_REGEX)) {
			System.out.println(listAttributes.get("Pagelength"));
			throw new InvalidTemplateException("Pagelength Invalid! It must be number");
		}
		if(! listAttributes.get("Pagenumber").matches(NUMBER_REGEX))
			throw new InvalidTemplateException("Pagenumber Invalid! It mus be a number");
		if(listAttributes.get("Attribute") != null) {
			if(callerClass.getSimpleName().equals("ProductController") && ! productAttributes.contains(listAttributes.get("Attribute")))
				throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in Product");
			else if(callerClass.getSimpleName().equals("UserController") && ! userAttributes.contains(listAttributes.get("Attribute")))
				throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in User");
			else if(callerClass.getSimpleName().equals("PurchaseController") && ! purchaseAttributes.contains(listAttributes.get("Attribute")))
				throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in Purchase");
			else if(callerClass.getSimpleName().equals("SalesController") && ! saleAttributes.contains(listAttributes.get("Attribute")))
				throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in Sales");
		}
	}
}

