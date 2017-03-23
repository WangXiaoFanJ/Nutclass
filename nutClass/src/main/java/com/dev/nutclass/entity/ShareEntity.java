package com.dev.nutclass.entity;

import java.io.Serializable;

import org.json.JSONObject;
/**
* @author LJ
* 分享url
*/
public class ShareEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final int TYPE_COURSE_INFO=1;//课程详情页分享
	public static final int TYPE_ACTION_INFO=2;//活动详情页分享
	
	private int type;
	private String img;//图片url
	private String desc;
	private String url;//targetUrl
	private String title;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	 
}
