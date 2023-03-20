package com.chatting.imagestorage.domain;

import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import com.chatting.imagestorage.exception.BusinessException;
import com.chatting.imagestorage.exception.ErrorCode;

public enum FileExtension {

	PNG("png") {
		@Override
		public MediaType mediaType() {
			return MediaType.IMAGE_PNG;
		}
	},
	JPG("jpg") {
		@Override
		public MediaType mediaType() {
			return MediaType.IMAGE_JPEG;
		}
	},
	JPEG("jpeg") {
		@Override
		public MediaType mediaType() {
			return MediaType.IMAGE_JPEG;
		}
	},
	GIF("gif") {
		@Override
		public MediaType mediaType() {
			return MediaType.IMAGE_GIF;
		}
	};

	private final String extension;

	FileExtension(String extension) {
		this.extension = extension;
	}

	public static FileExtension from(final String imageFileUrl) {
		final String fileExtension = StringUtils.getFilenameExtension(imageFileUrl);
		return Arrays.stream(FileExtension.values())
			.filter(extension -> extension.extension.equals(fileExtension))
			.findFirst()
			.orElseThrow(
				() -> new BusinessException("이미지 파일 확장자는 png, jpg, jpeg, gif 만 들어올 수 있습니다.", ErrorCode.F_INVALID));
	}

	public abstract MediaType mediaType();
}
