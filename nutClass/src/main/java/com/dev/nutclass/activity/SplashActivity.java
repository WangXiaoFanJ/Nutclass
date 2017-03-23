package com.dev.nutclass.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ADSplashEntity;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends BaseActivity {
	private static final String TAG = "SplashActivity";
	private Context context;
	private ImageView imageView;
	private TextView textView;
	private ADSplashEntity entity;
	JSONObject jsonObject;
	String status;
	String data;
	private TextView timerTv;
	private LinearLayout skipLayout;
	private  MyCount count;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1){
//				if(SharedPrefUtil.getInstance().isFirst()){
//					Intent intent=new Intent(context,HomeActivity.class);
//					startActivity(intent);
//					finish();
//				}else{
				if(Util.isNetworkAvailable(context)){
					requestUrl();
				}else{
					Log.d("===","当前网络不可用");
					Intent intent=new Intent(context,HomeActivity.class);
					startActivity(intent);
					finish();
				}

			}
//			else{
//				Intent intent=new Intent(context,HomeActivity.class);
//				startActivity(intent);
//				finish();
//			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=SplashActivity.this;
		setContentView(R.layout.activity_splash);
		Util.location(getApplication(),null);
		imageView = (ImageView) findViewById(R.id.iv_splash_bg);
		timerTv = (TextView) findViewById(R.id.tv_count_timer);
		skipLayout = (LinearLayout) findViewById(R.id.ll_skip);
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				handler.sendEmptyMessageDelayed(1,2000);
//				handler.sendEmptyMessageDelayed(0, 4000);
			}
		}.start();
	}
	private void requestUrl() {
		String url = UrlConst.SPLASH_AD_URL;
		OkHttpClientManager.getAsynNoAdd(url, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				LogUtil.d(TAG, "error e=" + e.getMessage());
				Intent intent=new Intent(context,HomeActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onResponse(String response) {
				try {
					jsonObject= new JSONObject(response);
					if(jsonObject!=null){
						status= jsonObject.optString("status");
						String info = jsonObject.optString("info");
						data= jsonObject.optString("data");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(TextUtil.isNotNull(status)&&status.equals("1")){
					Gson gson = new Gson();
					entity= gson.fromJson(data,ADSplashEntity.class);
					String img = entity.getImg();
					Picasso.with(context).load(img).into(imageView);
					imageView.setVisibility(View.VISIBLE);
					count= new MyCount(5 * 1000, 1000); // 1分钟（60秒）倒计时
					count.start();
					imageView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent=new Intent(context,HomeActivity.class);
							intent.putExtra(Const.IN_NEXT_ACTIVITY,"1");
							if(entity.getH5_jump().equals("1")){
								intent.putExtra("jump_type","h5_jump");
								intent.putExtra(Const.KEY_URL, entity.getH5_url());
							}
							if(entity.getApp_jump().equals("1")){
								intent.putExtra("jump_type","app_jump");
								intent.putExtra("jump_key",entity.getApp_jump_key());
								intent.putExtra("jump_value",entity.getApp_jump_value());
								if(entity.getApp_jump_key().equals("brand_id")){

									intent.putExtra(Const.KEY_ID, entity.getApp_jump_value());
									intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BRAND);
								}else if(entity.getApp_jump_key().equals("xiaoqu_id")){

									intent.putExtra(Const.KEY_ID,  entity.getApp_jump_value());
									intent.putExtra(Const.KEY_TYPE, CourseListActivity.TYPE_FROM_COURSE);
								}else if(entity.getApp_jump_key().equals("goods_id")){

									intent.putExtra(Const.KEY_ID, entity.getApp_jump_value());
									intent.putExtra(Const.KEY_SCHOOL_ID,0);
								}else if (entity.getApp_jump_key().equals("cuxiao_id")){
//									intentAD = new Intent(context, NewestActionActivity.class);
									intent.putExtra("id",entity.getApp_jump_value());
								}
							}
//							Intent[] intents = new Intent[2];
//							intents[0] = intent;
//							intents[1] = intentAD;
							count.cancel();
							startActivity(intent);
//							startActivities(intents);
							finish();
						}
					});
				}else{
					Intent intent=new Intent(context,HomeActivity.class);
					startActivity(intent);
//					handler.removeMessages(0);
					finish();
				}
			}
		});

	}
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			timerTv.setVisibility(View.GONE);
			Intent intent=new Intent(context,HomeActivity.class);
			startActivity(intent);
//					handler.removeMessages(0);
			finish();
//		getVerifyCodeBtn.setEnabled(true);
//		getVerifyCodeBtn.setText(getString(R.string.login_verify_get_again));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			skipLayout.setVisibility(View.VISIBLE);
			skipLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(context,HomeActivity.class);
					startActivity(intent);
//					handler.removeMessages(0);
					count.cancel();
					finish();
				}
			});
//			timerTv.setEnabled(false);
			long second = millisUntilFinished / 1000;
			String tick = second + "";
//			if (second < 10) {
//				tick = "0" + tick;
//			}
			timerTv.setText(tick + "S");
		}
	}
	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub
		
	}
}


