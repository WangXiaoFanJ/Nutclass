package com.dev.nutclass.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.google.gson.JsonObject;

public class CooponCardEntity extends BaseCardEntity {

	// 1 已使用 2 未使用 3 已过期
	public final static int TYPE_ALL = 0;//
	public final static int TYPE_USED = 1;//
	public final static int TYPE_NOT_USE = 2;//
	public final static int TYPE_EXPIRED = 3;//
	private static final long serialVersionUID = 1L;
	private String id = "";
	private String name = "";
	private String money = "";
	private String limit = "";
	private String availTime = "";
	private int type = 0;
	private String useTime = "";
	private String useSource = "";
	private String bonus_type_name;
	private String zd_goods_bonus;
	private String bonus_type;
	public CooponCardEntity(JSONObject jsonObj){
		setCardType(CARD_TYPE_COOPON);
		optJsonObj(jsonObj);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getAvailTime() {
		return availTime;
	}

	public void setAvailTime(String availTime) {
		this.availTime = availTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getUseSource() {
		return useSource;
	}

	public void setUseSource(String useSource) {
		this.useSource = useSource;
	}

	public String getBonus_type_name() {
		return bonus_type_name;
	}

	public void setBonus_type_name(String bonus_type_name) {
		this.bonus_type_name = bonus_type_name;
	}

	public String getZd_goods_bonus() {
		return zd_goods_bonus;
	}

	public void setZd_goods_bonus(String zd_goods_bonus) {
		this.zd_goods_bonus = zd_goods_bonus;
	}

	public String getBonus_type() {
		return bonus_type;
	}

	public void setBonus_type(String bonus_type) {
		this.bonus_type = bonus_type;
	}

	/*
             * bonus_id: "3136", type_name: "100元代金券", type_money: "100.00", min_amount:
             * "5000.00", use_date: "2016.07.12--2016.10.10", type_img: "", send_time:
             * "1468305976", used_time: "0", use_type: 2, use_source: "APP"
             */
	// 课程列表页Course数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setId(jsonObj.optString("bonus_id"));
		setName(jsonObj.optString("type_name"));
		setBonus_type(jsonObj.optString("bonus_type"));
		setMoney(jsonObj.optString("type_money"));
		setLimit(jsonObj.optString("min_amount"));
		setUseTime(jsonObj.optString("use_date"));
		setType(jsonObj.optInt("use_type", TYPE_ALL));
		setUseSource(jsonObj.optString("use_source"));
		setBonus_type_name(jsonObj.optString("bonus_type_name"));
		setZd_goods_bonus(jsonObj.optString("zd_goods_bonus"));
	}

	

}
