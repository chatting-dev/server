package com.chatting.imagestorage.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	F_INVALID("유효하지 않은 파일"),
	F_NOT_FOUND("경로상 이미지 탐색 에러");

	private final String description;

	ErrorCode(final String description) {
		this.description = description;
	}
}
