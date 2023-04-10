package com.chatting.imagestorage.exception;

import lombok.Getter;

@Getter
public class ImageFileNotFoundException extends RuntimeException {

	private final ErrorCode errorCode;

	public ImageFileNotFoundException(final String message, final ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
}
