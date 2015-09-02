package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 基本用户信息模型
 * @author xc
 * 2015-7-13
 */
public class BaseUserInfo implements Serializable {

	//用户id
	public int id;
	//用户类型
	public String user_type;
	public String name;
	public String avatar;
	public HashMap<String, String> avatar_more;
	public String email;
	public String uid;
	public String sex;
	public int sex_flag;
	public String description;

    public static BaseUserInfo parseJson(JSONObject jsb) {
        return JSON.parseObject(jsb.toJSONString(), BaseUserInfo.class);
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public HashMap<String, String> getAvatar_more() {
		return avatar_more;
	}

	public void setAvatar_more(HashMap<String, String> avatar_more) {
		this.avatar_more = avatar_more;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getSex_flag() {
		return sex_flag;
	}

	public void setSex_flag(int sex_flag) {
		this.sex_flag = sex_flag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
