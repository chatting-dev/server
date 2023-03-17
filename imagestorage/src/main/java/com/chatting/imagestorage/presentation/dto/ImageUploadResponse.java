package com.chatting.imagestorage.presentation.dto;

public record ImageUploadResponse(String imageDownloadUrl) {

	public static ImageUploadResponse of(final String imageDownloadUrl) {
		return new ImageUploadResponse(imageDownloadUrl);
	}
}
