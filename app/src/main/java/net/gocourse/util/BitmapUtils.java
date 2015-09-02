package net.gocourse.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapUtils {

	/**
	 * 通过一个网络图片的URL地址得到该图片的输入流
	 * 
	 * @param imageUrl
	 *            图片地址
	 * @return
	 */
	public static InputStream getInputStreamByUrl(String imageUrl) {

		InputStream inStream = null;
		URL url;
		try {
			url = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			inStream = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inStream;
	}

	public static Bitmap getBitmapByNativePath(String path) {
		return BitmapFactory.decodeFile(path);
	}

	public static Bitmap getBitmapByUrl(String url) {
		InputStream inStream = getInputStreamByUrl(url);
		return BitmapFactory.decodeStream(inStream);
	}

}
