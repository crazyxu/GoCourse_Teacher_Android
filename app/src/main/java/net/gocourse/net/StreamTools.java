package net.gocourse.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	/**
	 * 将输入流转化为byte数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {

		ByteArrayOutputStream baos;
		byte[] b = null;
		if (inputStream != null) {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			b = baos.toByteArray();
			inputStream.close();
			baos.close();
		}

		return b;
	}
}
