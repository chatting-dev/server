package com.chatting.imagestorage.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;

import com.chatting.imagestorage.exception.ImageFileNotFoundException;
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

	@DisplayName("이미지 다운로드 요청시 ")
	@Nested
	class ImageDownloadTest {

		@DisplayName("올바른 이미지 다운로드 url이 주어지면 이미지를 다운로드할 때 다운로드에 성공한다.")
		@Test
		void givenImageDownloadUrl_whenDownloadsImage_thenReturnsImageBytes() {
			// given
			String imageDownloadUrl = "1cad34b7-55bc-49c3-a5a8-d384c3d30c26.jpeg";

			// when
			byte[] downloadImage = sut.downloadImage(imageDownloadUrl, 600);

			// then
			assertThat(downloadImage).isNotNull();
		}

		@DisplayName("존재하지 않는 url이 주어지면 이미지를 다운로드할 때 예외를 던진다.")
		@Test
		void givenNotExistsImageDownloadUrl_whenDownloadsImage_thenThrowsException() {
			// given
			String imageDownloadUrl = "something.jpeg";

			// when & then
			assertThatThrownBy(() -> sut.downloadImage(imageDownloadUrl, 600))
				.isInstanceOf(ImageFileNotFoundException.class)
				.hasMessage("해당 파일이 경로에 존재하지 않습니다.");
		}
	}
}