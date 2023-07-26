package org.example.RestController;

import org.example.DAO.ApplicationErrorException;
import org.example.DAO.PageCountOutOfBoundsException;
import org.example.DAO.UniqueConstraintException;
import org.example.DAO.UnitCodeViolationException;
import org.example.Service.InvalidTemplateException;
import org.example.Service.UnDividableEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
	private ErrorDetails errorDetails;

	@ExceptionHandler(InvalidTemplateException.class)
	public final ResponseEntity<ErrorDetails> handleInvalidTemplateException(InvalidTemplateException invalidTemplateException, WebRequest request) {
		logger.error(invalidTemplateException.getMessage());
		errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, new Date(), invalidTemplateException.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PageCountOutOfBoundsException.class)
	public final ResponseEntity<ErrorDetails> handlePageCountOutOfBoundsException(PageCountOutOfBoundsException exception, WebRequest request) {
		logger.error(exception.getMessage());
		errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ApplicationErrorException.class)
	public final ResponseEntity<ErrorDetails> handleApplicationErrorException(ApplicationErrorException exception, WebRequest request) {
		logger.error(exception.getMessage());
		errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UniqueConstraintException.class)
	public final ResponseEntity<ErrorDetails> handleUniqueConstraintException(UniqueConstraintException exception, WebRequest request) {
		logger.error(exception.getMessage());
		errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnitCodeViolationException.class)
	public final ResponseEntity<ErrorDetails> handleUnitCodeViolationException(UnitCodeViolationException exception, WebRequest webRequest) {
		logger.error(exception.getMessage());
		errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, new Date(), exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnDividableEntityException.class)
	public final ResponseEntity<ErrorDetails> handleUnDividableEntityException(UnDividableEntityException exception, WebRequest request) {
		logger.error(exception.getMessage());
		errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}