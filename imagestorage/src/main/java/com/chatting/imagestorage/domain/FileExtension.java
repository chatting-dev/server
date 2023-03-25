package com.chatting.imagestorage.domain;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import com.chatting.imagestorage.exception.BusinessException;
import com.chatting.imagestorage.exception.ErrorCode;
import com.chatting.imagestorage.exception.FileResizeException;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.nio.AnimatedGifReader;
import com.sksamuel.scrimage.nio.GifSequenceWriter;
import com.sksamuel.scrimage.nio.ImageSource;
import com.sksamuel.scrimage.nio.JpegWriter;
import com.sksamuel.scrimage.nio.PngWriter;

public enum FileExtension {

	PNG("png") {
		@Override
		public MediaType mediaType() {
			return MediaType.IMAGE_PNG;
		}

		@Override
		public byte[] resizeImage(final byte[] originImage, final int width) {
			try {
				return ImmutableImage.loader()
					.fromBytes(originImage)
					.scaleToWidth(width)
					.bytes(PngWriter.NoCompression);
			} catch (final IOException e) {
				throw new FileResizeException("PNG 파일 사이즈 변환 중 문제가 발생했습니다.", ErrorCode.F_RESIZE);
			}
		}
	},
	JPG("jpg") {
		@Override
		public MediaType mediaType() {
			return MediaType.IMAGE_JPEG;
		}

		@Override
		public byte[] resizeImage(final byte[] originImage, final int width) {
			try {
				return ImmutableImage.loader()
					.fromBytes(originImage)
					.scaleToWidth(width)
					.bytes(JpegWriter.Default);
			} catch (final IOException e) {
				throw new FileResizeException("JPG 파일 사이즈 변환 중 문제가 발생했습니다.", ErrorCode.F_RESIZE);
			}
		}
	},
	JPEG("jpeg") {
		@Override
		public MediaType mediaType() {
			return MediaType.IMAGE_JPEG;
		}

		@Override
		public byte[] resizeImage(final byte[] originImage, final int width) {
			try {
				return ImmutableImage.loader()
					.fromBytes(originImage)
					.scaleToWidth(width)
					.bytes(JpegWriter.Default);
			} catch (final IOException e) {
				throw new FileResizeException("JPEG 파일 사이즈 변환 중 문제가 발생했습니다.", ErrorCode.F_RESIZE);
			}
		}
	},
	GIF("gif") {
		@Override
		public MediaType mediaType() {
			return MediaType.IMAGE_GIF;
		}

		@Override
		public byte[] resizeImage(final byte[] originImage, final int width) {
			try {
				ImmutableImage[] immutableImages = AnimatedGifReader.read(ImageSource.of(originImage))
					.getFrames()
					.stream()
					.map(immutableImage -> immutableImage.scaleToWidth(width))
					.toArray(ImmutableImage[]::new);
				return new GifSequenceWriter(0, Boolean.TRUE).bytes(immutableImages);
			} catch (final IOException e) {
				throw new FileResizeException("GIF 파일 사이즈 변환 중 문제가 발생했습니다.", ErrorCode.F_RESIZE);
			}
		}
	};

	private final String extension;

	FileExtension(String extension) {
		this.extension = extension;
	}

	public static FileExtension from(final String imageFileUrl) {
		final String fileExtension = StringUtils.getFilenameExtension(imageFileUrl);
		return Arrays.stream(FileExtension.values())
			.filter(extension -> extension.extension.equals(fileExtension))
			.findFirst()
			.orElseThrow(
				() -> new BusinessException("이미지 파일 확장자는 png, jpg, jpeg, gif 만 들어올 수 있습니다.", ErrorCode.F_INVALID));
	}

	public abstract MediaType mediaType();

	public abstract byte[] resizeImage(final byte[] originImage, final int width);
}
