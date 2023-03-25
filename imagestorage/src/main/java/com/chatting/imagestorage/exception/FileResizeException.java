package com.chatting.imagestorage.exception;

import lombok.Getter;

@Getter
public class FileResizeException extends BusinessException {

	public FileResizeException(final String message, final ErrorCode errorCode) {
		super(message, errorCode);
	}
}
