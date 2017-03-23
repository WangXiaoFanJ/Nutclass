package com.dev.nutclass.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LJ Tag信息
 */
public class TagEntity implements Serializable {
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof TagEntity) {
			if (parent1.equals(((TagEntity) o).getParent1())
					&& parent2.equals(((TagEntity) o).getParent2())
					&& desc.equals(((TagEntity) o).getDesc())) {
				return true;
			}

		}
		return false;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private boolean isSelf = false;
	private static final long serialVersionUID = 1L;
	private String id = "";
	private float x;
	private float y;
	private String desc;
	private int level = 0;// 标示自己是哪一类标签，也就是第几层层标签
	private String parent1 = "";
	private String parent1Desc = "";
	private String parent2 = "";
	private String parent2Desc = "";
	private String ownerId = "";// 用来做省市判断的
	
	public TagEntity(){
		
	}
	public TagEntity(JSONObject jsonObj){
		optJsonObj(jsonObj);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getParent1() {
		return parent1;
	}

	public void setParent1(String parent1) {
		this.parent1 = parent1;
	}

	public String getParent1Desc() {
		return parent1Desc;
	}

	public void setParent1Desc(String parent1Desc) {
		this.parent1Desc = parent1Desc;
	}

	public String getParent2() {
		return parent2;
	}

	public void setParent2(String parent2) {
		this.parent2 = parent2;
	}

	public String getParent2Desc() {
		return parent2Desc;
	}

	public void setParent2Desc(String parent2Desc) {
		this.parent2Desc = parent2Desc;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public JSONObject obj2Json() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("category_id", getParent2());
			jsonObj.put("tag", getDesc());
			jsonObj.put("x", getX());
			jsonObj.put("y", getY());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			jsonObj = null;
			e.printStackTrace();
		}
		return jsonObj;
	}
	public void optJsonObj(JSONObject json){
		if(json==null)
			return;
		setX(json.optInt("x"));
		setY(json.optInt("y"));
		setDesc(json.optString("tag"));
		setId(json.optString("tag_id"));
		setParent2(json.optString("category_id"));
	}

}
