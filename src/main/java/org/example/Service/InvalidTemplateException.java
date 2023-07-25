package org.example.Service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception thrown when user enters inputs that do not match the template.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTemplateException extends Exception {
	public InvalidTemplateException(String message) {
		super(message);
	}
}
