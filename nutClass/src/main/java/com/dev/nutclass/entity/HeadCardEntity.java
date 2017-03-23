package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HeadCardEntity extends BaseCardEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String h5Url = null;

	public HeadCardEntity() {
		setCardType(CARD_TYPE_HEAD);
	}

	public HeadCardEntity(JSONArray jsonArray) {
		setCardType(CARD_TYPE_HEAD);
		optJsonObj(jsonArray);
	}

	

	// 首页头部
	public void optJsonObj(JSONArray jsonArray) {
		if (jsonArray == null)
			return;
		
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				setH5Url(jsonArray.getString(0));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public String getH5Url() {
		return h5Url;
	}

	public void setH5Url(String h5Url) {
		this.h5Url = h5Url;
	}
}
