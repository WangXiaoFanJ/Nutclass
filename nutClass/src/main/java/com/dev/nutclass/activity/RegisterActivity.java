package com.dev.nutclass.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.NutClassApplication;
import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.parser.LoginParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.dev.nutclass.view.TitleBar.BarClickListener;
import com.squareup.okhttp.Request;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "RegisterActivity";
	private Context context;
	private TitleBar titleBar;
	private EditText phoneEdit;
	private EditText verifyCodeEdit;
	private TextView getVerifyCodeBtn;
	private EditText pwdEdit;
	private String uid;
	private TextView getVerifyCodeSoundBtn;
	private boolean sending=false;
	private TextView registerTv;
	private ImageView backIv;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = RegisterActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_register);
		initView();
		initIntent();
		initListener();
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		phoneEdit = (EditText) findViewById(R.id.edit_phone);
		verifyCodeEdit = (EditText) findViewById(R.id.edit_verifycode);
		getVerifyCodeBtn = (TextView) findViewById(R.id.tv_get_verifycode);
		getVerifyCodeSoundBtn = (TextView) findViewById(R.id.tv_get_verifycode_sound);
		registerTv = (TextView) findViewById(R.id.tv_register_user);
		backIv = (ImageView) findViewById(R.id.callback_iv);
	}

	private void initIntent() {
		if(getIntent().hasExtra(Const.KEY_ID)){
			uid=getIntent().getStringExtra(Const.KEY_ID);
		}
	}

	private void initListener() {
		getVerifyCodeBtn.setOnClickListener(this);
		getVerifyCodeSoundBtn.setOnClickListener(this);
		registerTv.setOnClickListener(this);
		backIv.setOnClickListener(this);
//		titleBar.setBarClickListener(new BarClickListener() {
//
//			@Override
//			public boolean onClickRight2() {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public boolean onClickRight1() {
//				// TODO Auto-generated method stub
//				String phone = phoneEdit.getText().toString();
//				String verifyCode = verifyCodeEdit.getText().toString();
//				String pwd = pwdEdit.getText().toString();
//				if(TextUtil.isNotNull(uid)){
//					if (TextUtil.isNotNull(phone) && TextUtil.isNotNull(verifyCode)
//							) {
//						reqRegister(phone, verifyCode, pwd);
//					} else {
//						DialogUtils.showToast(context, "输入数据有错误");
//					}
//				}else{
//					if (TextUtil.isNotNull(phone) && TextUtil.isNotNull(verifyCode)&& TextUtil.isNotNull(pwd)) {
//						reqRegister(phone, verifyCode, pwd);
//					} else {
//						DialogUtils.showToast(context, "输入数据有错误");
//					}
//				}
//				return true;
//			}
//
//			@Override
//			public boolean onClickMiddle() {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public boolean onClickLeft() {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});

	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == getVerifyCodeBtn) {
			String mobile = phoneEdit.getText().toString().trim();
			if ("".equals(mobile)) { // 判断 帐号和密码
				DialogUtils.showToast(context, "手机号不能为空");
			} else if (!Util.isMobileNO(mobile)) {
				DialogUtils.showToast(context, "手机号错误");
			} else {
				// TODO:请求验证码
				reqVerifyCode(mobile);
				MyCount count = new MyCount(60 * 1000, 1000); // 1分钟（60秒）倒计时
				count.start();
			}
		} else if(v==getVerifyCodeSoundBtn){
			String mobile = phoneEdit.getText().toString().trim();
			if ("".equals(mobile)) { // 判断 帐号和密码
				DialogUtils.showToast(context, "手机号不能为空");
			} else if (!Util.isMobileNO(mobile)) {
				DialogUtils.showToast(context, "手机号错误");
			} else {
				// TODO:请求验证码
				if(sending){
					DialogUtils.showToast(context, "正在发送，请稍后");
					return;
				}
				sending=true;
				reqVerifyCode(mobile,true);

			}
		}else if(v == registerTv){
			String phone = phoneEdit.getText().toString();
			String verifyCode = verifyCodeEdit.getText().toString();
			if(TextUtil.isNotNull(uid)){
				if (TextUtil.isNotNull(phone) && TextUtil.isNotNull(verifyCode)
						) {
					registerTv.setBackgroundResource(R.drawable.shape_rigester_gray);
					registerTv.setEnabled(false);
					reqRegister(phone, verifyCode);
				} else {
					DialogUtils.showToast(context, "输入数据有错误");
				}
			}else{
				if (TextUtil.isNotNull(phone) && TextUtil.isNotNull(verifyCode)) {
					registerTv.setBackgroundResource(R.drawable.shape_rigester_gray);
					registerTv.setEnabled(false);
					reqRegister(phone, verifyCode);
				} else {
					DialogUtils.showToast(context, "输入数据有错误");
				}
			}
		}else if(v == backIv){
			finish();
		}
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			getVerifyCodeBtn.setEnabled(true);
			getVerifyCodeBtn
					.setText(getString(R.string.login_verify_get_again));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			getVerifyCodeBtn.setEnabled(false);
			long second = millisUntilFinished / 1000;
			String tick = second + "";
			if (second < 10) {
				tick = "0" + tick;
			}
			getVerifyCodeBtn.setText(tick + "S");
		}
	}

	// 验证码请求
	private void reqVerifyCode(String mobile,boolean isSound) {
		String url="";
		if(isSound){
			url = String.format(
					HttpUtil.addBaseGetParams(UrlConst.GET_VERIFY_CODE), mobile);
			url+="&toCall=1";
		}else{
			url = String.format(
					HttpUtil.addBaseGetParams(UrlConst.GET_VERIFY_CODE), mobile);
		}
		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
						sending=false;
					}
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						DialogUtils.showToast(context, "发送成功");
						sending=false;
					}
				});
	}
	// 验证码请求
	private void reqVerifyCode(String mobile) {
		reqVerifyCode(mobile,false);
	}

	private void reqRegister(final String phone, String verifyCode) {
		SharedPrefUtil.getInstance().setMobile(phoneEdit.getText().toString());
		String device_token = NutClassApplication.device_token;
		String versionName = SharedPrefUtil.getInstance().getVersionName();
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.REGISTER_URL),
				phone,phone, verifyCode,device_token,versionName);
		if(TextUtil.isNotNull(uid)){
			url=url+"&user_id="+uid;
		}
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
						Log.d("===","errorCode:"+retObj.getErrorCode());
						if (retObj.getErrorCode() == UrlConst.SUCCESS_CODE) {
							setResult(RESULT_OK);
							DialogUtils.showToast(context, "注册成功");
							Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
							startActivity(intent);
							finish();
						} else {
							DialogUtils.showToast(context, retObj.getErrorMsg());
							registerTv.setEnabled(true);
							registerTv.setBackgroundResource(R.drawable.shape_login_btn);
						}
					}
				});
	}

}
