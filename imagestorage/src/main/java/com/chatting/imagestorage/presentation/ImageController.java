package com.chatting.imagestorage.presentation;

import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	private static final String DEFAULT_WIDTH = "600";
	private static final int CACHE_CONTROL_MAX_AGE = 30;

	private final ImageService imageService;

	@PostMapping("/api/imageUpload")
	public ResponseEntity<ImageUploadResponse> uploadImage(@RequestPart final MultipartFile image) {
		ImageUploadResponse response = imageService.uploadImage(image);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/resize/images/{imageUrl}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable final String imageUrl,
		@RequestParam(required = false, defaultValue = DEFAULT_WIDTH) final int width) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(FileExtension.from(imageUrl).mediaType())
			.cacheControl(CacheControl.maxAge(CACHE_CONTROL_MAX_AGE, TimeUnit.DAYS))
			.body(imageService.downloadImage(imageUrl, width));
	}
}
