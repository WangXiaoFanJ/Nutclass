package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.dev.nutclass.utils.TextUtil;
import com.google.gson.JsonObject;

public class ImageEntity extends BaseCardEntity implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 目录下的图片数
	private int count = 0;
	// 目录名或者图片名称
	private String name;
	// 图片路径（路径中对应的第一张图片的路径）
	private String imgPath;
	// 在UI中的位置
	private int position = 0;
	// bigPath,smallPath是网络图片的路径.目前是pid
	private String bigPath;
	private String smallPath;
	private String imgName;
	private List<TagEntity> tagList;
	private String schema="";
	private String id;
	private String is_promotion;
	private String img_width;
	private String img_height;
	private String app_jump;
	private String h5_jump;
	private String app_jump_key;
	private String app_jump_value;
	//
	public ImageEntity(){
		setCardType(CARD_TYPE_BRAND);
	}
	public ImageEntity(JSONObject jsonObj){
		setCardType(CARD_TYPE_BRAND);
		optJsonObj(jsonObj);
	}
	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public List<TagEntity> getTagList() {
		return tagList;
	}

	public void setTagList(List<TagEntity> tagList) {
		this.tagList = tagList;
	}

	public boolean isEmpty() {
		return TextUtils.isEmpty(imgPath) && TextUtils.isEmpty(smallPath)
				&& TextUtils.isEmpty(bigPath);
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getBigPath() {
		return bigPath;
	}

	public void setBigPath(String bigPath) {
		this.bigPath = bigPath;
	}

	public String getSmallPath() {
		return smallPath;
	}

	public void setSmallPath(String smallPath) {
		this.smallPath = smallPath;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	//Banner以及运营位数据解析
	public void optJsonObj(JSONObject jsonObj){
		if(jsonObj==null)
			return;
		setImgName(jsonObj.optString("desc"));
		setSmallPath(jsonObj.optString("pid"));
		setSchema(jsonObj.optString("scheme"));
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getIs_promotion() {
		return is_promotion;
	}

	public void setIs_promotion(String is_promotion) {
		this.is_promotion = is_promotion;
	}

	public String getImg_width() {
		return img_width;
	}

	public void setImg_width(String img_width) {
		this.img_width = img_width;
	}

	public String getImg_height() {
		return img_height;
	}

	public void setImg_height(String img_height) {
		this.img_height = img_height;
	}

	public String getApp_jump() {
		return app_jump;
	}

	public void setApp_jump(String app_jump) {
		this.app_jump = app_jump;
	}

	public String getH5_jump() {
		return h5_jump;
	}

	public void setH5_jump(String h5_jump) {
		this.h5_jump = h5_jump;
	}

	public String getApp_jump_key() {
		return app_jump_key;
	}

	public void setApp_jump_key(String app_jump_key) {
		this.app_jump_key = app_jump_key;
	}

	public String getApp_jump_value() {
		return app_jump_value;
	}

	public void setApp_jump_value(String app_jump_value) {
		this.app_jump_value = app_jump_value;
	}

	private String h5Url;
	/**
	 * "id": 1,
		"name": "早教幼儿",
		"icon": "http://182.92.7.222/app_files_v2/images/icon/zaojiao.png",
		"activity": "",
		"url": ""
	 */
	//首页广告位Banner;
	public  void optJsonObjAD(JSONObject jsonObj){
		if(jsonObj == null){
			return;
		}
		setId(jsonObj.optString("id"));
		setSmallPath(jsonObj.optString("img"));
		setImg_width(jsonObj.optString("img_width"));
		setImg_height(jsonObj.optString("img_height"));
		setApp_jump(jsonObj.optString("app_jump"));
		setH5_jump(jsonObj.optString("h5_jump"));
		setH5Url(jsonObj.optString("h5_url"));
		setApp_jump_key(jsonObj.optString("app_jump_key"));
		setApp_jump_value(jsonObj.optString("app_jump_value"));
	}
	// 运营位数据解析
	public void optJsonObjNew(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setId(jsonObj.optString("id"));
		setImgName(jsonObj.optString("name"));
		setSmallPath(jsonObj.optString("icon"));
		setSchema(jsonObj.optString("activity"));
		setH5Url(jsonObj.optString("url"));
//		setIs_promotion(jsonObj.optString("is_promotion"));

	}
	public String getH5Url() {
		return h5Url;
	}
	public void setH5Url(String h5Url) {
		this.h5Url = h5Url;
	}

}
