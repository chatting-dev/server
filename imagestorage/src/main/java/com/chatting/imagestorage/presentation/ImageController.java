package com.chatting.imagestorage.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatting.imagestorage.application.ImageService;
import com.chatting.imagestorage.domain.FileExtension;
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

	@GetMapping("/api/images/{imageUrl}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable final String imageUrl) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(FileExtension.from(imageUrl).mediaType())
			.body(imageService.downloadImage(imageUrl));
	}
}
