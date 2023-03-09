package com.chatting.exception;

public class TokenExpirationException extends BusinessException {

	public TokenExpirationException(final String message, final ErrorCode errorCode) {
		super(message, errorCode);
	}
}
