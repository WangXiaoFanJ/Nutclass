package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionInfoCardEntity extends BaseCardEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String icon;
	private String activityTitle;
	private String activityTag;
	private String address;
	private String userName;
	private String userProfile;
	private String detailInfo;
	private List<String> activityImageList;
	private String returnFee;
	private String activityTime;
	private String activityAge;
	private List<String> headerList;
	
	private int hasPraise;
	private int hasEnroll;
	private int commentNum;
	private ShareEntity shareEntity;
	public ShareEntity getShareEntity() {
		return shareEntity;
	}

	public void setShareEntity(ShareEntity shareEntity) {
		this.shareEntity = shareEntity;
	}

	public int getHasEnroll() {
		return hasEnroll;
	}

	public void setHasEnroll(int hasEnroll) {
		this.hasEnroll = hasEnroll;
	}

	private int praiseTotal;
	
	
	public int getHasPraise() {
		return hasPraise;
	}

	public void setHasPraise(int hasPraise) {
		this.hasPraise = hasPraise;
	}

	 

	public int getPraiseTotal() {
		return praiseTotal;
	}

	public void setPraiseTotal(int praiseTotal) {
		this.praiseTotal = praiseTotal;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public List<String> getActivityImageList() {
		return activityImageList;
	}

	public void setActivityImageList(List<String> activityImageList) {
		this.activityImageList = activityImageList;
	}

	public String getReturnFee() {
		return returnFee;
	}

	public void setReturnFee(String returnFee) {
		this.returnFee = returnFee;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	public String getActivityAge() {
		return activityAge;
	}

	public void setActivityAge(String activityAge) {
		this.activityAge = activityAge;
	}

	public List<String> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<String> headerList) {
		this.headerList = headerList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public String getActivityTag() {
		return activityTag;
	}

	public void setActivityTag(String activityTag) {
		this.activityTag = activityTag;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ActionInfoCardEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_ACTION_INFO);
	}

	public ActionInfoCardEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_ACTION_INFO);
		optJsonObj(jsonObject);
	}
	/**
	 *"activityId":"8",
	"activityImageUrl":"http://www.nutclass.com/ad_imgs/1438871232767308818.jpg",
	"activityTitle":"海淀/万柳全英文写生活动",
	"address":"北京市海淀区蓝靛厂北路---巴沟山水园内",
	"activityTag":"进行中"
	 */
	 
	// 运营位数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		 setActivityTitle(jsonObj.optString("activityTitle"));
		 setIcon(jsonObj.optString("activityImageUrl"));
		 setAddress(jsonObj.optString("address"));
		 
		 setUserName(jsonObj.optString("orgnizationName"));
		 setUserProfile(jsonObj.optString("orgnizationImageUrl"));
		 
		 JSONObject infoObj=jsonObj.optJSONObject("orgnizationInfo");
		 if(infoObj!=null){
			 setDetailInfo(infoObj.optString("detailInfo"));
			 JSONArray imgArray=infoObj.optJSONArray("activityImageList");
			 if(imgArray!=null&&imgArray.length()>0){
				 activityImageList=new ArrayList<String>();
				 for(int i=0;i<imgArray.length();i++){
					 activityImageList.add(imgArray.optString(i));
				 }
			 }
		 }
		 
		 
		 setReturnFee(jsonObj.optString("returnFee"));
		 setActivityTime(jsonObj.optString("activityTime"));
		 setActivityAge(jsonObj.optString("activtyAge"));
		 
		 JSONArray headArray=jsonObj.optJSONArray("headerlist");
		 if(headArray!=null&&headArray.length()>0){
			 headerList=new ArrayList<String>();
			 for(int i=0;i<headArray.length();i++){
				 headerList.add(headArray.optString(i));
			 }
		 }
		 JSONObject shareObj=jsonObj.optJSONObject("share_info");
			if(shareObj!=null){
				shareEntity=new ShareEntity();
				shareEntity.setDesc(shareObj.optString("context"));
				shareEntity.setTitle(shareObj.optString("title"));
				shareEntity.setImg(shareObj.optString("image"));
				shareEntity.setUrl(shareObj.optString("url"));
			}
		 setHasPraise(jsonObj.optInt("hasPraise",0));
		 setHasEnroll(jsonObj.optInt("has_enroll",0));
		 setPraiseTotal(jsonObj.optInt("praiseTotal"));
		 setCommentNum(jsonObj.optInt("commentTotal"));
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
}
