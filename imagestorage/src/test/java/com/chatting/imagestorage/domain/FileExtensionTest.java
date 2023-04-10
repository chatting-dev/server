package com.chatting.imagestorage.domain;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.chatting.imagestorage.ImageConvertor;

class FileExtensionTest {

	private byte[] image;

	@BeforeEach
	void setUp() throws IOException {
		File imageFile = Paths.get("src/test/resources/static/")
			.resolve("1cad34b7-55bc-49c3-a5a8-d384c3d30c26.jpeg")
			.toFile();
		image = Files.readAllBytes(imageFile.toPath());
	}

	@DisplayName("이미지와 리사이징할 크기가 주어지고 이미지를 리사이징할 때 리사이징된 길이를 가진 이미지가 반환된다.")
	@Test
	void givenImageAndResizedWidth_whenResizeImage_thenReturnsImageHasResizedWidth() {
		// given
		int width = 500;

		// when
		byte[] resized = FileExtension.JPEG.resizeImage(image, width);
		int resizedWidth = ImageConvertor.toBufferedImage(resized)
			.getWidth();

		// then
		assertThat(resizedWidth).isEqualTo(500);
	}
}