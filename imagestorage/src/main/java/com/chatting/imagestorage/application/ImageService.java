package com.chatting.imagestorage.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chatting.imagestorage.domain.ImageFile;
import com.chatting.imagestorage.exception.BusinessException;
import com.chatting.imagestorage.exception.ErrorCode;
import com.chatting.imagestorage.presentation.dto.ImageUploadResponse;

@Service
public class ImageService {

	private final Path storagePath;
	private final String serverPrefix;

	public ImageService(
		@Value("${file.upload-dir}") String storagePath,
		@Value("${file.server-prefix}") String serverPrefix
	) {
		this.storagePath = Paths.get(storagePath);
		this.serverPrefix = serverPrefix;
	}

	public ImageUploadResponse uploadImage(final MultipartFile image) {
		try {
			ImageFile imageFile = ImageFile.from(image);

			String imageFileName = imageFile.getRandomName();
			Path imageStoragePath = storagePath.resolve(imageFileName);
			Files.copy(imageFile.getInputStream(), imageStoragePath, StandardCopyOption.REPLACE_EXISTING);

			return ImageUploadResponse.of(serverPrefix + storagePath);
		} catch (final IOException e) {
			throw new BusinessException("이미지 저장에 실패했습니다.", ErrorCode.F_INVALID);
		}
	}
}