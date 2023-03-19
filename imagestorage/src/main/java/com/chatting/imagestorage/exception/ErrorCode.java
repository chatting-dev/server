package com.chatting.imagestorage.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// USER_ACCOUNT
	F_INVALID("이미지 파일 바이트 변환 에러");

	private final String description;

	ErrorCode(final String description) {
		this.description = description;
	}
}
