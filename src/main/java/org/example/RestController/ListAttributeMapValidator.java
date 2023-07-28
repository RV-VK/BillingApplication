package org.example.RestController;

import org.example.Service.InvalidTemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class ListAttributeMapValidator {
	private final Logger logger = LoggerFactory.getLogger(ListAttributeMapValidator.class);

	public void validate(HashMap<String, String> listAttributes, Class<?> callerClass) throws InvalidTemplateException {
		String NUMBER_REGEX = "^\\d+$";
		List<String> productAttributes = Arrays.asList("id", "code", "name", "unitcode", "type", "price", "stock");
		List<String> userAttributes = Arrays.asList("id", "usertype", "username", "password", "firstname", "lastname", "phonenumber");
		List<String> purchaseAttributes = Arrays.asList("id", "date", "invoice");
		List<String> saleAttributes = Arrays.asList("id", "date");
		if(! listAttributes.get("Pagelength").matches(NUMBER_REGEX)) {
			logger.warn("Error Invalid PageLength attribute given!, {}. It must be a number", listAttributes.get("Pagelength"));
			throw new InvalidTemplateException("Pagelength Invalid! It must be number");
		}
		if(! listAttributes.get("Pagenumber").matches(NUMBER_REGEX)) {
			logger.warn("Error Invalid PageNumber attribute given!, {}. It must be a number", listAttributes.get("Pagenumber"));
			throw new InvalidTemplateException("Pagenumber Invalid! It mus be a number");
		}
		if(listAttributes.get("Attribute") != null) {
			if(callerClass.getSimpleName().equals("ProductController") && ! productAttributes.contains(listAttributes.get("Attribute"))) {
				logger.warn("Error Given Attribute is not A Searchable Attribute in Product, {} ", listAttributes.get("Attribute"));
				throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in Product");
			} else if(callerClass.getSimpleName().equals("UserController") && ! userAttributes.contains(listAttributes.get("Attribute"))) {
				logger.warn("Error Given Attribute is not A Searchable Attribute in User, {} ", listAttributes.get("Attribute"));
				throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in User");
			} else if(callerClass.getSimpleName().equals("PurchaseController") && ! purchaseAttributes.contains(listAttributes.get("Attribute"))) {
				logger.warn("Error Given Attribute is not A Searchable Attribute in Purchase, {} ", listAttributes.get("Attribute"));
				throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in Purchase");
			} else if(callerClass.getSimpleName().equals("SalesController") && ! saleAttributes.contains(listAttributes.get("Attribute"))) {
				logger.warn("Error Given Attribute is not A Searchable Attribute in Sales, {} ", listAttributes.get("Attribute"));
				throw new InvalidTemplateException("Given Attribute is not A Searchable Attribute in Sales");
			}
		}
	}
}

