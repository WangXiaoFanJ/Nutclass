package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.utils.SharedPrefUtil;

public class FeedCardEntity extends BaseCardEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String uid;
	private String name;
	private String headImgUrl;
	private String content;
	private String time;
	private boolean islike;
	private String likeNum;
	private String commentNum;
	private List<CommentCardEntity> commentList;
	private List<UserEntity> userList;
	private ArrayList<ImageEntity> imgList;
	private boolean selfProfile=false;
	
	public static int TYPE_DEL=0;
	public static int TYPE_LIKE=1;
	public static int TYPE_COMMENT=2;

	public ArrayList<ImageEntity> getImgList() {
		return imgList;
	}

	public void setImgList(ArrayList<ImageEntity> imgList) {
		this.imgList = imgList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isIslike() {
		return islike;
	}

	public void setIslike(int islike) {
		if(islike>0){
			this.islike=true;
		}else{
			this.islike=false;
		}
	}
	public void setIslike(boolean islike) {
		this.islike = islike;
	}

	public String getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public List<CommentCardEntity> getCommentList() {
		if(commentList==null){
			commentList=new ArrayList<CommentCardEntity>();
		}
		return commentList;
		
	}

	public void setCommentList(List<CommentCardEntity> commentList) {
		this.commentList = commentList;
	}

	public FeedCardEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_FEED);
	}

	public FeedCardEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_FEED);
		optJsonObj(jsonObject);
	}
	/**
	 *a_id: "56",
user_id: "55394",
nick_name: "理解理解",
s_class: null,
headimgurl: "http://cdn2.kobiko.cn/images/201606/1466590842579571951.jpg",
content: "",
add_time: "2016-07-01",
is_best: "0",
zan_num: "0",
content_num: "0",
imgs: [
"http://cdn1.kobiko.cn/./Uploads/Shequ/2016-07-01/5776417790e9d.jpg"
]
comment: [
{
c_id: "31",
nick_name: "理解理解",
content: "咯好了",
a_id: "8",
is_audit: "1"
}
]
	 */
	 
	// 运营位数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		 setId(jsonObj.optString("a_id"));
		 setUid(jsonObj.optString("user_id"));
		 setName(jsonObj.optString("nick_name"));
		 setHeadImgUrl(jsonObj.optString("headimgurl"));
		 setContent(jsonObj.optString("content"));
		 setTime(jsonObj.optString("add_time"));
		 setIslike(jsonObj.optInt("my_zan"));
		 setLikeNum(jsonObj.optString("zan_num"));
		 setCommentNum(jsonObj.optString("content_num"));
		 JSONArray img=jsonObj.optJSONArray("imgs");
		 if(img!=null&&img.length()>0){
			 imgList=new ArrayList<ImageEntity>();
			 for(int i=0;i<img.length();i++){
				 ImageEntity entity=new ImageEntity();
				 entity.setSmallPath(img.optString(i));
				 imgList.add(entity);
			 }
		 }
		 JSONArray comment=jsonObj.optJSONArray("comment");
		 if(comment!=null&&comment.length()>0){
			 commentList=new ArrayList<CommentCardEntity>();
			 for(int i=0;i<comment.length();i++){
				 try {
					 CommentCardEntity entity=new CommentCardEntity();
					 entity.optJsonObjFromFeed(comment.getJSONObject(i));
					 commentList.add(entity);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			 }
		 } 
		 JSONArray user=jsonObj.optJSONArray("zan_user");
		 if(user!=null&&user.length()>0){
			 userList=new ArrayList<UserEntity>();
			 for(int i=0;i<user.length();i++){
				 try {
					 UserEntity entity=new UserEntity();
					 JSONObject obj=user.getJSONObject(i);
					 if(obj!=null){
						 entity.setUid(obj.optString("uid"));
						 entity.setName(obj.optString("nick_name"));
					 }
					 userList.add(entity);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			 }
		 }

	}

	public boolean isSelfProfile() {
		return selfProfile;
	}

	public void setSelfProfile(boolean selfProfile) {
		this.selfProfile = selfProfile;
	}

	public List<UserEntity> getUserList() {
		return userList;
	}

	public void setUserList(List<UserEntity> userList) {
		this.userList = userList;
	}
	public void addLiker(){
		if(userList==null){
			userList=new ArrayList<UserEntity>();
		}
		UserEntity user=SharedPrefUtil.getInstance().getSession();
		if(!userList.contains(user)){
			userList.add(user);
		}
		
	}
	public void removeLiker(){
		if(userList!=null){
			UserEntity user=SharedPrefUtil.getInstance().getSession();
			userList.remove(user);
		}
	}
}
