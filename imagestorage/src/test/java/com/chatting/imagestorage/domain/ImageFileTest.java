package com.chatting.imagestorage.domain;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.chatting.imagestorage.exception.BusinessException;

class ImageFileTest {

	@DisplayName("올바른 MultipartFile이 주어지면 ImageFile 생성에 성공한다.")
	@MethodSource("provideImageFile")
	@ParameterizedTest
	void givenMultipartFile_whenCreatesImageFile_thenReturnsImageFile(String originalFilename, String contentType) {
		// given
		MockMultipartFile mockFile = new MockMultipartFile(
			"test-image",
			originalFilename,
			contentType,
			"content".getBytes(StandardCharsets.UTF_8));

		// when & then
		assertThatCode(() -> ImageFile.from(mockFile))
			.doesNotThrowAnyException();
	}

	private static Stream<Arguments> provideImageFile() {
		return Stream.of(
			Arguments.of("image.png", MediaType.IMAGE_PNG_VALUE),
			Arguments.of("image.jpg", MediaType.IMAGE_JPEG_VALUE),
			Arguments.of("image.jpeg", MediaType.IMAGE_JPEG_VALUE)
		);
	}

	@DisplayName("null 파일이 주어지면 ImageFile 생성할 때 예외를 던진다.")
	@Test
	void givenNullFile_whenCreatesImageFile_thenThrowsException() {
		// given
		MockMultipartFile mockFile = null;

		// when & then
		assertThatThrownBy(() -> ImageFile.from(mockFile))
			.isInstanceOf(BusinessException.class)
			.hasMessage("이미지 파일은 null 값이 들어올 수 없습니다.");
	}

	@DisplayName("빈 파일이 주어지면 ImageFile을 생성할 때 예외를 던진다.")
	@Test
	void givenEmptyFile_whenCreatesImageFile_thenThrowsException() {
		// given
		MockMultipartFile mockFile = new MockMultipartFile(
			"test-image",
			"test-image.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"".getBytes(StandardCharsets.UTF_8)
		);

		// when & then
		assertThatThrownBy(() -> ImageFile.from(mockFile))
			.isInstanceOf(BusinessException.class)
			.hasMessage("빈 이미지 파일은 들어올 수 없습니다.");
	}

	@DisplayName("빈 문자열의 이름을 가지고 있는 이미지 파일이 주어지면 ImageFile을 생성할 때 예외를 던진다.")
	@Test
	void givenEmptyFilename_whenCreatesImageFile_thenThrowsException() {
		// given
		MockMultipartFile mockFile = new MockMultipartFile(
			"test-image",
			"",
			MediaType.IMAGE_JPEG_VALUE,
			"content".getBytes(StandardCharsets.UTF_8)
		);

		// when & then
		assertThatThrownBy(() -> ImageFile.from(mockFile))
			.isInstanceOf(BusinessException.class)
			.hasMessage("이미지 파일 이름은 빈 문자열이 들어올 수 없습니다.");
	}

	@DisplayName("지원하지 않는 확장자를 가진 파일이 주어지면 ImageFile을 생성할 때 예외를 던진다.")
	@Test
	void givenInvalidFileExtension_whenCreatesImageFile_thenThrowsException() {
		// given
		MockMultipartFile mockFile = new MockMultipartFile(
			"test-file",
			"invalid-file.pdf",
			MediaType.APPLICATION_PDF_VALUE,
			"content".getBytes(StandardCharsets.UTF_8)
		);

		// when & then
		assertThatThrownBy(() -> ImageFile.from(mockFile))
			.isInstanceOf(BusinessException.class)
			.hasMessage("이미지 파일의 확장자는 png, jpg, jpeg, gif만 가능합니다.");
	}
}