package com.dev.nutclass.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LJ Tag信息
 */
public class PriceEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String schoolHour;
	private String price;
	private String backMoney;
	private String backMoneyStr;
	private boolean selected;
	
	public PriceEntity() {
	}
	public PriceEntity(JSONObject json) {
		optJsonObj(json);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSchoolHour() {
		return schoolHour;
	}
	public void setSchoolHour(String schoolHour) {
		this.schoolHour = schoolHour;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBackMoney() {
		return backMoney;
	}
	public void setBackMoney(String backMoney) {
		this.backMoney = backMoney;
	}

	public String getBackMoneyStr() {
		return backMoneyStr;
	}

	public void setBackMoneyStr(String backMoneyStr) {
		this.backMoneyStr = backMoneyStr;
	}

	public void optJsonObj(JSONObject json){
		if(json==null)
			return;
		setId(json.optString("attr_id"));
		setSchoolHour(json.optString("school_hour"));
		setPrice(json.optString("price"));
		setBackMoney(json.optString("back_money"));
		setBackMoneyStr(json.optString("back_money_str"));
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
