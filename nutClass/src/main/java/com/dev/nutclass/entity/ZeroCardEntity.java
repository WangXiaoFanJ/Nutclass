package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.utils.TextUtil;

public class ZeroCardEntity extends BaseCardEntity {

	private static final long serialVersionUID = 1L;

	private String zeroActivityId;
	private int zeroActivityNo;
	private String zeroActivityTag;
	private String zeroActivityImageUrl;
	private String zeroActivityTitle;
	private String zeroActivityPrice;
	private long startTime;
	private long endTime;
	private String personCount;
	private String activityDetail;

	private int type = 0;

	private int from = -1;

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public String getZeroActivityId() {
		return zeroActivityId;
	}

	public void setZeroActivityId(String zeroActivityId) {
		this.zeroActivityId = zeroActivityId;
	}

	public int getZeroActivityNo() {
		return zeroActivityNo;
	}

	public void setZeroActivityNo(int zeroActivityNo) {
		this.zeroActivityNo = zeroActivityNo;
	}

	public String getZeroActivityTag() {
		return zeroActivityTag;
	}

	public void setZeroActivityTag(String zeroActivityTag) {
		this.zeroActivityTag = zeroActivityTag;
	}

	public String getZeroActivityImageUrl() {
		return zeroActivityImageUrl;
	}

	public void setZeroActivityImageUrl(String zeroActivityImageUrl) {
		this.zeroActivityImageUrl = zeroActivityImageUrl;
	}

	public String getZeroActivityTitle() {
		return zeroActivityTitle;
	}

	public void setZeroActivityTitle(String zeroActivityTitle) {
		this.zeroActivityTitle = zeroActivityTitle;
	}

	public String getZeroActivityPrice() {
		return zeroActivityPrice;
	}

	public void setZeroActivityPrice(String zeroActivityPrice) {
		this.zeroActivityPrice = zeroActivityPrice;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getPersonCount() {
		return personCount;
	}

	public void setPersonCount(String personCount) {
		this.personCount = personCount;
	}

	public String getActivityDetail() {
		return activityDetail;
	}

	public void setActivityDetail(String activityDetail) {
		this.activityDetail = activityDetail;
	}

	public ZeroCardEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_ZERO);
	}

	public ZeroCardEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_ZERO);
		optJsonObj(jsonObject);
	}

	// 数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setZeroActivityId(jsonObj.optString("zeroActivityId"));
		setZeroActivityNo(jsonObj.optInt("zeroActivityNo", 0));
		setZeroActivityTag(jsonObj.optString("zeroActivityTag"));
		setZeroActivityImageUrl(jsonObj.optString("zeroActivityImageUrl"));
		setZeroActivityTitle(jsonObj.optString("zeroActivityTitle"));
		setZeroActivityPrice(jsonObj.optString("zeroActivityPrice"));
		String st = jsonObj.optString("startTime");
		if (TextUtil.isNotNull(st)) {
			setStartTime(Long.parseLong(st));
		}
		String et = jsonObj.optString("endTime");
		if (TextUtil.isNotNull(et)) {
			setEndTime(Long.parseLong(et));
		}
		setPersonCount(jsonObj.optString("personCount"));
		setActivityDetail(jsonObj.optString("activityDetail"));

		if (TextUtil.isNotNull(getZeroActivityTag())) {
			if (getZeroActivityTag().equals("正在进行中")) {
				setType(0);
			} else if (getZeroActivityTag().equals("下期预告")) {
				setType(1);
			} else if (getZeroActivityTag().equals("已结束")) {
				setType(2);
			}
		}
	}

	// 我的页面的零元抢
	public void optUserInfoJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		/**
		 * "zero_id": "4", "title": "樱花日语课程0元抢", "img":
		 * "http://www.nutclass.com/data/article/1443329759837285734.jpg",
		 * "time": "2015-10-08", "status": "已结束"
		 * */
		setZeroActivityId(jsonObj.optString("zero_id"));
		setZeroActivityTag(jsonObj.optString("status"));
		setZeroActivityImageUrl(jsonObj.optString("img"));
		setZeroActivityTitle(jsonObj.optString("title"));
		setZeroActivityPrice(jsonObj.optString("zeroActivityPrice"));
		if (TextUtil.isNotNull(getZeroActivityTag())) {
			if (getZeroActivityTag().equals("正在进行中")) {
				setType(0);
			} else if (getZeroActivityTag().equals("下期预告")) {
				setType(1);
			} else if (getZeroActivityTag().equals("已结束")) {
				setType(2);
			}
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
