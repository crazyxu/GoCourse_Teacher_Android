package net.gocourse.net;

/**
 * 返回的status
 * 
 * @author Mouse
 *
 */

public abstract class MsgCode {
	/**
	 * 未知状态
	 */
	public static final int UNKNOEWN_STATUS = -1;
	/**
	 * 未登录
	 */
	public static final int UNLOGIN = -2;

	/**
	 * 成功
	 */
	public static final int SUCCESS = 0;
	/**
	 * 未处理的异常操作
	 */
	public static final int UNDEAL_ERROR = -3;
	/**
	 * 页面未找到
	 */
	public static final int PAGE_NOT_FOUND = 1;
	/**
	 * 服务器错误
	 */
	public static final int SERVER_ERROR = 2;
	/**
	 * HTTP CODE错误等
	 */
	public static final int REQUEST_ERROR = 3;

}
