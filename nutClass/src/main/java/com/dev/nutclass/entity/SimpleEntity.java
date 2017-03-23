package com.dev.nutclass.entity;

import java.io.Serializable;

import org.json.JSONObject;

import android.text.TextUtils;

import com.dev.nutclass.utils.TextUtil;
/**
* @author LJ
* 简单文本信息项
*/
public class SimpleEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TYPE_CHINA = "1";
	public static final String TYPE_FOREIGN = "2";
	public static final String TYPE_HOT = "3";
	private String id="";
	private String desc="";
	private String type=TYPE_CHINA;
	private String sortLetters;
	 
	public SimpleEntity(){
		//解析基本信息
	}
	public SimpleEntity(JSONObject jsonObject){
		//解析基本信息
		optBaseInfo(jsonObject);
	}
	private void optBaseInfo(JSONObject baseObject){
		this.id=baseObject.optString("id");
		if(TextUtils.isEmpty(this.id)){
			this.id=baseObject.optString("category_id");
		}
		this.desc=baseObject.optString("name");
		
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	
}
