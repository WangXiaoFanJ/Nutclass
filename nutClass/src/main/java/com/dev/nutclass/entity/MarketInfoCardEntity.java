package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;

public class MarketInfoCardEntity extends BaseCardEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String district;
	private String price;
	private String views;
	private String description;
	private String portrait;
	private String date;
	private BannerCardEntity bannerCardEntity;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MarketInfoCardEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_MARKET_INFO);
	}

	public MarketInfoCardEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_MARKET_INFO);
		optJsonObj(jsonObject);
	}

	/**
	 * "activityId":"8", "activityImageUrl":
	 * "http://www.nutclass.com/ad_imgs/1438871232767308818.jpg",
	 * "activityTitle":"海淀/万柳全英文写生活动", "address":"北京市海淀区蓝靛厂北路---巴沟山水园内",
	 * "activityTag":"进行中"
	 */

	// 运营位数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		JSONObject goodJsonObj = jsonObj.optJSONObject("goods_info");
		if (goodJsonObj == null)
			return;
		setId(goodJsonObj.optString("m_id"));
		setTitle(goodJsonObj.optString("title"));
		setDistrict(goodJsonObj.optString("district"));
		setPrice(goodJsonObj.optString("price"));
		setViews(goodJsonObj.optString("views"));
		setDescription(goodJsonObj.optString("m_description"));
		JSONObject infoObj = goodJsonObj.optJSONObject("publish_info");
		if (infoObj != null) {
			setPortrait(infoObj.optString("portrait"));
			setDate(infoObj.optString("p_date"));
		}
		bannerCardEntity = new BannerCardEntity();
		List<ImageEntity> imgList = new ArrayList<ImageEntity>();
		JSONArray imgArray = goodJsonObj.optJSONArray("images_list");
		if (imgArray != null && imgArray.length() > 0) {
			for (int i = 0; i < imgArray.length(); i++) {
				ImageEntity imgEntity = new ImageEntity();
				imgEntity.setSmallPath(UrlConst.BASE_NET_HOST
						+ imgArray.optString(i));
				imgList.add(imgEntity);
			}
			bannerCardEntity.setList(imgList);
		}
	}

	public BannerCardEntity getBannerCardEntity() {
		return bannerCardEntity;
	}

	public void setBannerCardEntity(BannerCardEntity bannerCardEntity) {
		this.bannerCardEntity = bannerCardEntity;
	}
}
