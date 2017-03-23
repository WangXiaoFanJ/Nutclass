package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author LJ Tag信息
 */
public class SchoolEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String schoolName;
	private String schoolAddr;
	private String tel;
	private String lon;
	private String lat;
	private List<PriceEntity> priceList;
	public SchoolEntity() {
	}
	public SchoolEntity(JSONObject json) {
		optJsonObj(json);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getSchoolAddr() {
		return schoolAddr;
	}
	public void setSchoolAddr(String schoolAddr) {
		this.schoolAddr = schoolAddr;
	}
	public List<PriceEntity> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<PriceEntity> priceList) {
		this.priceList = priceList;
	}
	
	public void optJsonObj(JSONObject json){
		if(json==null)
			return;
		setId(json.optString("school_id"));
		setSchoolName(json.optString("school_name"));
		setSchoolAddr(json.optString("school_addr"));
		setTel(json.optString("tel"));
		JSONArray priceArray=json.optJSONArray("attr_price");
		if(priceArray!=null){
			priceList=new ArrayList<PriceEntity>();
			for(int i=0;i<priceArray.length();i++){
				PriceEntity entity=new PriceEntity(priceArray.optJSONObject(i));
				priceList.add(entity);
			}
		}
		setLon(json.optString("longitude"));
		setLat(json.optString("latitude"));
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}

}
