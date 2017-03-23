package com.dev.nutclass.entity;

import java.io.Serializable;

public class BaseCardEntity implements Serializable {
	public static final int CARD_COUNT=10;

/**
cardType=1      首页九宫格
cardType=2      首页Banner
cardType=3      机构Card
cardType=4      学校Card
cardType=5      搜索推荐的机构（待商榷）
 * */
	
	public static final int CARD_TYPE_AD=1;
	public static final int CARD_TYPE_BANNER=2;
	public static final int CARD_TYPE_SCHOOL=3;
	public static final int CARD_TYPE_COURSE=4;
	public static final int CARD_TYPE_SCHOOL_REC=5;
	public static final int CARD_TYPE_COMMENT=6;
	
	
	
	
	public static final int CARD_TYPE_LIST=7;
	public static final int CARD_TYPE_SINGLE_ITEM=8;
	public static final int CARD_TYPE_DOUBLE_ITEM=9;
	public static final int CARD_TYPE_USER=10;
	public static final int CARD_TYPE_FEED_TWO=11;
	
	
	public static final int CARD_TYPE_ACTION=101;
	public static final int CARD_TYPE_CATEGORY=102;
	public static final int CARD_TYPE_AD1=103;
	public static final int CARD_TYPE_AD2=104;
	public static final int CARD_TYPE_SIMPLE_TEXT=105;
	public static final int CARD_TYPE_BRAND=106;
	public static final int CARD_TYPE_COURSE_INFO=107;
	public static final int CARD_TYPE_ACTION_INFO=108;
	public static final int CARD_TYPE_ZERO=109;
	public static final int CARD_TYPE_ZERO_INFO=110;
	public static final int CARD_TYPE_MARKET=111;
	public static final int CARD_TYPE_MARKET_INFO=112;
	
	//从200开始
	public static final int CARD_TYPE_HEAD=200;
	
	//朋友圈
	public static final int CARD_TYPE_FEED=201;
	//卡券
	public static final int CARD_TYPE_COOPON=202;
	//卡券数目
	public static final int CARD_TYPE_COOPON_NUM=203;
	//年龄段
	public static final int CARD_TYPE_AGE=204;
	//运营位第一种三个（左一右二）
	public static final int CARD_TYPE_AD_001=205;
	//运营位第二种三个（带标题以此排开）
	public static final int CARD_TYPE_AD_002=206;
	//运营位第三种四个（全是图片）
	public static final int CARD_TYPE_AD_003=207;
	//运营位第四种四个（文字、图片左右切换）
	public static final int CARD_TYPE_AD_004=208;
	//校区card
	public static final int CARD_TYPE_SCHOOL_LIST=209;
	//运营位多个滚动显示更多
//	public static final int CARD_TYPE_AD_005=210;
	//京东Banner
	public static final int CARD_TYPE_JD_BANNER=210;
	//活动详情Banner
	public static final int CARD_TYPE_ACTION_BANNER=215;
	
	//京东Card
//	public static final int CARD_TYPE_JD_CARD=211;
	
	//jd 双Card
	public static final int CARD_TYPE_JD_DOUBLE_CARD=212;
	
	//jd筛选card
	public static final int CARD_TYPE_JD_CAT_CARD=213;
	
	//待付款、待评价
	public static final int CARD_TYPE_COURSE_PAY=214;
	//新版待付款
	public static final int CARD_TYPE_WAITING_PAY=220;
	
	//商家后台订单
	public static final int CARD_TYPE_MERCHANT_ORDER = 216;
	//商家后台管理预约
	public static final int CARD_TYPE_MERCHANT_APPOITNMENT=217;
	//首页广告Banner
	public static final int CARD_TYPE_BANNER_AD = 300;

	//首页活动弹框
	public static final int CARD_TYPE_AlERT = 222;
	private int cardType=0;

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

}
