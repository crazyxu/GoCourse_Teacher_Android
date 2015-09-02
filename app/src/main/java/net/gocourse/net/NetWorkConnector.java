package net.gocourse.net;

import net.gocourse.util.Constant;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 网络请求处理器, 具体的请求操作
 * 单例模式
 * @author Mouse
 *
 */
public class NetWorkConnector {
	//单例
	private static NetWorkConnector netWorkConnector = new NetWorkConnector();
    //返回结果
	private static String response;
	//私有构造
    private NetWorkConnector() {
    }
	//产生实例
    public static NetWorkConnector getNetWorkConnector() {
        return netWorkConnector;
    }

    public String sendRequest(int type, String url,
                              Map<String, String> map, Header header) {
        try{
			switch (type){
				case Constant.HTTP_METHOD_GET:
					return doHTTPGet(type,url,map,header);
				case Constant.HTTP_METHOD_POST:
					return doHTTPPost(type,url,map,header);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }

	private String doHTTPGet(int type, String url,
							 Map<String, String> map, Header header) throws Exception{
		// 拼装url
		StringBuffer buffer = new StringBuffer(url).append("?");
		if (map != null && map.size() > 0) {
			for (Entry<String, String> entry : map.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(entry.getValue()).append("&");
			}
		}
		//去掉最后一个字符
		buffer.deleteCharAt(buffer.length() - 1);
		String requestUrl = buffer.toString().trim();
		URL urlPath = new URL(requestUrl);
		//开始请求
		HttpURLConnection conn = (HttpURLConnection) urlPath
				.openConnection();
		conn.setRequestMethod(Constant.getHttpTypeStr(type));
		//超时
		conn.setConnectTimeout(Constant.CONNECT_TIMEOUT);
		conn.connect();
		//连接成功
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return new String(StreamTools.readInputStream(conn
					.getInputStream()));
		} else {
			return null;
		}
	}
	private String doHTTPPost(int type, String url,
							  Map<String, String> map, Header header) throws Exception{
		HttpPost httpPost = new HttpPost(url);
		// 设置请求参数
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				Constant.CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams,
				Constant.READ_TIMEOUT);

		// 拼装params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : map.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}

		// httpclient 进行请求
		httpPost.setEntity(new UrlEncodedFormEntity(params,
				HttpConstantValue.UTF_8));
		//请求头
		httpPost.addHeader(header);

		HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpPost);
		//连接成功
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity);
		} else {
			return null;
		}
	}

}
