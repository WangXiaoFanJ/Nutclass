//package com.dev.nutclass;
//
//import android.app.Activity;
//import android.content.Context;
//import android.widget.Toast;
//
//import com.dev.nutclass.entity.ShareEntity;
//import com.dev.nutclass.utils.StorageUtil;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.StatusCode;
//import com.umeng.socialize.handler.SinaSsoHandler;
//import com.umeng.socialize.handler.UMQQSsoHandler;
//import com.umeng.socialize.handler.UMWXHandler;
//import com.umeng.socialize.media.QQShareContent;
//import com.umeng.socialize.media.QZoneShareContent;
//import com.umeng.socialize.media.SinaShareContent;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.WeiXinShareContent;
//
//
//
//public class OldSnsUtil {
//	public static final String QQ_APP_ID = "1104840741";
//	public static final String QQ_APP_KET = "CGxrDaUBSPrl6yLS";
//	public static final String WEIXIN_APP_ID = "wxf4451af60a7f3501";
//	public static final String WEIXIN_APP_KET = "ba438da1dabe212a10f41f25c26692b1";
//	public static final String DESCRIPTOR = "com.umeng.share";
//
//	private static OldSnsUtil instance = null;
//	private static Context context;
//	private final UMSocialService mController = UMServiceFactory
//			.getUMSocialService(DESCRIPTOR);
//	
//	
//	
//	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
//
//	public static OldSnsUtil getInstance(Context c) {
//		context = c;
//		if (instance == null) {
//			instance = new OldSnsUtil();
//		}
//		return instance;
//	}
//
//	public void initSns() {
//		addSinaPlatform();
//		addWXPlatform();
//		addQQQZonePlatform();
//		setShareContent();
//		OldSnsUtil.getInstance(context).getController().getConfig().addFollow(SHARE_MEDIA.SINA, "1");
//		OldSnsUtil.getInstance(context).getController().getConfig().addFollow(SHARE_MEDIA.WEIXIN, "2");
//		OldSnsUtil.getInstance(context).getController().getConfig().addFollow(SHARE_MEDIA.WEIXIN_CIRCLE, "3");
//		OldSnsUtil.getInstance(context).getController().getConfig().addFollow(SHARE_MEDIA.QQ, "4");
//		OldSnsUtil.getInstance(context).getController().getConfig().removePlatform(SHARE_MEDIA.QZONE);
//		OldSnsUtil.getInstance(context).getController().getConfig().removePlatform(SHARE_MEDIA.TENCENT);
//	}
//	public UMSocialService getController(){
//		return mController;
//	}
//
//	/**
//	 * @功能描述 : 添加sina授权
//	 * @return
//	 */
//	private void addSinaPlatform() {
//		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//	}
//	/**
//	 * @功能描述 : 添加微信平台分享
//	 * @return
//	 */
//	private void addWXPlatform() {
//		// 注意：在微信授权的时候，必须传递appSecret
//		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//		// 添加微信平台
//		UMWXHandler wxHandler = new UMWXHandler(context, WEIXIN_APP_ID,
//				WEIXIN_APP_KET);
//		wxHandler.addToSocialSDK();
//
//		// 支持微信朋友圈
//		UMWXHandler wxCircleHandler = new UMWXHandler(context, WEIXIN_APP_ID,
//				WEIXIN_APP_KET);
//		wxCircleHandler.setToCircle(true);
//		wxCircleHandler.addToSocialSDK();
//	}
//
//	/**
//	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
//	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
//	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
//	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
//	 * @return
//	 */
//	private void addQQQZonePlatform() {
//		// 添加QQ支持, 并且设置QQ分享内容的target url
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context,
//				QQ_APP_ID, QQ_APP_KET);
//		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
//		qqSsoHandler.addToSocialSDK();
//
//		// 添加QZone平台
////		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
////				(Activity) context, QQ_APP_ID, QQ_APP_KET);
////		qZoneSsoHandler.addToSocialSDK();
//	}
//
//	/**
//	 * 执行分享
//	 * */
//	public void performShare(SHARE_MEDIA platform,ShareEntity entity) {
//		if(platform==SHARE_MEDIA.WEIXIN){// 微信分享
//			WeiXinShareContent weixinContent = new WeiXinShareContent();
//			UMImage urlImage = new UMImage(context,
//					StorageUtil.getPid2Url(entity.getImg()));
//			weixinContent.setShareImage(urlImage);
//			weixinContent.setTargetUrl(entity.getUrl());
//			switch (entity.getType()) {
//			case ShareEntity.TYPE_FEED:
//				weixinContent
//				.setShareContent(entity.getDesc());
//				break;
//			case ShareEntity.TYPE_USER:
//				weixinContent
//				.setShareContent("我分享了 "+entity.getDesc()+" 的个人主页");
//				
//				break;
//			case ShareEntity.TYPE_HOSPITAL:
//				weixinContent
//				.setShareContent("我在美颜日记找到了【"+entity.getDesc()+"】");
//				break;
//			default:
//				break;
//			}
//			mController.setShareMedia(weixinContent);
//		}else if(platform==SHARE_MEDIA.WEIXIN_CIRCLE){
//			CircleShareContent circleMedia = new CircleShareContent();
//			UMImage urlImage = new UMImage(context,
//					StorageUtil.getPid2Url(entity.getImg()));
//			circleMedia.setShareImage(urlImage);
//			circleMedia.setTargetUrl(entity.getUrl());
//			switch (entity.getType()) {
//			case ShareEntity.TYPE_FEED:
//				circleMedia
//				.setShareContent(entity.getDesc());
//				break;
//			case ShareEntity.TYPE_USER:
//				circleMedia
//				.setShareContent("我分享了 "+entity.getDesc()+" 的个人主页");
//				
//				break;
//			case ShareEntity.TYPE_HOSPITAL:
//				circleMedia
//				.setShareContent("我在美颜日记找到了【"+entity.getDesc()+"】");
//				break;
//			default:
//				break;
//			}
//			mController.setShareMedia(circleMedia);
//		}else if(platform==SHARE_MEDIA.QQ){
//			QQShareContent qqShareContent = new QQShareContent();
//			UMImage urlImage = new UMImage(context,
//					StorageUtil.getPid2Url(entity.getImg()));
//			qqShareContent.setShareImage(urlImage);
//			qqShareContent.setTargetUrl(entity.getUrl());
//			switch (entity.getType()) {
//			case ShareEntity.TYPE_FEED:
//				qqShareContent
//				.setShareContent(entity.getDesc());
//				break;
//			case ShareEntity.TYPE_USER:
//				qqShareContent
//				.setShareContent("我分享了 "+entity.getDesc()+" 的个人主页");
//				
//				break;
//			case ShareEntity.TYPE_HOSPITAL:
//				qqShareContent
//				.setShareContent("我在美颜日记找到了【"+entity.getDesc()+"】");
//				break;
//			default:
//				break;
//			}
//			mController.setShareMedia(qqShareContent);
//		}else if(platform==SHARE_MEDIA.QZONE){
//			QZoneShareContent qzone = new QZoneShareContent();
//			UMImage urlImage = new UMImage(context,
//					StorageUtil.getPid2Url(entity.getImg()));
//			urlImage
//			.setTargetUrl(entity.getUrl());
//			qzone.setShareImage(urlImage);
//			qzone.setTargetUrl(entity.getUrl());
//			switch (entity.getType()) {
//			case ShareEntity.TYPE_FEED:
//				qzone
//				.setShareContent(entity.getDesc());
//				break;
//			case ShareEntity.TYPE_USER:
//				qzone
//				.setShareContent("我分享了 "+entity.getDesc()+" 的个人主页");
//				
//				break;
//			case ShareEntity.TYPE_HOSPITAL:
//				qzone
//				.setShareContent("我在美颜日记找到了【"+entity.getDesc()+"】");
//				break;
//			default:
//				break;
//			}
//			mController.setShareMedia(qzone);
//		}
//		
// 
//		mController.postShare(context, platform, new SnsPostListener() {
//
//			@Override
//			public void onStart() {
//
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,
//					SocializeEntity entity) {
//				String showText = platform.toString();
//				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//					showText += "平台分享成功";
//				} else {
//					showText += "平台分享失败";
//				}
//				Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
//
//	/**
//	 * 根据不同的平台设置不同的分享内容</br>
//	 */
//	private void setShareContent() {
//		
//		String targetUrl="http://www.nutclass.com";
//		String shareContent="课比课，孩子学习的助力";
//		String title="课比课";
//		mController
//				.setShareContent("课比课，孩子学习的助力");
////http://www.nutclass.com
//		/**
//		 * 分享内容的格式定义
//		 * */
////		UMImage fileImage = new UMImage(context, BitmapFactory.decodeResource(
////				context.getResources(), R.drawable.ic_launcher));
////		UMImage urlImage = new UMImage(context,
////				"http://www.umeng.com/images/pic/social/integrated_3.png");
//		UMImage resImage = new UMImage(context, R.drawable.icon_share_nut);
//
//
//
//		// 微信分享
//		WeiXinShareContent weixinContent = new WeiXinShareContent();
//		weixinContent
//				.setShareContent(shareContent);
//		weixinContent.setTitle(title);
//		weixinContent.setTargetUrl(targetUrl);
//		weixinContent.setShareMedia(resImage);
//		mController.setShareMedia(weixinContent);
//
//		// 设置朋友圈分享的内容
//		CircleShareContent circleMedia = new CircleShareContent();
//		circleMedia
//				.setShareContent(shareContent);
//		circleMedia.setTitle(title);
//		circleMedia.setShareMedia(resImage);
//		circleMedia.setTargetUrl(targetUrl);
//		mController.setShareMedia(circleMedia);
//
//		 
//		resImage
//				.setTargetUrl(targetUrl);
//
//		// 设置QQ空间分享内容
//		QZoneShareContent qzone = new QZoneShareContent();
//		qzone.setShareContent(shareContent);
//		qzone.setTargetUrl(targetUrl);
//		qzone.setTitle(title);
//		qzone.setShareMedia(resImage);
//		mController.setShareMedia(qzone);
//
//		QQShareContent qqShareContent = new QQShareContent();
//		qqShareContent.setShareContent(shareContent);
//		qqShareContent.setTitle(title);
//		qqShareContent.setShareMedia(resImage);
//		qqShareContent.setTargetUrl(targetUrl);
//		mController.setShareMedia(qqShareContent);
//
//		SinaShareContent sinaContent = new SinaShareContent();
//		sinaContent
//				.setShareContent(shareContent);
//		sinaContent.setShareImage(resImage);
//		mController.setShareMedia(sinaContent);
//
//	}
//    /**
//     * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
//     */
//	public void directShare(ShareEntity entity) {
//		SinaShareContent sinaContent = new SinaShareContent(null);
//		UMImage urlImage = new UMImage(context,
//				StorageUtil.getPid2Url(entity.getImg()));
//		sinaContent.setShareImage(urlImage);
//		sinaContent.setTargetUrl(entity.getUrl());
//		switch (entity.getType()) {
//		case ShareEntity.TYPE_FEED:
//			sinaContent
//			.setShareContent(entity.getDesc());
//			
//			break;
//		case ShareEntity.TYPE_USER:
//			sinaContent
//			.setShareContent("我分享了 "+entity.getDesc()+" 的个人主页");
//			
//			break;
//		case ShareEntity.TYPE_HOSPITAL:
//			sinaContent
//			.setShareContent("我在美颜日记找到了【"+entity.getDesc()+"】");
//			break;
//		default:
//			break;
//		}
//		mController.setShareMedia(sinaContent);
//        mController.directShare(context, mPlatform, new SnsPostListener() {
//
//            @Override
//            public void onStart() {
//            }
//
//            @Override
//            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//                String showText = "分享成功";
//                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
//                    showText = "分享失败 [" + eCode + "]";
//                }
//                Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//}
