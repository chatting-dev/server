package com.chatting.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

	public BusinessException(final String message, final ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
}
