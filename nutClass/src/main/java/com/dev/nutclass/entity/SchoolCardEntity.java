 package com.dev.nutclass.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.google.gson.JsonObject;

public class SchoolCardEntity extends BaseCardEntity {
/*
 * "id": 1,
"name": "淘宝贝(淘宝贝北京珠江帝景中心)",
"distance": 0,
"thumb": "http://182.92.7.222/images/201410/thumb_img/93_thumb_G_1413688677710.jpg",
"adders": "朝阳区西大望路甲23号B区218号楼2单元101室",
"max_gwk": "2000.00"
 * */
	private static final long serialVersionUID = 1L;
	private String distance;
	private String name;
	private String id;
	private String priceReturn;
	private String icon;
	private String address;
	private String tag;
	private String scope;
	private int flag1;
	private int flag2;
	private int flag3;
	private int flag4;

	

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public int getFlag1() {
		return flag1;
	}

	public void setFlag1(int flag1) {
		this.flag1 = flag1;
	}

	public int getFlag2() {
		return flag2;
	}

	public void setFlag2(int flag2) {
		this.flag2 = flag2;
	}

	public int getFlag3() {
		return flag3;
	}

	public void setFlag3(int flag3) {
		this.flag3 = flag3;
	}

	public int getFlag4() {
		return flag4;
	}

	public void setFlag4(int flag4) {
		this.flag4 = flag4;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public SchoolCardEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_COURSE);
	}

	//3,209
	public SchoolCardEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_SCHOOL);
		optJsonObj(jsonObject);
	}
	public SchoolCardEntity(int type,JSONObject jsonObject) {
		setCardType(type);
		optJsonObj(jsonObject);
	}

	/*
	 * "id": 1,
	"name": "淘宝贝(淘宝贝北京珠江帝景中心)",
	"distance": 0,
	"thumb": "http://182.92.7.222/images/201410/thumb_img/93_thumb_G_1413688677710.jpg",
	"adders": "朝阳区西大望路甲23号B区218号楼2单元101室",
	"max_gwk": "2000.00"
	 * */

	// 课程列表页Course数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setDistance(jsonObj.optString("distance"));
		setName(jsonObj.optString("name"));
		setId(jsonObj.optString("id"));
		setPriceReturn(jsonObj.optString("max_gwk"));
		setIcon(jsonObj.optString("thumb"));
		setAddress(jsonObj.optString("adders"));
		setTag(jsonObj.optString("key_words"));
		setScope(jsonObj.optString("region"));
		setFlag1(jsonObj.optInt("tag1",0));
		setFlag2(jsonObj.optInt("tag2",0));
		setFlag3(jsonObj.optInt("tag3",0));
		setFlag4(jsonObj.optInt("tag4",0));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
