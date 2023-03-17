package com.chatting.imagestorage.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(final BusinessException e) {
		return ResponseEntity
			.badRequest()
			.body(ErrorResponse.from(e.getMessage(), e.getErrorCode()));
	}
}
