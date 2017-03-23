package com.dev.nutclass.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ConversionCodeEntity;
import com.dev.nutclass.fragment.BaseFragment;
import com.dev.nutclass.fragment.BrandFragment;
import com.dev.nutclass.fragment.CircleFragment;
import com.dev.nutclass.fragment.EduMapFragment;
import com.dev.nutclass.fragment.HomeFragmentNew;
import com.dev.nutclass.fragment.MeFragment;
import com.dev.nutclass.fragment.ShoppingFragment;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.HomeNavView;
import com.dev.nutclass.view.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

public class HomeActivity extends BaseActivity implements View.OnClickListener{
	private Context context;
	private final static String TAG = "HomeActivity";

	private FrameLayout container;
	private HomeFragmentNew homeFragment;// 首页
//	private CategoryFragment categoryFragment;// 分类页
//	private CommunityFragment communityFragment; // 社区
	private CircleFragment cricleFragment; // 社区
	private BrandFragment brandFragment; // 品牌馆
	private ShoppingFragment shoppingFragment; // 购物车
	private MeFragment meFragment; // 个人页
	private EduMapFragment eduMapFragment;//教育圈地图
	private HomeNavView navView;

	private BaseFragment curFragment;
 
	private TitleBar titleBar;
 	private RelativeLayout homeTittle;
	private int versionCode;
	private String versionName;

	private String appJump;
	private String h5Jump;
	private String appKey;
	private String appValue;

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		context = this;
		clearCache();
		initView();
		inNextActivity();
		initData();
		initIntent();
		initListener();
		Util.initAppData(context);
		setOnGestureBackEnable(false);

		
	}

	private void inNextActivity() {
		if(getIntent().hasExtra("next_activity") && getIntent().getStringExtra("next_activity").equals("1")){
//			Intent intent = new Intent(context,NewestActionActivity.class);
			String jumpType = getIntent().getStringExtra("jump_type");
			if(jumpType!=null && jumpType.equals("h5_jump")){
				Intent intent = new Intent(context,WebViewActivity.class);
				intent.putExtra(Const.KEY_URL,getIntent().getStringExtra(Const.KEY_URL));
				startActivity(intent);
			}else if (jumpType!=null && jumpType.equals("app_jump")){
				String jumpKey = getIntent().getStringExtra("jump_key");
				String jumpValue = getIntent().getStringExtra("jump_value");
				if(jumpKey.equals("brand_id")){
					Intent	intent = new Intent(context,CourseListActivity.class);
					intent.putExtra(Const.KEY_ID, jumpValue);
					intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BRAND);
					startActivity(intent);
				}else if(jumpKey.equals("xiaoqu_id")){
					Intent	intent = new Intent(context,CourseListActivity.class);
					intent.putExtra(Const.KEY_ID,  jumpValue);
					intent.putExtra(Const.KEY_TYPE, CourseListActivity.TYPE_FROM_COURSE);
					startActivity(intent);
				}else if(jumpKey.equals("goods_id")){
					Intent	intent = new Intent(context,CourseInfoActivity.class);
					intent.putExtra(Const.KEY_ID, jumpValue);
					intent.putExtra(Const.KEY_SCHOOL_ID,0);
					startActivity(intent);
				}else if (jumpKey.equals("cuxiao_id")){
					int id = Integer.parseInt(getIntent().getStringExtra("id"));
					Log.d("===","id:"+id);
					if(id > 0){
//						if(id==24){
							Intent intent = new Intent(context, NewestActionActivity02.class);
							intent.putExtra("id",getIntent().getStringExtra("id"));
							context.startActivity(intent);
//						}else {
//							Intent intent = new Intent(context, NewestActionActivity.class);
//							intent.putExtra("id",getIntent().getStringExtra("id"));
//							context.startActivity(intent);
//						}
					}
//					Intent	intent= new Intent(context, NewestActionActivity02.class);
//					intent.putExtra("id",getIntent().getStringExtra("id"));
//					Log.d("===","id:"+getIntent().getStringExtra("id"));
//					startActivity(intent);
				}
			}

		}
	}


	private void initIntent() {
		if(getIntent().hasExtra("app_jump")){
			appJump = getIntent().getStringExtra("app_jump");
		}
		if(getIntent().hasExtra("h5_jump")){
			h5Jump = getIntent().getStringExtra("h5_jump");
		}
		if(getIntent().hasExtra("app_jump_key")){
			appKey = getIntent().getStringExtra("app_jump_key");
		}
		if(getIntent().hasExtra("app_jump_value")){
			appValue = getIntent().getStringExtra("app_jump_value");
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}


	private void initView() {
		titleBar=(TitleBar)findViewById(R.id.tb_title);
		navView = (HomeNavView) findViewById(R.id.view_nav);
		container = (FrameLayout) findViewById(R.id.container);
		titleBar.setVisibility(View.GONE);
	}

	private void initListener() {
 
		navView.setOnBarTabClickListener(new HomeNavView.OnTabClickListener() {

			@Override
			public boolean onTabClick(int pos) {
				// TODO Auto-generated method stub
				if(pos>=3){
					if (Util.checkLogin(context)) {
						switchFragment(pos);
					}else{
						return false;
					}
				}else{
					switchFragment(pos);
				}
				return true;
			}
		});
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {

			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
				if(curFragment==homeFragment){
					Intent intent = new Intent(context, SearchActivity.class);
					startActivity(intent);
				}
//				else if(curFragment==cricleFragment){
//					if(Util.checkLogin(context)){
//						Intent intent = new Intent(context, CircleReleaseActivity.class);
//						startActivity(intent);
//					}
//				}
				
				return false;
			}

			@Override
			public boolean onClickMiddle() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickLeft() {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	private void initData() {

		container.removeAllViews();
		homeFragment = new HomeFragmentNew();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.container, homeFragment);
		curFragment = homeFragment;
		transaction.commit();
		navView.setSelected(0);
//		检查是否需要更新
		checkIsUpdate();

	}


	private void checkIsUpdate() {
		//获取本应用的versionCode
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
			versionCode = info.versionCode;
			versionName = info.versionName;
			if(!SharedPrefUtil.getInstance().getVersionName().equals(versionName)){
				SharedPrefUtil.getInstance().setVersionName(versionName);
			}
			SharedPrefUtil.getInstance().getMobile();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		String url = UrlConst.CHECK_APP_UPDATA_URL;
		OkHttpClientManager.getAsynNoAdd(url, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {

			}

			@Override
			public void onResponse(String response) {
				{
					LogUtil.d(TAG, "response=" + response);
					Gson gson = new Gson();
					//该conversionCodeEntity为通用验证entity;
					ConversionCodeEntity conversionCodeEntity = gson.fromJson(response, ConversionCodeEntity.class);
					if(conversionCodeEntity!=null){
						String code = conversionCodeEntity.getData();
						int codeInt = Integer.parseInt(code);
						if(codeInt > versionCode){
							final AlertDialog.Builder  builder=new AlertDialog.Builder(context);
							builder.setCancelable(false);
							builder.setTitle("新版本上线啦，快去更新！");
							builder.setMessage(conversionCodeEntity.getInfo());
							builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									Uri uri = Uri.parse("market://details?id="+ "com.dev.nutclass");
									Intent intent = new Intent(Intent.ACTION_VIEW, uri);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							});
							builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							});
							builder.show();
						}else{
							Log.d("===","版本号相同无需更新");
							return;
						}
					}
				}
			}

		});
	}


	public void switchFragment(int to) {
		LogUtil.i(TAG, "switchFragment to:" + to);
		titleBar.setVisibility(View.VISIBLE);
		titleBar.removeTitleLeft();
		titleBar.removeTitleRight1();
		navView.setVisibility(View.VISIBLE);
		switch (to) {
		case 0:
			if (homeFragment == null) {
				homeFragment = new HomeFragmentNew();
			}
//			titleBar.setTitleRight1(TitleBar.TYPE_SEARCH_IMG);
//			titleBar.setMiddleText("");
			titleBar.setVisibility(View.GONE);
//			titleBar.setTitleLeft(TitleBar.TYPE_LEFT_ICON_IMG);
			switchContent(homeFragment);
			break;
//		case 1: // 动态
//			if (categoryFragment == null) {
//				categoryFragment = new CategoryFragment();
//			}
//			 
//			switchContent(categoryFragment);
//			break;
		
		case 1:
			if (brandFragment == null) {
				brandFragment = new BrandFragment();
			}
			titleBar.setMiddleText("品牌馆");
			switchContent(brandFragment);
			break;
		case 2:

			if(eduMapFragment == null){
				eduMapFragment = EduMapFragment.newInstance();
				eduMapFragment.setHomeNavView(navView);
			}
			titleBar.setVisibility(View.GONE);
			titleBar.setMiddleText("教育圈");
			switchContent(eduMapFragment);
//			if (cricleFragment == null) {
//				//cricleFragment = new CircleFragment(navView);
//				cricleFragment=CircleFragment.newInstance();
//				cricleFragment.setHomeNavView(navView);
//			}
//			titleBar.setVisibility(View.VISIBLE);
//			titleBar.setTitleRight1(TitleBar.TYPE_RELEASE_IMG);
//			titleBar.setMiddleText("圈子");
//			switchContent(cricleFragment);
			break;
		case 3:
			titleBar.setVisibility(View.GONE);
			if (shoppingFragment == null) {
				shoppingFragment = new ShoppingFragment();
			}
			titleBar.setMiddleText("购物车");
			switchContent(shoppingFragment);
			break;
		case 4:
			if (meFragment == null) {
				meFragment = new MeFragment();
			}
			titleBar.removeTitleRight1();
			titleBar.setMiddleText("我的");
			switchContent(meFragment);
			break;
		default:
			break;
		}
	}

	/**
	 * fragment 切换
	 * **/
	public void switchContent(BaseFragment to) {
		if (curFragment != to) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			if (!to.isAdded()) { //
				transaction.hide(curFragment).add(R.id.container, to).commit(); //
//				if(to==communityFragment){
//					communityFragment.onRefresh(false);
//				}
			} else {
				transaction.hide(curFragment).show(to).commit(); //
//				if(to==homeFragment){
//					homeFragment.updateLocation();
//				}
//				else if(to==categoryFragment){
//					categoryFragment.updateLocation();
//				}else if(to==shoppingFragment){
//					curFragment.onRefresh(false);
//				}
				if(to==shoppingFragment){
					shoppingFragment.onRefresh(false);
				}else if (to == meFragment){
					meFragment.onRefresh(false);
				}
			}
			curFragment = to;
		} else {
			// 双击同一个Tab，刷新Fragment数据
			if (curFragment != null) {
				curFragment.onRefresh(false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	/**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
//        // Check if the key event was the Back button and if there's history
//        if ((keyCode == KeyEvent.KEYCODE_BACK) &&curFragment==communityFragment){
//            // 返回键退回
//        	if(communityFragment.back()){
//        		return true;
//        	}
//        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
	public void changeFragment(){
		switchFragment(4);
		navView.setSelected(4);
	}
	private void clearCache() {
		long curTime = System.currentTimeMillis();
		if(SharedPrefUtil.getInstance().isFirst()){
			SharedPrefUtil.getInstance().setKeepClearCacheTime(curTime);
			SharedPrefUtil.getInstance().setFirst();
			Log.d("===","curtime"+curTime);
		}else{
			Log.d("===","time"+SharedPrefUtil.getInstance().getKeepClearCacheTime());
			if(curTime - SharedPrefUtil.getInstance().getKeepClearCacheTime()>10800000L){
				SharedPrefUtil.getInstance().setKeepClearCacheTime(curTime);
				ImageLoader.getInstance().clearMemoryCache();
				ImageLoader.getInstance().clearDiskCache();
			}
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
