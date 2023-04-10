package com.chatting.imagestorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MockImageFactory {

	public static File createMockImage() throws IOException {
		File mockImage = File.createTempFile("image", ".jpg");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(mockImage))) {
			bw.write("test");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mockImage;
	}
}
