package net.gocourse.model;
import com.alibaba.fastjson.JSONObject;

/**
 * 基本json解析模型
 * @author xc
 * 2017-7-13
 */
public class BaseJsonModel {
	//以下为源外层json基本数据格式
	private boolean status;		//当前操作状态，操作成功时才返回true
	private int code;			//状态码，0为正常状态
	private String msg;			//务器在异常状态下返回的消息提
	protected JSONObject data;	//所请求的数据，部分数据请求可能无此数据，返回NULL

    public boolean isStatus() {
        return status;
    }

    public JSONObject getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public BaseJsonModel(String json) {
		try {
			json = new String(json.getBytes("UTF-8"));
			JSONObject jsObject = JSONObject.parseObject(json);
			if(null != jsObject){
				//提取数据，data在对应模型中进一步解析
				status = jsObject.getBoolean("status");
				code = jsObject.getIntValue("code");
				data = jsObject.getJSONObject("data");
				msg = jsObject.getString("msg");
			}
		} catch (Exception e) {
			status = false;
			code = -100;// 未知错误
			msg = "data exception";
		}
	}

}
