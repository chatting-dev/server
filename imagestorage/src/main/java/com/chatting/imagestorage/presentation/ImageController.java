package com.chatting.imagestorage.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatting.imagestorage.application.ImageService;
import com.chatting.imagestorage.presentation.dto.ImageUploadResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageController {

	private final ImageService imageService;

	@PostMapping("/api/imageUpload")
	public ResponseEntity<ImageUploadResponse> uploadImage(@RequestPart final MultipartFile image) {
		ImageUploadResponse response = imageService.uploadImage(image);

		return ResponseEntity.ok(response);
	}
}
