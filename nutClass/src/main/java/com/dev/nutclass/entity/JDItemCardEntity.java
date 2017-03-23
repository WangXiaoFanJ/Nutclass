package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JDItemCardEntity extends BaseCardEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String picLeftUrl = null;
	private String picRightUrl = null;

	private String leftDesc = null;
	private String rightDesc = null;
	
	private String leftId = null;
	private String rightId = null;
	
	private String leftPrice = null;
	private String rightPrice= null;
	
	  

	public String getPicLeftUrl() {
		return picLeftUrl;
	}

	public void setPicLeftUrl(String picLeftUrl) {
		this.picLeftUrl = picLeftUrl;
	}

	public String getPicRightUrl() {
		return picRightUrl;
	}

	public void setPicRightUrl(String picRightUrl) {
		this.picRightUrl = picRightUrl;
	}

	public String getLeftDesc() {
		return leftDesc;
	}

	public void setLeftDesc(String leftDesc) {
		this.leftDesc = leftDesc;
	}

	public String getRightDesc() {
		return rightDesc;
	}

	public void setRightDesc(String rightDesc) {
		this.rightDesc = rightDesc;
	}

	public String getLeftId() {
		return leftId;
	}

	public void setLeftId(String leftId) {
		this.leftId = leftId;
	}

	public String getRightId() {
		return rightId;
	}

	public void setRightId(String rightId) {
		this.rightId = rightId;
	}

	public String getLeftPrice() {
		return leftPrice;
	}

	public void setLeftPrice(String leftPrice) {
		this.leftPrice = leftPrice;
	}

	public String getRightPrice() {
		return rightPrice;
	}

	public void setRightPrice(String rightPrice) {
		this.rightPrice = rightPrice;
	}

	public JDItemCardEntity() {
		setCardType(CARD_TYPE_JD_DOUBLE_CARD);
	}

	public JDItemCardEntity(JSONObject leftObj,JSONObject rightObj) {
		setCardType(CARD_TYPE_JD_DOUBLE_CARD);
		optJsonObj(leftObj,rightObj);
	}

	

	/**
	 * id: "511",
gid: "2717499",
gcate: "2",
gname: "巴拉巴拉男童短袖t恤中大童上衣童装儿童学生T恤男22172151107本白130",
gprice: "49.00",
commission: "1.50",
forecast_commission: "0.74",
cps_url: "http://union.click.jd.com/jdc?e=&p=AyIBZRprFgQSAVATUyVGTV8LRGtMR1dGXgVFT1ZTWgFYRE5XDVULR0VNR0ZbW1kBUQ4RAVUdXh0KInxQZg5MYBYPNxoHR3Zkfgl9JGp%2FTVFZF2sQARQDUBxYFAYiB1QaWhQEEg5UGmslZG83A3VbFAMTBlQdUhYFIgZlG14TARsOUhxYHAoUBGUSUyUyIjdl&t=W1dCFBBFC1hGRghPGApZRxgHRQcLWldTCRUZT0YeBFMbXRAKGg%3D%3D",
cps_start_time: "2016-04-01",
cps_end_time: "2100-01-01",
image: "http://cdn2.kobiko.cn//Uploads/jd_kepler/jd_img/2717499.jpg",
is_online: "1"
	 * 
	 * */
	public void optJsonObj(JSONObject leftObj,JSONObject rightObj) {
		 if(leftObj!=null){
			 setLeftId(leftObj.optString("gid"));
			 setLeftDesc(leftObj.optString("gname"));
			 setLeftPrice(leftObj.optString("gprice"));
			 setPicLeftUrl(leftObj.optString("image"));
		 }
		 if(rightObj!=null){
			 setRightId(rightObj.optString("gid"));
			 setRightDesc(rightObj.optString("gname"));
			 setRightPrice(rightObj.optString("gprice"));
			 setPicRightUrl(rightObj.optString("image"));
		 }

	}

	 
}
