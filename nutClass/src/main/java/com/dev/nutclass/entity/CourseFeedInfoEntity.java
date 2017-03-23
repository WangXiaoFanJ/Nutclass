package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseFeedInfoEntity extends BaseCardEntity {
	
	private static final long serialVersionUID = 1L;
	private String distance;
	private String courseName;
	private String id;
	private int level;
	private String priceMax;
	private String priceReturn;
	private String icon;
	private String address;
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(String priceMax) {
		this.priceMax = priceMax;
	}

	public String getPriceReturn() {
		return priceReturn;
	}

	public void setPriceReturn(String priceReturn) {
		this.priceReturn = priceReturn;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CourseFeedInfoEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_COURSE);
	}

	public CourseFeedInfoEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_COURSE);
		optJsonObj(jsonObject);
	}
	// 课程列表页Course数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		 setDistance(jsonObj.optString("distance"));
		 setLevel(jsonObj.optInt("lv"));
		 setCourseName(jsonObj.optString("goods_name"));
		 setId(jsonObj.optString("id"));
		 setPriceMax(jsonObj.optString("shop_price"));
		 setPriceReturn(jsonObj.optString("max_gwk"));
		 setIcon(jsonObj.optString("thumb"));
		 setAddress(jsonObj.optString("adders"));

	}
	// 品牌馆表页Course数据解析
	public void optJsonObjBrand(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
         setId(jsonObj.optString("product_id"));
         setLevel(jsonObj.optInt("levelInfo"));
         setIcon(jsonObj.optString("headerImageUrl"));
         setCourseName(jsonObj.optString("courseName"));
		 setPriceMax(jsonObj.optString("price"));
		 setPriceReturn(jsonObj.optString("returnFee"));
		 setAddress(jsonObj.optString("adders"));
		 setDistance(jsonObj.optString("distance"));

	}
}
