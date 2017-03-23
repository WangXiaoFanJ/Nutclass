package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.renderscript.BaseObj;

import com.dev.nutclass.utils.SharedPrefUtil;

/**
 * @author LJ 基本的用户信息(登录用户的信息)
 */
public class UserEntity extends BaseCardEntity implements Serializable {
	private static final long serialVersionUID = -4108673652430439788L;
//	1 qq  2 sina  3 wechat
	public static final int USER_TYPE_WEIBO = 2;
	public static final int USER_TYPE_WEIXIN = 3;
	public static final int USER_TYPE_QQ = 1;
	/**
	 * 需要本地保存的数据
	 * */
	// userid
	private String uid = "";//  
	// usernick(唯一)
	private String name;//  
	private String portrait;//  
	private String mobile;//  
	private String token;
	
	private String openId;//三方的uid
	private int type;
 	private String is_compny;

	public String getIs_compny() {
		return is_compny;
	}

	public void setIs_compny(String is_compny) {
		this.is_compny = is_compny;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 需要动态加载的数据
	 * */
 

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

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	 
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	

	

	public UserEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_USER);
	}

	// //
	// {"status":1,"info":"\u767b\u5f55\u6210\u529f","data":{"user_id":"2824","user_name":"13717710739","token":"542b60df5f633320fc246f85eefb6ecf","headerIconUrl":"http:\/\/www.nutclass.com\/images\/default.png"}}
	public UserEntity(JSONObject userObject) {
		// 解析基本信息
		optBaseInfo(userObject);
	}

	private void optBaseInfo(JSONObject baseObject) {
		this.uid = baseObject.optString("user_id");
		this.name = baseObject.optString("user_name");
		this.portrait = baseObject.optString("headerIconUrl");
		this.mobile=baseObject.optString("mobile_phone");
		
	}
	private int order1;
	private int order2;
	private int order3;
	private int order4;
	
	private int money1;
	private int money2;
	private int money3;
	public int getOrder1() {
		return order1;
	}

	public void setOrder1(int order1) {
		this.order1 = order1;
	}

	public int getOrder2() {
		return order2;
	}

	public void setOrder2(int order2) {
		this.order2 = order2;
	}

	public int getOrder3() {
		return order3;
	}

	public void setOrder3(int order3) {
		this.order3 = order3;
	}

	public int getOrder4() {
		return order4;
	}

	public void setOrder4(int order4) {
		this.order4 = order4;
	}

	public int getMoney1() {
		return money1;
	}

	public void setMoney1(int money1) {
		this.money1 = money1;
	}

	public int getMoney2() {
		return money2;
	}

	public void setMoney2(int money2) {
		this.money2 = money2;
	}

	public int getMoney3() {
		return money3;
	}

	public void setMoney3(int money3) {
		this.money3 = money3;
	}

	public int getMoney4() {
		return money4;
	}

	public void setMoney4(int money4) {
		this.money4 = money4;
	}
	private int money4;
	// 解析Me
	public void optUserInfo(JSONObject userObject) {
		this.uid = SharedPrefUtil.getInstance().getUid();
		this.name = userObject.optString("name");
		this.portrait = userObject.optString("headerIconUrl");
		setIs_compny(userObject.optString("is_compny"));
		JSONObject orderObj=userObject.optJSONObject("order");
		order1=orderObj.optInt("pay_order_count",0);
		order2=orderObj.optInt("goods_reservation_count",0);
		order3=orderObj.optInt("appraisal_count",0);
		order4=orderObj.optInt("sale_count",0);
		JSONObject moneyObj=userObject.optJSONObject("money");
		money1=moneyObj.optInt("money_flow",0);
		money2=moneyObj.optInt("coupon",0);
		money3=moneyObj.optInt("pay_points",0);
		money4=moneyObj.optInt("jianguo_card",0);
	}

	 

	public JSONObject buildJsonObject() throws JSONException {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_id", uid);
		jsonObject.put("user_name", name);
//		jsonObject.put("mobile", SharedPrefUtil.getInstance().getMobile());
		jsonObject.put("headerIconUrl", portrait);

		return jsonObject;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (o instanceof UserEntity) {
			if (((UserEntity) o).getUid().equals(this.uid)) {
				flag = true;
			}
		}
		return flag;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	private String profileLocalPath;
//	sex  性别  0 无  1男  2 女 
	private int sex;
	private String birthday;
	private String address;
	private int babayStatus;
	public String getProfileLocalPath() {
		return profileLocalPath;
	}

	public void setProfileLocalPath(String profileLocalPath) {
		this.profileLocalPath = profileLocalPath;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBabayStatus() {
		return babayStatus;
	}

	public void setBabayStatus(int babayStatus) {
		this.babayStatus = babayStatus;
	}

	/**
	 * image1  图片KEY
	   nick_name  昵称
	   sex        性别  1 男  2女
	   Birthday    生日  1900-01-01
	   District     地址   北京
	   Babystatus   宝宝状态  0无宝宝 1备孕 2已孕 已出生 
	 * */
	 
	// 解析用户信息编辑
	public void optUserInfoEdit(JSONObject userObject) {
		this.uid = SharedPrefUtil.getInstance().getUid();
		this.name = userObject.optString("nick_name");
		this.portrait = userObject.optString("headimgurl");
		this.sex = userObject.optInt("sex",1);
		this.birthday = userObject.optString("birthday");
		this.address = userObject.optString("district");
		this.babayStatus = userObject.optInt("babystatus",0);
	} 
}
