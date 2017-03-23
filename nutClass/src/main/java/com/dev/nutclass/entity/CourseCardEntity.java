 package com.dev.nutclass.entity;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.view.WaitingPayView;
import com.google.gson.JsonObject;

public class CourseCardEntity extends BaseCardEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String distance;
	private String courseName;
	private String id;
	private int level;
	private String priceMax;
	private String priceReturn;
	private String icon;
	private String address;
	private BannerCardEntity bannerCardEntity;
	private PlusPriceBuyEntity plusPriceBuyEntity;
	private List<SchoolEntity> schoolList;
	private List<PlusPriceBuyEntity> plusPriceBuyList;
	
	private String payType="3";
	private String payFQ="";
	private List<FQEntity> payFQList;
	
	private String keyStr;
	private String tel;
	
	private List<CourseCardEntity> courseList;
	private int flag1;//七天
	private int flag2;//官方
	private int flag3;//保险
	private int flag4;//信用卡
	private int flag5;//预约
	private String age;
	private int isPromotion;
	private String bonus_id;
	private String bonus_name;
	private String is_pro;
	private String is_pro_img;
	private String is_pro_text;

	private String plus_price_buy;
	private String plus_price_total_money;
	private String plus_price_total_num;
	private String logo;
	private String brandName;
	public List<PlusPriceBuyEntity> getPlusPriceBuyList() {
		return plusPriceBuyList;
	}

	public void setPlusPriceBuyList(List<PlusPriceBuyEntity> plusPriceBuyList) {
		this.plusPriceBuyList = plusPriceBuyList;
	}

	public String getIs_pro_text() {
		return is_pro_text;
	}

	public void setIs_pro_text(String is_pro_text) {
		this.is_pro_text = is_pro_text;
	}

	public String getIs_pro_img() {
		return is_pro_img;
	}

	public void setIs_pro_img(String is_pro_img) {
		this.is_pro_img = is_pro_img;
	}

	public int getIsPromotion() {
		return isPromotion;
	}

	public void setIsPromotion(int isPromotion) {
		this.isPromotion = isPromotion;
	}

	public String getBonus_id() {
		return bonus_id;
	}

	public void setBonus_id(String bonus_id) {
		this.bonus_id = bonus_id;
	}

	public String getBonus_name() {
		return bonus_name;
	}

	public void setBonus_name(String bonus_name) {
		this.bonus_name = bonus_name;
	}

	public List<CourseCardEntity> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<CourseCardEntity> courseList) {
		this.courseList = courseList;
	}

	public int getFlag1() {
		return flag1;
	}

	public void setFlag1(int flag1) {
		this.flag1 = flag1;
	}

	public int getFlag2() {
		return flag2;
	}

	public void setFlag2(int flag2) {
		this.flag2 = flag2;
	}

	public int getFlag3() {
		return flag3;
	}

	public void setFlag3(int flag3) {
		this.flag3 = flag3;
	}

	public int getFlag4() {
		return flag4;
	}

	public void setFlag4(int flag4) {
		this.flag4 = flag4;
	}

	public int getFlag5() {
		return flag5;
	}

	public void setFlag5(int flag5) {
		this.flag5 = flag5;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getKeyStr() {
		return keyStr;
	}

	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIs_pro() {
		return is_pro;
	}

	public void setIs_pro(String is_pro) {
		this.is_pro = is_pro;
	}

	public String getPlus_price_buy() {
		return plus_price_buy;
	}

	public void setPlus_price_buy(String plus_price_buy) {
		this.plus_price_buy = plus_price_buy;
	}

	public String getPlus_price_total_money() {
		return plus_price_total_money;
	}

	public void setPlus_price_total_money(String plus_price_total_money) {
		this.plus_price_total_money = plus_price_total_money;
	}

	public String getPlus_price_total_num() {
		return plus_price_total_num;
	}

	public void setPlus_price_total_num(String plus_price_total_num) {
		this.plus_price_total_num = plus_price_total_num;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	private int dbId;//对应数据库id
	
	private ArrayList<String> descImgList;
	private boolean isEdit=false;
	//选中的学校  以及课节数
	private SchoolEntity schoolEntity;
	private PriceEntity priceEntity;
	
	private String retIds;
	
	private String cardName;
	
	private int from=-1;
	
	private String schoolId;
	private String schoolHour;

	public String getSchoolHour() {
		return schoolHour;
	}

	public void setSchoolHour(String schoolHour) {
		this.schoolHour = schoolHour;
	}

	private ShareEntity shareEntity;
	public BannerCardEntity getBannerCardEntity() {
		return bannerCardEntity;
	}

	public void setBannerCardEntity(BannerCardEntity bannerCardEntity) {
		this.bannerCardEntity = bannerCardEntity;
	}

	public List<SchoolEntity> getSchoolList() {
		return schoolList;
	}

	public void setSchoolList(List<SchoolEntity> schoolList) {
		this.schoolList = schoolList;
	}

	public SchoolEntity getSchoolEntity() {
		return schoolEntity;
	}

	public void setSchoolEntity(SchoolEntity schoolEntity) {
		this.schoolEntity = schoolEntity;
	}

	public PriceEntity getPriceEntity() {
		return priceEntity;
	}

	public void setPriceEntity(PriceEntity priceEntity) {
		this.priceEntity = priceEntity;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(String priceMax) {
		this.priceMax = priceMax;
	}

	public String getPriceReturn() {
		return priceReturn;
	}

	public void setPriceReturn(String priceReturn) {
		this.priceReturn = priceReturn;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CourseCardEntity() {
		setCardType(BaseCardEntity.CARD_TYPE_COURSE);
	}

	public CourseCardEntity(JSONObject jsonObject) {
		setCardType(BaseCardEntity.CARD_TYPE_COURSE);
		optJsonObj(jsonObject);
	}

	/**
	 * "distance":294, "goods_name":"玛尔比恩VIP套餐", "lv":4, "id":"368",
	 * "max_gwk":"700.00", "shop_price":"10400.00", "thumb":
	 * "http://www.nutclass.com/images/201506/thumb_img/368_thumb_G_1434777435288.jpg"
	 * , "adders":"朝阳区百子湾东里沿海赛洛城211-2玛尔比恩早教中心"
	 */

	// 课程列表页Course数据解析
	public void optJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setDistance(jsonObj.optString("distance"));
		setLevel(jsonObj.optInt("lv"));
		setCourseName(jsonObj.optString("goods_name"));
		setId(jsonObj.optString("id"));
		setPriceMax(jsonObj.optString("shop_price"));
		setPriceReturn(jsonObj.optString("max_gwk"));
		setIcon(jsonObj.optString("thumb"));
		setAddress(jsonObj.optString("adders"));
		setRetIds(jsonObj.optString("retids"));
		setSchoolId(jsonObj.optString("school_id"));
		setTel(jsonObj.optString("tel"));
		setLogo(jsonObj.optString("logo"));
		setBrandName(jsonObj.optString("brand_name"));
		setPlus_price_buy(jsonObj.optString("plus_price_buy"));
		setPlus_price_total_money(jsonObj.optString("plus_price_total_money"));
		setPlus_price_total_num(jsonObj.optString("plus_price_total_num"));
		setSchoolHour(jsonObj.optString("school_hour"));
	}
	//转换成购物车数据
	public String obj2JsonStr() {
		JSONObject jsonObj=new JSONObject();
		try {
			jsonObj.put("distance", getDistance());
			jsonObj.put("lv", getLevel());
			jsonObj.put("goods_name", getCourseName());
			jsonObj.put("id", getId());
			jsonObj.put("shop_price",getPriceMax());
			jsonObj.put("max_gwk",getPriceEntity().getBackMoney());
			jsonObj.put("school_hour",getSchoolHour());
			if(bannerCardEntity!=null){
				List<ImageEntity> imgList=bannerCardEntity.getList();
				if(imgList!=null&&imgList.size()>0){
					jsonObj.put("thumb", imgList.get(0).getSmallPath());
				}
			}
			jsonObj.put("adders", getSchoolEntity().getSchoolAddr());
			jsonObj.put("retids", getRetIds());
			jsonObj.put("plus_price_buy",getPlus_price_buy());
			jsonObj.put("plus_price_total_money",getPlus_price_total_money());
			jsonObj.put("plus_price_total_num",getPlus_price_total_num());
			Log.d("===","plus_price_buy,"+getPlus_price_buy());
			jsonObj.put("school_id",getSchoolId());
			jsonObj.put("logo",getLogo());
			jsonObj.put("brand_name",getBrandName());
			jsonObj.put("tel", getSchoolEntity().getTel());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj.toString();
		
	}

	// 品牌馆表页Course数据解析
	public void optJsonObjBrand(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setId(jsonObj.optString("product_id"));
		setLevel(jsonObj.optInt("levelInfo"));
		setIcon(jsonObj.optString("headerImageUrl"));
		setCourseName(jsonObj.optString("courseName"));
		setPriceMax(jsonObj.optString("price"));
		setPriceReturn(jsonObj.optString("returnFee"));
		setAddress(jsonObj.optString("adders"));
		setDistance(jsonObj.optString("distance"));
		setSchoolId(jsonObj.optString("school_id"));
		

	}
	private String desc;//描述
	private int brandId;
	// 课程详情页Course数据解析
	/**
	 * inco_list: {
age: "3-12岁",
tag1: 1,
tag2: 1,
tag3: 0,
tag4: 0,
tag5: 1
}
	 * */
	public void optJsonObjInfo(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setId(jsonObj.optString("goods_id"));
		setCourseName(jsonObj.optString("goods_name"));
		setDesc(jsonObj.optString("goods_desc"));
		setLogo(jsonObj.optString("brand_img"));
		setBrandName(jsonObj.optString("brand_name"));
		setLevel(jsonObj.optInt("lv"));
		setIsPromotion(jsonObj.optInt("is_promotion"));
		setBrandId(jsonObj.optInt("brand_id"));
		setPriceMax(jsonObj.optString("shop_price"));
		setIcon(jsonObj.optString("thumb"));
		setBonus_id(jsonObj.optString("bonus_id"));
		setBonus_name(jsonObj.optString("bonus_name"));
		setIs_pro(jsonObj.optString("is_pro"));
		setIs_pro_img(jsonObj.optString("is_pro_img"));
		setIs_pro_text(jsonObj.optString("is_pro_text"));
		bannerCardEntity = new BannerCardEntity();
		JSONObject flagObj=jsonObj.optJSONObject("inco_list");
		if(flagObj!=null){
			setAge(flagObj.optString("age"));
			setFlag1(flagObj.optInt("tag1",0));
			setFlag2(flagObj.optInt("tag2",0));
			setFlag3(flagObj.optInt("tag3",0));
			setFlag4(flagObj.optInt("tag4",0));
			setFlag5(flagObj.optInt("tag5",0));
		}
		
		JSONArray productArray=jsonObj.optJSONArray("product_list");
		if(productArray!=null&&productArray.length()>0){
			courseList=new ArrayList<CourseCardEntity>();
			for(int i=0;i<productArray.length();i++){
				CourseCardEntity entity=new CourseCardEntity(productArray.optJSONObject(i));
				courseList.add(entity);
			}
			
		}
		
		JSONArray bannerArray=jsonObj.optJSONArray("th_imgs");
		List<ImageEntity> imgList=new ArrayList<ImageEntity>();
		for(int i=0;i<bannerArray.length();i++){
			ImageEntity imgEntity=new ImageEntity();
			imgEntity.setSmallPath(UrlConst.BASE_NET_HOST+bannerArray.optString(i));
			imgList.add(imgEntity);
		}
		bannerCardEntity.setList(imgList);
		JSONArray schoolArray=jsonObj.optJSONArray("school_list");
		if(schoolArray!=null){
			schoolList=new ArrayList<SchoolEntity>();
			for(int i=0;i<schoolArray.length();i++){
				SchoolEntity entity=new SchoolEntity(schoolArray.optJSONObject(i));
				schoolList.add(entity);
				if(i==0){
					schoolEntity=entity;
					List<PriceEntity> priceList=entity.getPriceList();
					if(priceList!=null){
						priceEntity=priceList.get(0);
					}
				}
			}
		}
		JSONArray descImgArray=jsonObj.optJSONArray("desc_imgs");
		if(descImgArray!=null&&descImgArray.length()>0){
			descImgList=new ArrayList<String>();
			for(int i=0;i<descImgArray.length();i++){
				String str=UrlConst.BASE_NET_HOST+descImgArray.optString(i);
				descImgList.add(str);
			}
		}
//		"share_info": {
//			"title": "亲亲袋鼠脑神经亲子课",
//			"context": "帮助您的宝宝奠定未来人生的基础。",
//			"image": "http://182.92.7.222/Uploads/App_imgs/20160306/56dbf957c78d5.jpg",
//			"url": "http://new.kobiko.cn/Html5/Index/get_product_detail/pid/273"
//			}
		JSONObject shareObj=jsonObj.optJSONObject("share_info");
		if(shareObj!=null){
			shareEntity=new ShareEntity();
			shareEntity.setDesc(shareObj.optString("context"));
			shareEntity.setTitle(shareObj.optString("title"));
			shareEntity.setImg(shareObj.optString("image"));
			shareEntity.setUrl(shareObj.optString("url"));
		}
		JSONArray plusPriceBuyArray = jsonObj.optJSONArray("plus_price_buy");
		if(plusPriceBuyArray!=null&&plusPriceBuyArray.length()>0){
			plusPriceBuyList = new ArrayList<>();
			for (int i = 0;i<plusPriceBuyArray.length();i++){
				JSONObject plusPriceObj = plusPriceBuyArray.optJSONObject(i);
				plusPriceBuyEntity = new PlusPriceBuyEntity();
				plusPriceBuyEntity.setId(plusPriceObj.optString("id"));
				plusPriceBuyEntity.setGoodsName(plusPriceObj.optString("goods_name"));
				plusPriceBuyEntity.setGoodsImgUrl(plusPriceObj.optString("goods_img_url"));
				plusPriceBuyEntity.setGoodsDetailUrl(plusPriceObj.optString("goods_detail_url"));
				plusPriceBuyEntity.setMarketPrice(plusPriceObj.optString("market_price"));
				plusPriceBuyEntity.setCurentPrice(plusPriceObj.optString("current_price"));
				plusPriceBuyList.add(plusPriceBuyEntity);
			}
			setPlusPriceBuyList(plusPriceBuyList);
		}
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getRetIds() {
		return retIds;
	}

	public void setRetIds(String retIds) {
		this.retIds = retIds;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public int getDbId() {
		return dbId;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}
	public final static String STATUS_ORDER_0="0";//普通课程
	public final static String STATUS_ORDER_1="1";//待付款
	public final static String STATUS_ORDER_2="2";//待评价
	public final static String STATUS_ORDER_3="3";//隐藏状态
	private String orderSn;
	private String orderId;
	private String orderStatus="0";
	private String orderTime="";
	private String coupon="";
	private String orderSum;
	private String payFree;

	public String getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(String orderSum) {
		this.orderSum = orderSum;
	}

	public String getPayFree() {
		return payFree;
	}

	public void setPayFree(String payFree) {
		this.payFree = payFree;
	}

	private List<WaitingPayEntity> waitingPayList;

	public List<WaitingPayEntity> getWaitingPayList() {
		return waitingPayList;
	}

	public void setWaitingPayList(List<WaitingPayEntity> waitingPayList) {
		this.waitingPayList = waitingPayList;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	// 课程列表页Course数据解析(待付款\待评价\全部订单)
	public void optUserInfoJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setCardType(BaseCardEntity.CARD_TYPE_COURSE_PAY);
		setOrderSn(jsonObj.optString("order_sn"));
		setOrderStatus(jsonObj.optString("order_status"));
		setPriceMax(jsonObj.optString("order_price"));
		setOrderId(jsonObj.optString("order_id"));
		setOrderTime(jsonObj.optString("order_time"));
		setCoupon(jsonObj.optString("is_bonus"));
		setPriceReturn(jsonObj.optString("max_gwk"));
		JSONArray goodList=jsonObj.optJSONArray("goods_list");
		if(goodList!=null&&goodList.length()>0){
			JSONObject itemObj=goodList.optJSONObject(0);
			setCourseName(itemObj.optString("goods_name"));
			setId(itemObj.optString("goods_id"));
			setIcon(itemObj.optString("goods_thumb"));
			setAddress(itemObj.optString("school_name"));
		}
	}
	// 课程列表页Course数据解析(待付款\全部订单)
	public void optUserInfoJsonObj02(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setCardType(BaseCardEntity.CARD_TYPE_WAITING_PAY);
		setOrderSn(jsonObj.optString("order_sn"));
		setOrderStatus(jsonObj.optString("order_status"));
		setPriceMax(jsonObj.optString("order_price"));
		setOrderId(jsonObj.optString("order_id"));
		setOrderTime(jsonObj.optString("order_time"));
		setCoupon(jsonObj.optString("is_bonus"));
		setPriceReturn(jsonObj.optString("max_gwk"));
		setOrderSum(jsonObj.optString("goods_sum"));
		setLogo(jsonObj.optString("brand_logo"));
		setBrandName(jsonObj.optString("brand_name"));
		setPayFree(jsonObj.optString("pay_free"));
		JSONArray goodList=jsonObj.optJSONArray("goods_list");
		if(goodList!=null&&goodList.length()>0){
			List<WaitingPayEntity> list = new ArrayList<>();
			for(int i = 0;i<goodList.length();i++){
				JSONObject itemObj=goodList.optJSONObject(i);
				WaitingPayEntity entity = new WaitingPayEntity(itemObj);
				list.add(entity);
			}
			setWaitingPayList(list);
		}
	}
	// 课程列表页Course数据解析(预约)
//	"goods_id": "476",
//	"goods_name": "聚能教育畅学无忧精品套课",
//	"max_gwk": "100.00",
//	"shop_price": "1998.00",
//	"thumb": "http://182.92.7.222/images/201512/thumb_img/476_thumb_G_1450188710313.jpg",
//	"xiaoqu_name": "聚能教育集团总部",
//	"xiaoqu_addr": "北京市海淀区中关村大街1号海龙大厦9层",
//	"add_time": "2016-03-02"
	public void optUserInfoBookJsonObj(JSONObject jsonObj) {
		if (jsonObj == null)
			return;
		setId(jsonObj.optString("goods_id"));
		setIcon(jsonObj.optString("thumb"));
		setCourseName(jsonObj.optString("goods_name"));
		setPriceMax(jsonObj.optString("shop_price"));
		setPriceReturn(jsonObj.optString("max_gwk"));
		setAddress(jsonObj.optString("xiaoqu_addr"));
		setKeyStr(jsonObj.optString("key_str"));
		setTel(jsonObj.optString("tel"));
	}
	public void optUserInfoWaitPayJsonObj(JSONObject jsonObj) {
		 
		if (jsonObj == null)
			return;
		setOrderSn(jsonObj.optString("order_sn"));
		setOrderStatus(jsonObj.optString("order_status"));
		setPriceMax(jsonObj.optString("order_price"));
		setOrderId(jsonObj.optString("order_id"));
		setOrderTime(jsonObj.optString("order_time"));
		setCourseName(jsonObj.optString("is_bonus"));
		setPriceReturn(jsonObj.optString("max_gwk"));
		JSONArray goodList=jsonObj.optJSONArray("goods_list");
		if(goodList!=null&&goodList.length()>0){
			JSONObject itemObj=goodList.optJSONObject(0);
			setCourseName(itemObj.optString("goods_name"));
			setId(itemObj.optString("goods_id"));
			setIcon(itemObj.optString("goods_thumb"));
			setAddress(itemObj.optString("school_name"));
		}
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public ArrayList<String> getDescImgList() {
		return descImgList;
	}

	public void setDescImgList(ArrayList<String> descImgList) {
		this.descImgList = descImgList;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public ShareEntity getShareEntity() {
		return shareEntity;
	}

	public void setShareEntity(ShareEntity shareEntity) {
		this.shareEntity = shareEntity;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayFQ() {
		return payFQ;
	}

	public void setPayFQ(String payFQ) {
		this.payFQ = payFQ;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	

	public List<FQEntity> getPayFQList() {
		return payFQList;
	}

	public void setPayFQList(List<FQEntity> payFQList) {
		this.payFQList = payFQList;
	}

	 
}
