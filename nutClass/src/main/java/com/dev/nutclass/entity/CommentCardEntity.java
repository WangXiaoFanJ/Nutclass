package com.dev.nutclass.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LJ Tag信息
 */
public class CommentCardEntity extends BaseCardEntity {


	private static final long serialVersionUID = 1L;
	private String id = "";
	private String time;
	private String desc;
	private String name;
	private String avater;
	
	public CommentCardEntity(){
		
	}
	public CommentCardEntity(JSONObject jsonObj){
		setCardType(BaseCardEntity.CARD_TYPE_COMMENT);
		optJsonObj(jsonObj);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
 
 
	 
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
//	"userImageUrl":"http://www.nutclass.com/images/default.png",
//	"userNick":"坚果用户",
//	"userComment":"购物卡已收到，价格挺合理的。",
//	"releaseTime":"2015-06-22"
	public void optJsonObj(JSONObject json){
		if(json==null)
			return;
		setDesc(json.optString("userComment"));
		setId(json.optString("id"));
		setName(json.optString("userNick"));
		setAvater(json.optString("userImageUrl"));
		setTime(json.optString("releaseTime"));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvater() {
		return avater;
	}
	public void setAvater(String avater) {
		this.avater = avater;
	}
	public void optJsonObjFromFeed(JSONObject json){
		if(json==null)
			return;
		setDesc(json.optString("content"));
		setId(json.optString("uid"));
		setName(json.optString("nick_name"));
		 
	}
}
