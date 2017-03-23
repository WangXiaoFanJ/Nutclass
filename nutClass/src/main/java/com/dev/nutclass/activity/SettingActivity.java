package com.dev.nutclass.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.StorageUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "SettingActivity";
	private Context context;
	private LinearLayout userLayout;
	private RoundedImageView avaterIv;
	private TextView nameTv;
	private LinearLayout suggestLayout;
	private LinearLayout aboutLayout;
	private LinearLayout exitLayout;
	private UserEntity entity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		context = SettingActivity.this;
		// TODO Auto-generated method stub
		initView();
		initIntent();
		initData();
		initListener();
	}

	private void initView() {
		nameTv = (TextView) findViewById(R.id.tv_name);
		avaterIv = (RoundedImageView) findViewById(R.id.iv_icon);
		userLayout = (LinearLayout) findViewById(R.id.ll_userinfo);
		suggestLayout = (LinearLayout) findViewById(R.id.ll_suggest);
		aboutLayout = (LinearLayout) findViewById(R.id.ll_about);
		exitLayout = (LinearLayout) findViewById(R.id.ll_exit);

	}

	private void initIntent() {

	}

	private void initData() {
		entity = SharedPrefUtil.getInstance().getSession();
		nameTv.setText(entity.getName());
		ImageLoader.getInstance().displayImage(
				StorageUtil.getPid2Url(entity.getPortrait()), avaterIv,
				ImgConfig.getPortraitOption());
	}

	private void initListener() {
		userLayout.setOnClickListener(this);
		suggestLayout.setOnClickListener(this);
		aboutLayout.setOnClickListener(this);
		exitLayout.setOnClickListener(this);
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (v == userLayout) {
//			intent.setClass(context, AddUserInfoActivity.class);
//			intent.putExtra(Const.KEY_ENTITY, entity);
//			startActivity(intent);
		} else if (v == suggestLayout) {
			intent.setClass(context, SuggestActivity.class);
			startActivity(intent);
		} else if (v == aboutLayout) {
			intent.setClass(context, AboutActivity.class);
			startActivity(intent);
		} else if (v == exitLayout) {
			// 退出账号

		}
	}

}
