package org.example.RestController;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDetails {
	private HttpStatus httpStatus;
	private Date timeStamp;
	private String message;
	private String details;

	public ErrorDetails(HttpStatus httpStatus, Date timeStamp, String message, String details) {
		super();
		this.httpStatus = httpStatus;
		this.timeStamp = timeStamp;
		this.message = message;
		this.details = details;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}
