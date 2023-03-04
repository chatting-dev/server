package com.chatting.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// USER_ACCOUNT
	UA_SIGNUP("회원가입 중 에러 발생");

	private final String description;

	ErrorCode(final String description) {
		this.description = description;
	}
}
