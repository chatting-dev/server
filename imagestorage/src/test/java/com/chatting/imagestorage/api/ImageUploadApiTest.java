package com.chatting.imagestorage.api;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.chatting.imagestorage.MockImageFactory;
import com.chatting.imagestorage.presentation.dto.ImageUploadResponse;

public class ImageUploadApiTest {

	private final TestRestTemplate testRestTemplate = new TestRestTemplate();

	@DisplayName("이미지 파일이 주어지면 업로드할 때 업로드에 성공한다.")
	@Test
	void givenMockImageFile_whenUploadsImage_thenReturnsResponseEntity() throws IOException {
		// given
		FileSystemResource fileSystemResource = new FileSystemResource(MockImageFactory.createMockImage());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("image", fileSystemResource);

		HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);

		// when
		ResponseEntity<ImageUploadResponse> res = testRestTemplate.postForEntity(
			"http://localhost:8080/api/imageUpload",
			req,
			ImageUploadResponse.class
		);

		// then
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
			softAssertions.assertThat(res.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		});
	}

	@DisplayName("이미지 파일을 다운받을 때 이미지를 반환한다.")
	@Test
	void whenImageDownloadsThenReturnsImage() {

		// when
		ResponseEntity<byte[]> res = testRestTemplate.getForEntity(
			"http://localhost:8080/api/resize/images/1cad34b7-55bc-49c3-a5a8-d384c3d30c26.jpeg?width=500",
			byte[].class
		);

		// then
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
			softAssertions.assertThat(res.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_JPEG);
		});
	}

	@DisplayName("한 번 응답을 받은 후 동일한 요청을 etag 헤더와 같이 보내면 304응답코드가 반환된다.")
	@Test
	void givenResponseWasRetrieved_whenRetrievingAgainWithEtag_thenNotModified() {
		// given
		ResponseEntity<byte[]> res = testRestTemplate.getForEntity(
			"http://localhost:8080/api/resize/images/1cad34b7-55bc-49c3-a5a8-d384c3d30c26.jpeg?width=500",
			byte[].class
		);
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
			softAssertions.assertThat(res.getHeaders().getCacheControl()).isNotNull();
			softAssertions.assertThat(res.getHeaders().getETag()).isNotNull();
		});

		HttpHeaders headers = new HttpHeaders();
		headers.setIfNoneMatch(res.getHeaders().getETag());
		HttpEntity<Object> request = new HttpEntity<>(headers);

		// when
		ResponseEntity<byte[]> secondRes = testRestTemplate.exchange(
			"http://localhost:8080/api/resize/images/1cad34b7-55bc-49c3-a5a8-d384c3d30c26.jpeg?width=500",
			HttpMethod.GET,
			request,
			byte[].class
		);

		// then
		assertThat(secondRes.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);
	}

	@DisplayName("이미지를 다운로드할 때 이미지가 존재하지 않아 NotFound 코드가 반환된다.")
	@Test
	void whenImageDownloadsThenReturnsNotFoundStatusCode() {
		// when
		ResponseEntity<byte[]> res = testRestTemplate.getForEntity(
			"http://localhost:8080/api/resize/images/test.jpeg?width=500",
			byte[].class
		);

		// then
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
