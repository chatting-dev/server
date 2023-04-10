package com.chatting.imagestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(FileResizeException.class)
	public ResponseEntity<ErrorResponse> handleFileResize(final FileResizeException e) {
		return ResponseEntity
			.internalServerError()
			.body(ErrorResponse.from(e.getMessage(), e.getErrorCode()));
	}

	@ExceptionHandler(ImageFileNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleFileNotFound(final ImageFileNotFoundException e) {
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.from(e.getMessage(), e.getErrorCode()));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(final BusinessException e) {
		return ResponseEntity
			.badRequest()
			.body(ErrorResponse.from(e.getMessage(), e.getErrorCode()));
	}
}
