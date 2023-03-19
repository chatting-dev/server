package com.chatting.imagestorage.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;

import com.chatting.imagestorage.presentation.dto.ImageUploadResponse;

@SpringBootTest
class ImageServiceTest {

	private final ImageService sut;

	public ImageServiceTest(@Autowired ImageService sut) {
		this.sut = sut;
	}

	@DisplayName("이미지 파일이 주어지면 이미지를 업로드할 때 이미지 다운로드 url을 반환받는다.")
	@Test
	void givenImageFile_whenUploadsImage_thenReturnsImageResponse() {
		// given
		MockMultipartFile mockFile = new MockMultipartFile(
			"test-image",
			"test-image.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"content".getBytes(StandardCharsets.UTF_8));

		// when
		ImageUploadResponse response = sut.uploadImage(mockFile);

		// then
		assertAll(
			() -> assertThat(response).isNotNull(),
			() -> assertThat(StringUtils.getFilenameExtension(response.imageDownloadUrl()))
				.isEqualTo("jpg")
		);
	}
}