package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListCardEntity extends BaseCardEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SimpleEntity> list = null;
	private String title;
	//是否显示更多
	private boolean isMore=false;

	public ListCardEntity() {
		setCardType(CARD_TYPE_LIST);
	}

	public List<SimpleEntity> getList() {
		return list;
	}

	public void setList(List<SimpleEntity> list) {
		this.list = list;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isMore() {
		return isMore;
	}

	public void setMore(boolean isMore) {
		this.isMore = isMore;
	}
}
