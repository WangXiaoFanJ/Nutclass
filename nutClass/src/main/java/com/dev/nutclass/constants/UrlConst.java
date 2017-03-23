package com.dev.nutclass.constants;

public class UrlConst {

	// NutClass
	// public static final String BASE_NET_HOST = "http://www.nutclass.com/";
	// public static final String BASE_HOST_URL =
	// "http://www.nutclass.com/service.php?act=";
	public static final String BASE_NET_HOST = "http://182.92.7.222/";
	public static final String BASE_HOST_URL_NEW = "http://new.kobiko.cn/Api/Appapi/";
	//v1
//	public static final String BASE_HOST_URL = "http://182.92.7.222/service.php?act=";
	//v2
//	public static final String BASE_HOST_URL = "http://gxd.kobiko.cn/service_v2.php?act=";
	public static final String BASE_HOST_URL = "http://new.kobiko.cn/api/appapi/";
	// 首页列表KO
	public static final String HOME_URL = BASE_HOST_URL + "get_index_data_list";
	// 获取经纬度
	public static final String GET_POSITION_URL = BASE_HOST_URL
			+ "get_gps_for_addre&address=%s";
	// 品牌馆KO
	public static final String BRANDS_URL = BASE_HOST_URL + "get_hot_brands";
	// 品牌馆详情页KO
	public static final String BRANDS_DETAIL_URL = BASE_HOST_URL
			+ "get_detail_for_brand&brand_id=%s";

	// 0元抢KO
	public static final String ZERO_URL = BASE_HOST_URL + "get_zeroActivity";
	// 判断是否可以抽奖
	public static final String ZERO_CAN_PRAISE_URL = BASE_HOST_URL
			+ "hasGotBouds&zeroActivityId=%s";
	// 抽奖
	public static final String ZERO_PRAISE_URL = BASE_HOST_URL
			+ "get_ToGotBouds&zeroActivityId=%s&phone=%s";
	// 分享成功以后
	public static final String ZERO_SHARE_BACK_URL = BASE_HOST_URL
			+ "get_add_GotBouds&zeroActivityId=%s";

	// 分类KO
	public static final String CATEGORY_URL = BASE_HOST_URL
			+ "get_product_cat_list";
	// 分类BannerKO
	public static final String CATEGORY_BANNER_URL = BASE_HOST_URL
			+ "get_product_banner_list";
	// 分类子类KO
	public static final String CATEGORY_CHILD_URL = BASE_HOST_URL
			+ "get_product_cat_list&cid=";
	// 产品列表KO
	public static final String PRODUCT_LIST_URL = BASE_HOST_URL
			+ "get_product_list";

	// 产品详情页KO
	public static final String COURSE_DETAIL_URL = BASE_HOST_URL
			+ "get_product_info&pid=%s&xiaoqu_id=%s&ver=%s";
	// public static final String COURSE_DETAIL_URL
	// ="http://www.nutclass.com/service.php?act=get_product_info&pid=273";
	// 产品详情页评论
	public static final String COURSE_DETAIL_COMMENT_URL = BASE_HOST_URL
			+ "get_product_commentlist&pid=";
	// 产品订单接口（产品ID）-（校区ID）-（属性ID）-（购物卡ID jd 1 一号店2 唯品会 3 苏宁4），生成订单号下单
	public static final String COURSE_ORDER_URL = BASE_HOST_URL
			+ "get_order_no&order_list=%s&pay_id=%d&mobile_type=2&bonus_id=%s&pay_fq=%s";
	// 产品订单接口 通过订单号获取订单信息
	// http://182.92.7.222/service.php?act=get_order_info&order_id=929&userId=ii&token=666666
	public static final String COURSE_ORDER_INFO_BY_ORDERID_URL = BASE_HOST_URL
			+ "get_order_info&order_id=%s";
	// 产品订单接口 按照订单号下单
	// http://182.92.7.222/service.php?act=order_to_buy&pay_id=3&order_id=929&userId=ii&token=666666
	public static final String COURSE_ORDER_BY_ORDERID_URL = BASE_HOST_URL
			+ "order_to_buy&order_id=%s&pay_id=%d&pay_fq=%s&mobile_type=2";
	// 产品详情页预约试听
	// http://182.92.7.222/service.php?act=get_goods_reservation&userId=11&token=666666&pid=273&school_id=163
	public static final String COURSE_BOOK_URL = BASE_HOST_URL
			+ "get_goods_reservation?&pid=%s&school_id=%s&mobile_phone=%s&consignee=%s";
	// 产品详情页点评
	// http://182.92.7.222/service.php?act=product_comment_add&userId=11&token=666666&pid=273&content=%E8%AF%BE%E7%A8%8B%E4%BE%BF%E5%AE%9C%E4%B8%8D%E5%B0%91%EF%BC%8C%E5%A5%BD
	public static final String COURSE_COMMENT_URL = BASE_HOST_URL
			+ "product_comment_add&pid=%s&content=%s";
	/**
	 * 活动相关接口KO
	 * */
	// activityAreaId（区域的id）
	// topLevel（1 是从高到底 ）
	// pageNo （ 1 开始-n）
	// 最新活动KO
	public static final String HOT_URL = BASE_HOST_URL + "get_hot_activity";
	// 活动详情KO
	// http://www.nutclass.com/service.php?act=get_activity_headerDetail&activityId=6
	public static final String ACTION_DETAIL_URL = BASE_HOST_URL
			+ "get_activity_headerDetail&activityId=%s";
	//
	// 活动评论列表KO：
	// http://www.nutclass.com/service.php?act=get_activity_CommentDetail&activityId=6&pageNo=1
	public static final String ACTION_DETAIL_COMMENT_LIST_URL = BASE_HOST_URL
			+ "get_activity_CommentDetail&activityId=%s";

	//
	// 点赞：
	// path：activity_to_Praise
	// 上传参数：
	// activityId
	// userId
	// 下行参数
	// 成功/失败/未登录/已经赞过了
	// http://www.nutclass.com/service.php?act=activity_to_Praise&userId=2800&activityId=6
	public static final String ACTION_DETAIL_LIKE_URL = BASE_HOST_URL
			+ "activity_to_Praise&activityId=%s";
	//
	// 活动评论：
	// http://www.nutclass.com/service.php?act=get_activity_toComment&activityId=6&comment=jasdfnsdf&userId=2800&token=asdfasdfasdfdfgdfgfgf
	public static final String ACTION_DETAIL_COMMENT_URL = BASE_HOST_URL
			+ "get_activity_toComment&activityId=%s&commnet=%s";
	// 活动报名：
	// http://182.92.7.222/service.php?act=go_activity_enroll&activityId=10&userId=11&token=666666
	public static final String ACTION_DETAIL_ATTEND_URL = BASE_HOST_URL
			+ "go_activity_enroll&activityId=%s";
	/**
	 * 跳蚤市场
	 * */
	// 跳蚤市场分类
	// http://182.92.7.222/service.php?act=get_maket_cat_list
	public static final String MARKET_CATEGORY_URL = BASE_HOST_URL
			+ "get_maket_cat_list";
	// 跳蚤市场
	public static final String MARKET_URL = BASE_HOST_URL
			+ "get_market_data_list";
	// 跳蚤市场详情页头部
	// http://www.nutclass.com/service.php?id=118&userId=11&token=666666&act=get_market_data_for_id
	public static final String MARKET_INFO_URL = BASE_HOST_URL
			+ "get_market_data_for_id&id=%s";
	// 跳蚤市场详情页评论
	// http://www.nutclass.com/service.php?marketId=11&pageNo=1&userId=11&token=666666&act=get_market_comment_list
	public static final String MARKET_COMMENT_LIST_URL = BASE_HOST_URL
			+ "get_market_comment_list&marketId=%s";
	// 跳蚤市场评论
	// http://www.nutclass.com/service.php?token=666666&userId=11&act=get_market_comment_add&marketId=11&content=%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95
	public static final String MARKET_COMMENT_URL = BASE_HOST_URL
			+ "get_market_comment_add&marketId=%s&content=%s";
	// 跳蚤市场发布
	// http://www.nutclass.com/service.php?token=666666&userId=11&act=get_market_comment_add&marketId=11&content=%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95
	public static final String MARKET_RELEASE_URL = BASE_HOST_URL
			+ "get_market_data_up";
	// 搜索页面
	public static final String SEARCH_URL = BASE_HOST_URL
			+ "get_search_list?keys=%s";

	/**
	 * 我的信息
	 * */
	// 获取个人信息
	public static final String USERINFO_URL = BASE_HOST_URL
			+ "get_user_info_for_id";

	// 提交个人信息修改（POST）
	public static final String USERINFO_EDIT_URL = BASE_HOST_URL
			+ "get_user_info_edit";
	// 全部订单
	public static final String USERINFO_ALL_ORDER_URL = BASE_HOST_URL
			+ "myCouresBuyOrder";
	// 待付款pay_type (1 待支付 2待评价 )
	public static final String USERINFO_WAIT_PAY_URL = BASE_HOST_URL
			+ "myCouresBuyOrder&pay_type=1";

	// 预约试听
	public static final String USERINFO_BOOK_URL = BASE_HOST_URL
			+ "get_user_reservation";
	// 待评价
	public static final String USERINFO_WAIT_COMMENT_URL = BASE_HOST_URL
			+ "myCouresBuyOrder&pay_type=2";
	// http://www.nutclass.com/service.php?token=666666&userId=11&act=product_comment_add&content=asd%20fsdf%20sdf&order_sn=2016011812024
	public static final String USERINFO_COMMENT_URL = BASE_HOST_URL
			+ "product_comment_add&order_sn=%s&content=%s";

	// 0元抢
	public static final String USERINFO_ZERO_URL = BASE_HOST_URL
			+ "get_zero_list";
	// 优惠券
	public static final String USERINFO_YOUHUI_URL = BASE_HOST_URL
			+ "get_user_coupon_for_id";
	// 我的发布
	public static final String USERINFO_RELEASE_URL = BASE_HOST_URL
			+ "get_maket_list_for_user";
	// 修改个人发布的状态
	// http://182.92.7.222/service.php?act=change_maket_status&userId=2833&token=cf460ea12940869713320a0cb52b7dbe&maket_id=11&status=1
	public static final String USERINFO_MODIFY_MARKET_STATUS = BASE_HOST_URL
			+ "change_market_status&market_id=%s&status=%s";
	/**
	 * image1 图片KEY nick_name 昵称 sex 性别 1 男 2女 Birthday 生日 1900-01-01 District
	 * 地址 北京 Babystatus 宝宝状态 0无宝宝 1备孕 2已孕 已出生
	 * 
	 * */

	/**
	 * 登陆注册
	 * */
	// 获取验证码KO
	public static final String GET_VERIFY_CODE = BASE_HOST_URL
			+ "get_mobile_captcha&mobile=%s";
	// 完成注册KO
	public static final String REGISTER_URL = BASE_HOST_URL
			+ "user_register&username=%s&mobile=%s&captcha=%s&device_token=%s&ver=%s&mobile_type=2";
	// 登陆KO
	public static final String LOGIN_URL = BASE_HOST_URL_NEW
			+ "user_login_v2&username=%s&captcha=%s&device_token=%s&ver=%s&mobile_type=2";
	// 三方登陆
	public static final String LOGIN_THIRDPART_URL = BASE_HOST_URL
			+ "get_other_info?&login_type=%d&openid=%s&nickname=%s&headimgurl=%s&sex=%d&device_token=%s&mobile_type=2";
	// 修改密码
	public static final String MODIFY_PWD_URL = BASE_HOST_URL
			+ "get_user_editor_password&new_password=%s";

	// 用户个人信息页KO
	public static final String USER_INFO_URL = BASE_HOST_URL
			+ "get_user_info_detail";
	// 用户个人信息页0元抢列表
	public static final String USER_INFO_ZERO_URL = BASE_HOST_URL
			+ "get_zero_list";

	// userId=11&token=666666
	public static double lon = 116.49702654684;
	public static double lat = 39.8977879699;

	// tel
	public static final String SMS_TEL = "1069";
	public static final int SUCCESS_CODE = 1;
	/**
	 * token 原文 u是uid；m是手机号；t是时间戳；i是设备标识；s是用户状态
	 * */
	public static final String TOKEN_FORMAT = "u=%s&m=%s&t=%s&i=%s&s=%s";
	/**
	 * 设置页面接口
	 * */
	// 意见反馈
	public static final String SETTING_FEEDBACK = BASE_HOST_URL
			+ "feedbacks/update?";

	/**
	 * 本地缓存
	 * */
	// 缓存标签
	public static final String GET_CATEGORY_CACHE = BASE_HOST_URL
			+ "category/all?";
	// 缓存标签
	public static final String GET_CITY_CACHE = BASE_HOST_URL
			+ "area/recommend?";
	
	// v2
//	public static final String GET_HOME=BASE_HOST_URL+"get_index_data_list";
	
	//v3
//	public static final String GET_HOME=BASE_HOST_URL+"get_index_data2&device_token=%s&pageNo=%d";
	public static final String GET_HOME=BASE_HOST_URL+"get_index_data2?device_token=%s&pageNo=%d&ver=%s";
	//校区列表
	public static final String GET_SCHOOL=BASE_HOST_URL+"get_school_list&cat_id=%s&ver=%s";
	//课程列表http://182.92.7.222/service_v2.php?act=get_product_list_for_school_id?school_id=3
	public static final String GET_COURSE=BASE_HOST_URL+"get_product_list&school_id=%s&ver=%s";
	public static final String GET_YIYUAN=BASE_HOST_URL+"get_product_list&id=%s";
	public static final String GET_RESET=BASE_HOST_URL+"find_pwd_submit&mobile=%s&captcha=%s&password=%s";
	

	//artical，用户列表，我的列表user_info=other_uid,区分某个用户列表还是总列表（KO）
	public static final String GET_ARTICAL_LIST=BASE_HOST_URL_NEW+"get_community_article";
	public static final String GET_ARTICAL_LIST_BY_ID=BASE_HOST_URL+"get_community_article&user_info_id=%s";
	public static final String GET_ARTICAL_DETAIL=BASE_HOST_URL+"get_community_article&a_id=%s";
	
	/**
	 *  userId	int	用户ID	必须
		token	string	用户token	必须
		s_id	Int	文章分类ID	非必须
		content	string	文章内容	必须
		article_img		文章配图	

	 * */
	//文章发布KO
	public static final String GET_ARTICAL_RELEASE=BASE_HOST_URL_NEW+"add_shequ_article";

	//文章赞KO
	public static final String GET_ARTICAL_LIKE=BASE_HOST_URL+"click_praise&a_id=%s";
	
	//文章评论
	public static final String GET_ARTICAL_COMMENT=BASE_HOST_URL+"commemts_article&a_id=%s&content=%s";
	
	//文章删除
	public static final String GET_ARTICAL_DELETE=BASE_HOST_URL+"article_delete&a_id=%s";

	//优惠券列表
	public static final String GET_COOPON_LIST=BASE_HOST_URL_NEW+"my_bonus_count_new";
	
	//获取分期列表
	public static final String GET_FQ_LIST=BASE_HOST_URL_NEW+"get_bank_fq?goods_id=%s&attr_id=%s&order_sn=%s";
	//获取分期列表
//	public static final String GET_FQ_LIST="http://new.kobiko.cn/Api/Appapi/get_bank_fq?&attr_id=%s&goods_id=%s"
//	public static final String COURSE_ORDER_NEW_URL=BASE_HOST_URL_NEW+"get_order_no?order_list=%s&mobile_phone=%s&pay_id=%d&pay_fq=%s&bonus_id=%s&mobile_type=2";
	public static final String COURSE_ORDER_NEW_URL=BASE_HOST_URL_NEW+"get_order_no?plus_price_buy=%s&order_list=%s&mobile_phone=%s&pay_id=%d&ver=%s&pay_fq=%s&mobile_type=2";
	//获取京东分类
	public static final String KJD_CAT_URL=BASE_HOST_URL_NEW+"jd_kepler_list?gcate=%s";

	//获取京东分类
	public static final String SEARCH_HOT_URL=BASE_HOST_URL_NEW+"get_search_key_list";
	//获取促销活动
	public static final String NEWEST_ACTION_URL=BASE_HOST_URL_NEW+"promotion_list?id=";
	//验证兑换码是否成功
	public static final String CONVERSION_CODE_URL = BASE_HOST_URL_NEW+"bind_redeem_code/redeem_code/%s";
	//删除待付款订单
	public static final String ORDER_REMOVE_URL = BASE_HOST_URL_NEW+"order_remove?order_id=%s";
	//课程详情Web页面
	public static final String COURSE_DETAIL_WEB_URL = "http://new.kobiko.cn/sys/goods/index/goods_id/";
	//商家订单列表
	public static final String MERCHANT_ORDER_YES_URL =BASE_HOST_URL_NEW+ "user_order_is_compny?is_confirm=1";
	public static final String MERCHANT_ORDER_NO_URL = BASE_HOST_URL_NEW+"user_order_is_compny?is_confirm=0";
	//商家一元购列表
	public static final String MERCHANT_ONE_YUAN_YES_URL = BASE_HOST_URL_NEW+"user_yiyuangou_compny?is_confirm=1";
	public static final String MERCHANT_ONE_YUAN_NO_URL = BASE_HOST_URL_NEW+"user_yiyuangou_compny?is_confirm=0";
	//商家预约列表
	public static final String MERCHANT_APPOITNMENT_YES_URL = BASE_HOST_URL_NEW+"user_reservation_is_compny?is_back=1";
	public static final String MERCHANT_APPOINTMENT_NO_URL = BASE_HOST_URL_NEW+"user_reservation_is_compny?is_back=0";
	//商家后台订单验证列表
	public static final String MERCHANT_ORDER_MAKESURE_URL = BASE_HOST_URL_NEW+"user_confirm_for_xiaoqu?user_code=%s&order_id=%s";
	//商家后台一元购验证接口
	public static final String MERCHANT_ONE_YUAN_MAKESURE_URL = BASE_HOST_URL_NEW+"user_confirm_for_xiaoqu_new?&order_id=%s";
	//商家后台预约验证接口
	public static final String MERCHANT_APPOINTMENT_MAKESURE_URL = BASE_HOST_URL_NEW+"user_confirm_for_xiaoqu_yuyue?&order_id=%s";
	//检测更新App版本
	public static final String CHECK_APP_UPDATA_URL = BASE_HOST_URL_NEW+"return_android_version";
	//启动页广告接口
	public static final String SPLASH_AD_URL = BASE_HOST_URL_NEW+"loadingAd";
	//新版品牌馆接口
	public static final String BRANDS_URL_NEW = BASE_HOST_URL_NEW+"get_hot_brands2";
	//请求获取优惠券接口
	public static final String REQ_BONUS_RUL = BASE_HOST_URL_NEW+"new_send_bonus_to_user?pid=%s&goods_id=%s";
	//订单支付页面接口
//	public static final String REQ_ORDER_INFO_URL = BASE_HOST_URL_NEW+"getUserOrderDetail?token=%s&userId=%s&orderInfo=%s";

	public static final String REQ_ORDER_INFO_URL = BASE_HOST_URL_NEW+"getUserOrderDetail2";
//添加加价购收货地址接口
	public static final String REQ_ADD_ADDRESS_URL = BASE_HOST_URL_NEW+"submit_address?userId=%s&token=%s&address=%s";
//地图接口
	public static  final String REQ_EDU_MAP_URL = BASE_HOST_URL_NEW+"get_school_list_poi?userId=%s&longitude=%s&latitude=%s&device_token=%s&token=%s&mobile_phone=%s&cat_id=%s";

}
