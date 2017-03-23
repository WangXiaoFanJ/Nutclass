package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AdCardEntity extends BaseCardEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ImageEntity> list = null;
	private String headUrl="";
	private String headName="";
	private String headScheme="";

	public AdCardEntity(int cardType) {
		setCardType(cardType);
	}

	public AdCardEntity(int cardType, JSONArray jsonArray) {
		setCardType(cardType);
		optJsonObj(jsonArray);
	}
	public AdCardEntity(int cardType, JSONObject jsonObj) {
		setCardType(cardType);
		optJsonObjAD2(jsonObj);
	}

	public List<ImageEntity> getList() {
		return list;
	}

	public void setList(List<ImageEntity> list) {
		this.list = list;
	}

	// 运营位数据解析
	public void optJsonObj(JSONArray jsonArray) {
		if (jsonArray == null)
			return;
		if (list == null) {
			list = new ArrayList<ImageEntity>();
		}
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				ImageEntity entity = new ImageEntity();
				entity.optJsonObjNew(jsonArray.getJSONObject(i));
				list.add(entity);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	// 运营位数据解析
		public void optJsonObjAD2(JSONObject jsonObj) {
			if (jsonObj == null)
				return;
			this.headUrl=jsonObj.optString("head_url");
			this.headScheme=jsonObj.optString("head_scheme");
			this.headName=jsonObj.optString("name");
			optJsonObj(jsonObj.optJSONArray("pic_list_array"));

		}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getHeadScheme() {
		return headScheme;
	}

	public void setHeadScheme(String headScheme) {
		this.headScheme = headScheme;
	}

	public String getHeadName() {
		return headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}
}
