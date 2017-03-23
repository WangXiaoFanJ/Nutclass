package com.dev.nutclass.constants;


public interface Const {
	//KeyName
	public static final String KEY_UID="type_uid";
	public static final String KEY_ID="type_id";
	public static final String KEY_SCHOOL_ID="school_id";
	public static final String KEY_POSITION="type_position";
	public static final String KEY_CONTENT="type_content";
	public static final String KEY_MODE="key_mode";
	public static final String KEY_TYPE="key_type";
	public static final String KEY_ENTITY="key_entity";
	public static final String KEY_ENTITY_LIST="key_entity_list";
	public static final String KEY_IMAGE_PATH="key_image_path";
	public static final String KEY_LON="key_lon";
	public static final String KEY_LAT_PATH="key_lat";
	public static final String KEY_URL="key_url";
	public static final String KEY_TITLE_H5="key_title_h5";
	public static final String KEY_CONTEXT_H5="key_context_h5";
	public static final String KEY_IMAGE_H5 = "key_image_h5";
	public static final String KEY_URL_H5="key_url_h5";
	public static final String KEY_TITLE="key_title";
	public static final String KEY_FROM = "key_from";
	public static final String KEY_AGE="key_age";
	public static final String KEY_KEYWORD="key_keyword";
	public  static final String DEVICE_TOKEN = "device_token";
	public static final String KEY_PROMOTION = "is_promotion";
	public  static final String KEY_TYPE_MERCHANT = "key_type_merchant";
	public static final String IN_NEXT_ACTIVITY= "next_activity";
	public static final String KEY_ORDER_INFO = "orderInfo";
	public static final String KEY_PLUS_PRICE_BUY = "plus_price_buy";
	//Action
	//login success
	public static final String ACTION_BROADCAST_LOGIN_SUCC="action_broadcat_login_succ";
	//update userinfo
	public static final String ACTION_BROADCAST_USERINFO_CHANGE="action_broadcat_userinfo_succ";
	//feed change
	public static final String ACTION_BROADCAST_FEED_CHANGED="action_broadcat_feed_change";
	//feed create
	public static final String ACTION_BROADCAST_FEED_CREATE="action_broadcat_feed_create";
	//message receive
	public static final String ACTION_BROADCAST_RECEIVE_MESSAGE="action_broadcat_receive_message";
	//feed release
	public static final String ACTION_BROADCAST_FEED_RELEASE_CHANGED="action_broadcat_feed_release_change";
	//feed delete
	
	//FEED_CHANGE
	public static final int TYPE_FEED_CHANGE_RELEASE=1;//release
	public static final int TYPE_FEED_CHANGE_DELETE=2;//delete
	public static final int TYPE_FEED_CHANGE_SUCCESS=3;//release success
	public static final int TYPE_FEED_CHANGE_FAILTURE=4;//release failure
	public static final int TYPE_FEED_CHANGE_UPDATE=5;//update
	//判断商家后台是否验证
	public static final int TYPE_MERCHANT_BTN_LEFT = 0;
	public static final int TYPE_MERCHANT_BTN_RIGHT=1;

	//商家后台类型
	public static final int TYPE_FROM_MERCHANT_APPOINTMENT = 1007;
	public static final int TYPE_FROM_MERCHANT_ORDER = 1008;
	public static final int TYPE_FROM_MERCHANT_ONE_YUAN = 1009;
	
	
	
	
}
