package com.chatting.imagestorage.exception;

public record ErrorResponse(
	String errorMessage,
	ErrorCode errorCode
) {

	public static ErrorResponse from(final String errorMessage, final ErrorCode errorCode) {
		return new ErrorResponse(errorMessage, errorCode);
	}
}
