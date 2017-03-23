package com.dev.nutclass.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.dev.nutclass.R;
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
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class SuggestActivity extends BaseActivity {
	private static final String TAG="SuggestActivity";
	private Context context;
	private TitleBar titleBar;
	private EditText suggestEdit;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_suggest);
	    context=SuggestActivity.this;
	    // TODO Auto-generated method stub
	    initView();
	    initListener();
	}
	private void initView(){
		titleBar=(TitleBar)findViewById(R.id.tb_title);
	    suggestEdit=(EditText)findViewById(R.id.edit_suggest);
	    suggestEdit.setSelection(suggestEdit.getText().toString().length());
	}
	private void initListener(){
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {
			
			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
				submitSuggest();
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
	// 验证码核实
	private void submitSuggest() {
		String suggest=suggestEdit.getText().toString();
		if(TextUtils.isEmpty(suggest)){
			DialogUtils.showToast(context, getString(R.string.tips_suggest_not_empty));
			return;
		}
		// 此处参数应该带有用户信息
		String url = HttpUtil.addBaseGetParams(UrlConst.SETTING_FEEDBACK) + "&content="+suggest;
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
						DialogUtils.showToast(context, "提交成功");
						finish();

					}
				});
	}

}
