package com.dev.nutclass.activity;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.RegisterActivity.MyCount;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.parser.SimpleInfoParser;
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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class ModifyPwdActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "ModifyPwdActivity";
	private Context context;
	private TitleBar titleBar;
	;
	private EditText oldPwdEdit;
	private EditText pwdEdit;
	private EditText pwdAgainEdit;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ModifyPwdActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_modify_pwd);
		initView();
		initIntent();
		initListener();
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		oldPwdEdit = (EditText) findViewById(R.id.edit_old_pwd);
		pwdEdit = (EditText) findViewById(R.id.edit_pwd);
		pwdAgainEdit = (EditText) findViewById(R.id.edit_pwd_again);
		 
	}

	private void initIntent() {

	}

	private void initListener() {
		
		titleBar.setBarClickListener(new BarClickListener() {

			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
//				String oldPwd = oldPwdEdit.getText().toString();
				String pwd = pwdEdit.getText().toString();
				String pwdAgain=pwdAgainEdit.getText().toString();
//				TextUtil.isNotNull(oldPwd) && TextUtil.isNotNull(pwd)
				if (TextUtil.isNotNull(pwd)&& TextUtil.isNotNull(pwdAgain)) {
					if(pwd.equals(pwdAgain)){
						reqResetPwd(pwd);
					}else{
						DialogUtils.showToast(context, "两次密码输入不一致");
					}
				} else {
					DialogUtils.showToast(context, "输入数据有错误");
				}
				return true;
			}

			@Override
			public boolean onClickMiddle() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickLeft() {
				// TODO Auto-generated method stub
				return false;
			}
		});

	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	



	private void reqResetPwd( String pwd) {
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.MODIFY_PWD_URL), pwd);
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
//							DialogUtils.showToast(context, "感谢您参与本次活动，请及时关注活动动态");
							DialogUtils.showToast(context,"修改密码成功");
							finish();
						}else{
							DialogUtils.showToast(context, "修改失败，请核对后输入");
						}
					}
				});
	}

}
