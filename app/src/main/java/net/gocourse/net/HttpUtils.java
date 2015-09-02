package net.gocourse.net;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class HttpUtils {
	private static final String TAG = "HttpUtils";

	public static InputStream getStreamFromURL(String imageURL) {
		InputStream in = null;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			in = connection.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;

	}

	public static InputStream getStreamFromURL2(String imageURL) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet();

		if (TextUtils.isEmpty(imageURL)) {
			Log.i(TAG, "imageUrl访问的地址为空");
			return null;
		}
		get.setURI(URI.create(imageURL));

		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				InputStream inputStream = response.getEntity().getContent();
				return inputStream;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
