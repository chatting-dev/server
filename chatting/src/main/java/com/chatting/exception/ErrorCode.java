package com.chatting.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// USER_ACCOUNT
	UA_SIGNUP("회원가입 중 에러 발생"),
	UA_LOGIN("로그인 중 에러발생"),

	// JWT
	JWT_INVALID("유효하지 않은 토큰"),
	JWT_EXPIRED("유효기간이 만료된 토큰");

	private final String description;

	ErrorCode(final String description) {
		this.description = description;
	}
}
