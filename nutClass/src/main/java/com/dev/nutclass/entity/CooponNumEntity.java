package com.dev.nutclass.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.google.gson.JsonObject;

public class CooponNumEntity extends BaseCardEntity {

	// 1 已使用 2 未使用 3 已过期
	public final static int TYPE_ALL = 0;//
	public final static int TYPE_USED = 1;//
	public final static int TYPE_NOT_USE = 2;//
	public final static int TYPE_EXPIRED = 3;//
	private static final long serialVersionUID = 1L;
	private String allNum = "";
	private String usedNum = "";
	private String notUseNum = "";
	private String expiredNum = "";
	 
	
	public String getAllNum() {
		return allNum;
	}



	public void setAllNum(String allNum) {
		this.allNum = allNum;
	}



	public String getUsedNum() {
		return usedNum;
	}



	public void setUsedNum(String usedNum) {
		this.usedNum = usedNum;
	}



	public String getNotUseNum() {
		return notUseNum;
	}



	public void setNotUseNum(String notUseNum) {
		this.notUseNum = notUseNum;
	}



	public String getExpiredNum() {
		return expiredNum;
	}



	public void setExpiredNum(String expiredNum) {
		this.expiredNum = expiredNum;
	}



	public CooponNumEntity(JSONObject jsonObj){
		setCardType(CARD_TYPE_COOPON_NUM);
		optJsonObj(jsonObj);
	}

	

	/*
	 * my_bonus_count: 5,
	use_bonus: 0,
	not_use_bonus: 5,
	time_old_bonus: 0
	 */
	// 课程列表页Course数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setAllNum(jsonObj.optString("my_bonus_count"));
		setUsedNum(jsonObj.optString("use_bonus"));
		setNotUseNum(jsonObj.optString("not_use_bonus"));
		setExpiredNum(jsonObj.optString("time_old_bonus"));
	}

}
