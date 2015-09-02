package net.gocourse.net;

/**
 * 一些网络请求的连接参数常量
 * 
 * @author Gang Zh
 *
 */
public abstract class HttpConstantValue {
	/**
	 * get请求标识符
	 */
	public static final int TYPE_GET = 0;

	/**
	 * post请求标识符
	 */
	public static final int TYPE_POST = 1;

	/**
	 * connect timeout 超时时间
	 */
	public static final int CONNECT_TIMEOUT = 5000;

	/**
	 * 储存cookie的sharedpreference
	 */
	public static final String sp_cookie = "cookie_container";

	/**
	 * 读取的超时时间
	 */
	public static final int READ_TIMEOUT = 20000;

	/**
	 * 字符编码
	 */
	public static final String UTF_8 = "UTF-8";

}
