package com.chatting.exception;

public class InvalidTokenException extends BusinessException {

	public InvalidTokenException(final String message, final ErrorCode errorCode) {
		super(message, errorCode);
	}
}
