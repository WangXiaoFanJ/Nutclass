package com.dev.nutclass.activity;

import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.parser.LoginParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class ThirdLoginActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "ThirdLoginActivity";
	private Context context;
	private TitleBar titleBar;
	private LinearLayout weixinLayout;
	private LinearLayout qqLayout;
	private LinearLayout weiboLayout;
	private TextView registerTv;
	private TextView loginTv;
	private TextView skipTv;

	public static int REQ_PHONE_LOGIN = 100;

	private UMShareAPI mShareAPI = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ThirdLoginActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_third_login);
		initView();
		initIntent();
		initListener();
		Util.initAppData(context);
		api = WXAPIFactory.createWXAPI(this, SnsUtil.WEIXIN_APP_ID);

	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		weixinLayout = (LinearLayout) findViewById(R.id.ll_weixin);
		qqLayout = (LinearLayout) findViewById(R.id.ll_qq);
		weiboLayout = (LinearLayout) findViewById(R.id.ll_weibo);
		registerTv = (TextView) findViewById(R.id.tv_register);
		loginTv = (TextView) findViewById(R.id.tv_login);
		skipTv = (TextView) findViewById(R.id.tv_skip);
	}

	private void initIntent() {

	}

	private void initListener() {
		registerTv.setOnClickListener(this);
		weixinLayout.setOnClickListener(this);
		qqLayout.setOnClickListener(this);
		weiboLayout.setOnClickListener(this);
		registerTv.setOnClickListener(this);
		loginTv.setOnClickListener(this);
		skipTv.setOnClickListener(this);
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == registerTv) {
			Intent intent = new Intent();
			intent.setClass(context, RegisterActivity.class);
			startActivity(intent);
		} else if (v == loginTv) {
			Intent intent = new Intent();
			intent.setClass(context, PhoneLoginActivity.class);
			startActivityForResult(intent, REQ_PHONE_LOGIN);
		} else if (v == skipTv) {
			// Intent intent=new Intent();
			// intent.setClass(context, HomeActivity.class);
			// startActivity(intent);
			finish();

		} else if (v == weixinLayout) {
			thirdLogin(SHARE_MEDIA.WEIXIN);
			DialogUtils.showToast(context, "微信登陆");
		} else if (v == qqLayout) {
			thirdLogin(SHARE_MEDIA.QQ);
			DialogUtils.showToast(context, "QQ登陆");
		} else if (v == weiboLayout) {
			thirdLogin(SHARE_MEDIA.SINA);
			DialogUtils.showToast(context, "微博登陆");
			// reqOrderData(1);
		}
	}

	private IWXAPI api;

	/*
	 * 网络请求数据
	 */
	private void reqOrderData(final int page) {
		String url = "http://182.92.7.222/service.php?act=get_order_no&userId=11&token=666666&order_list=229-149-1066&pay_id=5";

		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);

						SimpleInfoParser parser = new SimpleInfoParser();
						JsonResult<String> result = (JsonResult<String>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							JSONObject jsonData;
							try {
								jsonData = new JSONObject(response);
								JSONObject json = jsonData
										.optJSONObject("data");
								if (null != json) {
									PayReq req = new PayReq();
									// req.appId = "wxf8b4f85f3a794e77"; //
									// 测试用appId
									req.appId = json.optString("appid");
									req.partnerId = json.optString("partnerid");
									req.prepayId = json.optString("prepayid");
									req.nonceStr = json.optString("noncestr");
									req.timeStamp = json.optString("timestamp");
									req.packageValue = json
											.optString("package");
									req.sign = json.optString("sign");
									req.extData = "app data"; // optional
									Toast.makeText(context, "正常调起支付",
											Toast.LENGTH_SHORT).show();
									// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
									api.sendReq(req);
								} else {
									Log.d("PAY_GET",
											"返回错误" + json.getString("retmsg"));
									Toast.makeText(context,
											"返回错误" + json.getString("retmsg"),
											Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						DialogUtils.showToast(context, result.getErrorMsg());
					}
				});

	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			registerTv.setEnabled(true);
			registerTv.setText(getString(R.string.login_verify_get_again));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			registerTv.setEnabled(false);
			long second = millisUntilFinished / 1000;
			String tick = second + "";
			if (second < 10) {
				tick = "0" + tick;
			}
			registerTv.setText(tick + "S");
		}
	}

	// UM三方登陆
	// UMSocialService mController = null;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// UM三方登陆
	private void thirdLogin(SHARE_MEDIA media) {
		mShareAPI = UMShareAPI.get(this);
		mShareAPI.doOauthVerify(this, media, new UMAuthListener() {
			@Override
			public void onComplete(SHARE_MEDIA platform, int action,
					Map<String, String> data) {
				Toast.makeText(getApplicationContext(), "Authorize succeed",
						Toast.LENGTH_SHORT).show();
				// String uid = value.getString("uid");
				// LogUtil.d(TAG, "onComplete uid=" + uid);
				// if (!TextUtils.isEmpty(uid)) {
				// getUserInfo(platform, uid);
				// } else {
				// LogUtil.d(TAG, "授权失败...");
				// }
				getUserInfo(platform);
			}

			@Override
			public void onError(SHARE_MEDIA platform, int action, Throwable t) {
				Toast.makeText(getApplicationContext(), "Authorize fail",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCancel(SHARE_MEDIA platform, int action) {
				Toast.makeText(getApplicationContext(), "Authorize cancel",
						Toast.LENGTH_SHORT).show();
			}
		});

		// mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// mController.doOauthVerify(context, media, new UMAuthListener() {
		//
		// @Override
		// public void onStart(SHARE_MEDIA platform) {
		// LogUtil.d(TAG, "start");
		// }
		//
		// @Override
		// public void onError(SocializeException e, SHARE_MEDIA platform) {
		// LogUtil.d(TAG,"onError"+e.getMessage());
		// }
		//
		// @Override
		// public void onComplete(Bundle value, SHARE_MEDIA platform) {
		// String uid = value.getString("uid");
		// LogUtil.d(TAG, "onComplete uid=" + uid);
		// if (!TextUtils.isEmpty(uid)) {
		// getUserInfo(platform, uid);
		// } else {
		// LogUtil.d(TAG, "授权失败...");
		// }
		// }
		//
		// @Override
		// public void onCancel(SHARE_MEDIA platform) {
		// LogUtil.d(TAG,"onCancel");
		// }
		// });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQ_PHONE_LOGIN && resultCode == RESULT_OK) {
			Intent intent = new Intent();
			intent.setPackage(ApplicationConfig.getInstance().getPackageName());
			intent.setAction(Const.ACTION_BROADCAST_LOGIN_SUCC);
			sendBroadcast(intent);
			finish();
		} else {
			/** 使用SSO授权必须添加如下代码 */
			// if (mController == null) {
			// return;
			// }
			// UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
			// requestCode);
			// if (ssoHandler != null) {
			// ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			// }
			if (mShareAPI != null) {
				mShareAPI.onActivityResult(requestCode, resultCode, data);
			}
		}

	}

	// weixin

	// 获取平台用户信息
	/**
	 * 获取授权平台的用户信息</br>
	 */
	private void getUserInfo(final SHARE_MEDIA platform) {
		mShareAPI.getPlatformInfo(this, platform, new UMAuthListener() {

			@Override
			public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int status,
					Map<String, String> info) {
				// TODO Auto-generated method stub
				LogUtil.d(TAG, "status：" + status);
				if (status == 200 && info != null) {
					

				} 
				if(info!=null){
					StringBuilder sb = new StringBuilder();
					Set<String> keys = info.keySet();
					for (String key : keys) {
						sb.append(key + "=" + info.get(key).toString() + ";");
					}
					sb.append("\r\n");
					LogUtil.d(TAG, sb.toString());
					thirdUser2UserEntity(platform, info);
				}else {
					LogUtil.d(TAG, "发生错误：" + status);
				}
				
			}

			@Override
			public void onCancel(SHARE_MEDIA arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});

		// mController.getPlatformInfo(context, platform, new UMDataListener() {
		//
		// @Override
		// public void onStart() {
		//
		// }
		// @Override
		// public void onComplete(int status, Map<String, Object> info) {
		// if (status == 200 && info != null) {
		// StringBuilder sb = new StringBuilder();
		// Set<String> keys = info.keySet();
		// for (String key : keys) {
		// sb.append(key + "=" + info.get(key).toString() + ";");
		// }
		// sb.append("\r\n");
		// LogUtil.d(TAG, sb.toString());
		// info.put("uid", uid);
		// thirdUser2UserEntity(platform, info);
		//
		// } else {
		// LogUtil.d(TAG, "发生错误：" + status);
		// }
		// }
		// });
	}

	// weibo~~~followers_count=279;profile_image_url=http://tp1.sinaimg.cn/2135837280/180/5604273438/1;description=One
	// Piece;screen_name=世界无限大1987;location=北京
	// 海淀区;access_token=2.005qkX1COKlh9Ed7eef2f350zOsuCC;verified=true;gender=1;uid=2135837280;favourites_count=25;statuses_count=1078;friends_count=490;
	// QQ~~~~is_yellow_vip=0;yellow_vip_level=0;profile_image_url=http://q.qlogo.cn/qqapp/1104840741/6D86F9512813350AF08418752B0BC532/100;screen_name=marcous;msg=;vip=0;city=威海;gender=男;province=山东;level=0;is_yellow_year_vip=0;
	// 三方登陆绑定手机号码页面
	private void thirdUser2UserEntity(SHARE_MEDIA media,
			Map<String, String> info) {
		UserEntity entity = new UserEntity();
		if (media == SHARE_MEDIA.SINA) {
			entity.setType(UserEntity.USER_TYPE_WEIBO);
			entity.setOpenId((String) info.get("uid"));
			entity.setPortrait((String) info.get("profile_image_url"));
			entity.setName((String) info.get("screen_name"));
			if (String.valueOf(info.get("gender")).equals("1")) {// 男
				entity.setSex(1);
			} else {
				entity.setSex(2);
			}
		} else if (media == SHARE_MEDIA.QQ) {
			entity.setType(UserEntity.USER_TYPE_QQ);
			entity.setOpenId((String) info.get("openid"));
			entity.setPortrait((String) info.get("profile_image_url"));
			entity.setName((String) info.get("screen_name"));
			if (((String) info.get("gender")).equals("男")) {
				entity.setSex(1);
			} else if (((String) info.get("gender")).equals("女")) {
				entity.setSex(2);
			}
		} else if (media == SHARE_MEDIA.WEIXIN) {
			// unionid=oB6xXxGwh21QRNpryKE5aWU8-Apw;country=中国;nickname=测试;city=;province=;language=zh_CN;headimgurl=;sex=2;openid=oKz1EuOXpsIkOUMjR0TRWOyB38c8;

			entity.setType(UserEntity.USER_TYPE_WEIXIN);
			entity.setOpenId((String) info.get("openid"));
			entity.setPortrait((String) info.get("headimgurl"));
			entity.setName((String) info.get("nickname"));
			if (info.get("sex").equals("1")) {
				entity.setSex(1);
			} else {
				entity.setSex(2);
			}
		}
		// 判断是否是注册用户
		reqThirdLogin(media, entity, (String) info.get("uid"));
	}

	private void reqThirdLogin(final SHARE_MEDIA platform,
			final UserEntity entity, String key) {
		int type = -1;
		if (platform == SHARE_MEDIA.SINA) {
			type = 1;
		} else if (platform == SHARE_MEDIA.QQ) {
			type = 2;
		} else if (platform == SHARE_MEDIA.WEIXIN) {
			type = 0;
		}
		// 此处参数应该带有用户信息
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.LOGIN_THIRDPART_URL),
				entity.getType(), entity.getOpenId(), entity.getName(),
				entity.getPortrait(), entity.getSex());
		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						LoginParser parser = new LoginParser();
						JsonResult<UserEntity> retObj = (JsonResult<UserEntity>) parser
								.parse(response);
						if (retObj.getErrorCode() == UrlConst.SUCCESS_CODE) {
							// DialogUtils.showToast(context, "登陆成功");
							// Intent intent=new Intent();
							// intent.setPackage(ApplicationConfig.getInstance().getPackageName());
							// intent.setAction(Const.ACTION_BROADCAST_LOGIN_SUCC);
							// sendBroadcast(intent);
							if (TextUtil.isNotNull(SharedPrefUtil.getInstance()
									.getMobile())) {
								DialogUtils.showToast(context, "登陆成功");
								Intent intent = new Intent();
								intent.setPackage(ApplicationConfig
										.getInstance().getPackageName());
								intent.setAction(Const.ACTION_BROADCAST_LOGIN_SUCC);
								sendBroadcast(intent);
							} else {
								Intent intent = new Intent();
								intent.setClass(context, RegisterActivity.class);
								intent.putExtra(Const.KEY_ID, SharedPrefUtil
										.getInstance().getUid());
								startActivityForResult(intent, REQ_PHONE_LOGIN);
								SharedPrefUtil.getInstance().setUserSession("");
								SharedPrefUtil.getInstance().setToken("");
								SharedPrefUtil.getInstance().setMobile("");
							}

							finish();
						} else {
							DialogUtils.showToast(context, retObj.getErrorMsg());
						}
					}

				});
	}

}
