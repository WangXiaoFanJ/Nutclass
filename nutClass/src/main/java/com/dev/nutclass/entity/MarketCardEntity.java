package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.utils.TextUtil;

public class MarketCardEntity extends BaseCardEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title;
	private String district;
	private String image;
	private String price;
	private String isPersonal;
	private int status;//物品状态
	private String statusInfo;//物品状态信息
	private String time;
	
	private boolean isMine=false;
	
	public String getId() {
		return id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getIsPersonal() {
		return isPersonal;
	}

	public void setIsPersonal(String isPersonal) {
		this.isPersonal = isPersonal;
	}

	public MarketCardEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_MARKET);
	}

	public MarketCardEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_MARKET);
		optJsonObj(jsonObject);
	}
 
	 
	// 数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		 setId(jsonObj.optString("mid"));
		 setTitle(jsonObj.optString("title"));
		 setDistrict(jsonObj.optString("district"));
		 setImage(jsonObj.optString("image"));
		 setPrice(jsonObj.optString("price"));
		 setIsPersonal(jsonObj.optString("is_personal"));
		 setStatus(jsonObj.optInt("status"));
		 setStatusInfo(jsonObj.optString("status_info"));
		 setTime(jsonObj.optString("add_time"));
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
