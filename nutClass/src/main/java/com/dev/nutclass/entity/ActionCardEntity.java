package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionCardEntity extends BaseCardEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String icon;
	private String activityTitle;
	private String activityTag;
	private String address;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public String getActivityTag() {
		return activityTag;
	}

	public void setActivityTag(String activityTag) {
		this.activityTag = activityTag;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ActionCardEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_ACTION);
	}

	public ActionCardEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_ACTION);
		optJsonObj(jsonObject);
	}
	/**
	 *"activityId":"8",
	"activityImageUrl":"http://www.nutclass.com/ad_imgs/1438871232767308818.jpg",
	"activityTitle":"海淀/万柳全英文写生活动",
	"address":"北京市海淀区蓝靛厂北路---巴沟山水园内",
	"activityTag":"进行中"
	 */
	 
	// 运营位数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		 setActivityTitle(jsonObj.optString("activityTitle"));
		 setActivityTag(jsonObj.optString("activityTag"));
		 setId(jsonObj.optString("activityId"));
		 setIcon(jsonObj.optString("activityImageUrl"));
		 setAddress(jsonObj.optString("address"));

	}
}
