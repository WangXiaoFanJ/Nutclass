package com.dev.nutclass.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dev.nutclass.ApplicationConfig;
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
import com.squareup.okhttp.Request;

public class PhoneLoginActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "PhoneLoginActivity";
	private Context context;
	private TitleBar titleBar;
	private EditText phoneEdit;
	private EditText pwdEdit;
	private TextView loginTv;
	private TextView leftTv;
	private TextView rightTv;
	private TextView registerTv;
	private String uid="";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = PhoneLoginActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_phone_login);
		initView();
		initIntent();
		initListener();
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		phoneEdit = (EditText) findViewById(R.id.edit_phone);
		pwdEdit = (EditText) findViewById(R.id.edit_pwd);
		leftTv = (TextView) findViewById(R.id.tv_left);
		rightTv = (TextView) findViewById(R.id.tv_right);
		loginTv = (TextView) findViewById(R.id.tv_login);
		registerTv = (TextView) findViewById(R.id.tv_register);
		rightTv.setVisibility(View.INVISIBLE);
	}

	private void initIntent() {
		
	}

	private void initListener() {
		loginTv.setOnClickListener(this);
		registerTv.setOnClickListener(this);
		leftTv.setOnClickListener(this);
		rightTv.setOnClickListener(this);
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == loginTv) {
			String mobile = phoneEdit.getText().toString().trim();
			String pwd = pwdEdit.getText().toString().trim();
			if (TextUtil.isNotNull(mobile) && TextUtil.isNotNull(pwd)) {
				if ("".equals(mobile)) { // 判断 帐号和密码
					DialogUtils.showToast(context, "手机号不能为空");
				} else if (!Util.isMobileNO(mobile)) {
					DialogUtils.showToast(context, "手机号错误");
				} else {
					reqLogin(mobile, pwd);
				}
			}
		} else if (v == leftTv) {
			finish();
		} else if (v == rightTv) {
			Intent intent = new Intent();
			intent.setClass(context, ResetPwdActivity.class);
			startActivity(intent);
		} else if (v == registerTv) {
			Intent intent=new Intent();
			intent.setClass(context, RegisterActivity.class);
			startActivityForResult(intent,ThirdLoginActivity.REQ_PHONE_LOGIN);
		}

	}

	private void reqLogin(final String phone, String pwd) {
		SharedPrefUtil.getInstance().setMobile(phoneEdit.getText().toString());
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.LOGIN_URL), phone, pwd);
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
							SharedPrefUtil.getInstance().setMobile(phone);
							DialogUtils.showToast(context, "登陆成功");
							setResult(RESULT_OK);
							finish();
						} else {
							DialogUtils.showToast(context, retObj.getErrorMsg());
						}
					}
				});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==ThirdLoginActivity.REQ_PHONE_LOGIN&&resultCode==RESULT_OK){
			Intent intent=new Intent();
			intent.setPackage(ApplicationConfig.getInstance().getPackageName());
			intent.setAction(Const.ACTION_BROADCAST_LOGIN_SUCC);
			sendBroadcast(intent);
			finish();
		}
	}

}
