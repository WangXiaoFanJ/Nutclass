package com.dev.nutclass.entity;

import java.io.Serializable;

import org.json.JSONObject;
/**
* @author LJ
* Tag信息
*/
public class TagParentEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id="";
	private String desc="";
	private String parentId="";
	private boolean isSelected=false;
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
	public TagParentEntity(JSONObject jsonObject){
		//解析基本信息
		optBaseInfo(jsonObject);
	}
	private void optBaseInfo(JSONObject baseObject){
		this.desc=baseObject.optString("name");
		this.id=baseObject.optString("category_id");
		this.setParentId(baseObject.optString("parent_id"));
		
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
