package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 取得当前学期开学信息，并返回
 *
 * @author loveyu
 * @date 2015/5/12 12:42
 */
public class DataCurrentWeekInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int year;
	private int term;
	private int week;
	private String begin_date;

	public static DataCurrentWeekInfo parseJson(JSONObject jsb) {
		return JSON.parseObject(jsb.toJSONString(), DataCurrentWeekInfo.class);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}

}
