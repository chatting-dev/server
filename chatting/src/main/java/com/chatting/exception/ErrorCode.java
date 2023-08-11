package com.chatting.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// USER_ACCOUNT
	UA_SIGNUP("회원가입 중 에러 발생"),
	UA_LOGIN("로그인 중 에러발생"),

	// JWT
	JWT_INVALID("유효하지 않은 토큰"),
	JWT_EXPIRED("유효기간이 만료된 토큰"),

	// CHAT_ROOM
	CR_SAME_TOPIC("같은 이름의 토픽이 존재"),
	CR_NONE_TOPIC("존재하지 않은 토픽"),
	CR_NONE_CHATROOM("존재하지 않은 채팅방");
	private final String description;

	ErrorCode(final String description) {
		this.description = description;
	}
}
