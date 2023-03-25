package com.chatting.imagestorage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.chatting.imagestorage.exception.BusinessException;
import com.chatting.imagestorage.exception.ErrorCode;

public class ImageConvertor {

	public static BufferedImage toBufferedImage(final byte[] image) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
			return ImageIO.read(inputStream);
		} catch (final IOException e) {
			throw new BusinessException("BufferedImage 로의 변환 중 실패했습니다.", ErrorCode.F_INVALID);
		}
	}
}
