package com.chatting.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

	private final ErrorCode errorCode;

	public UserNotFoundException(final String message, final ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
}
