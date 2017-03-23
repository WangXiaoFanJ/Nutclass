package com.dev.nutclass.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.activity.CardListActivity;
import com.dev.nutclass.activity.CooponListActivity;
import com.dev.nutclass.activity.EditUserInfoActivity;
import com.dev.nutclass.activity.FeedUserPageActivity;
import com.dev.nutclass.activity.MerchantInfoActivity;
import com.dev.nutclass.activity.ModifyPwdActivity;
import com.dev.nutclass.activity.NewestActionActivity;
import com.dev.nutclass.activity.NewestActionActivity02;
import com.dev.nutclass.activity.RegisterActivity;
import com.dev.nutclass.activity.SuggestActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.ConversionCodeEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.ShareEntity;
import com.dev.nutclass.entity.SingnalEvent;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.parser.MeParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.DiscountCouponView;
import com.dev.nutclass.view.RoundedImageView;
import com.google.gson.Gson;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import java.util.List;

import de.greenrobot.event.EventBus;

public class MeFragment extends BaseFragment implements View.OnClickListener {
	private static final String TAG = "MeFragment";

	private Context context;

	private RoundedImageView headIv;
	private TextView nameTv;
	private TextView nameEdit;
	private LinearLayout allOrderTv;
	private RelativeLayout order1Layout;
	private LinearLayout order2Layout;
	private LinearLayout order3Layout;
	private LinearLayout order4Layout;

	private LinearLayout money1Layout;
	private LinearLayout money2Layout;
	private LinearLayout money3Layout;
	private LinearLayout money4Layout;

	private LinearLayout suggestLayout;
	private LinearLayout shareLayout;
	private LinearLayout releaseLayout;
	private LinearLayout cooponLayout;
	private TextView loginTv;

	private TextView order1Tv;
	private TextView order2Tv;
	private TextView order3Tv;
	private TextView order4Tv;

	private TextView money1Tv;
	private TextView money2Tv;
	private TextView money3Tv;
	private TextView money4Tv;
	private TextView orderCountTv;
	private TextView discountCouponTv;
	private AlertDialog mAlertDialog;
	private LinearLayout merchantLayout;
	private LinearLayout getMerchant01Layout;
	private LinearLayout getMerchant02Layout;
	private LinearLayout getMerchant03Layout;
	private LinearLayout getMerchant04Layout;

	public MeFragment() {
		// TODO Auto-generated constructor stub
		LogUtil.d(TAG, "MeFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		EventBus.getDefault().register(this);
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_me, null);
		headIv = (RoundedImageView) view.findViewById(R.id.iv_icon_new);
		nameTv = (TextView) view.findViewById(R.id.tv_name);
		nameEdit = (TextView) view.findViewById(R.id.tv_modify);

		allOrderTv = (LinearLayout) view.findViewById(R.id.ll_all_order);
		order1Layout = (RelativeLayout) view.findViewById(R.id.ll_order1);
		order2Layout = (LinearLayout) view.findViewById(R.id.ll_order2);
		order3Layout = (LinearLayout) view.findViewById(R.id.ll_order3);
		order4Layout = (LinearLayout) view.findViewById(R.id.ll_order4);

		money1Layout = (LinearLayout) view.findViewById(R.id.ll_money1);
		money2Layout = (LinearLayout) view.findViewById(R.id.ll_money2);
		money3Layout = (LinearLayout) view.findViewById(R.id.ll_money3);
		money4Layout = (LinearLayout) view.findViewById(R.id.ll_money4);

		suggestLayout = (LinearLayout) view.findViewById(R.id.ll_suggest);
		shareLayout = (LinearLayout) view.findViewById(R.id.ll_share);
		releaseLayout = (LinearLayout) view.findViewById(R.id.ll_release);
		cooponLayout = (LinearLayout) view.findViewById(R.id.ll_coopon);

		loginTv = (TextView) view.findViewById(R.id.tv_login);

		orderCountTv = (TextView) view.findViewById(R.id.tv_order_count);
		order1Tv = (TextView) view.findViewById(R.id.tv_order1);
		order2Tv = (TextView) view.findViewById(R.id.tv_order2);
		order3Tv = (TextView) view.findViewById(R.id.tv_order3);
		order4Tv = (TextView) view.findViewById(R.id.tv_order4);

		money1Tv = (TextView) view.findViewById(R.id.tv_money1);
		money2Tv = (TextView) view.findViewById(R.id.tv_money2);
		money3Tv = (TextView) view.findViewById(R.id.tv_money3);
		money4Tv = (TextView) view.findViewById(R.id.tv_money4);

		merchantLayout = (LinearLayout) view.findViewById(R.id.ll_merchant);
		getMerchant01Layout = (LinearLayout) view.findViewById(R.id.ll_merchant_01);
		getMerchant02Layout = (LinearLayout) view.findViewById(R.id.ll_merchant_02);
		getMerchant03Layout = (LinearLayout) view.findViewById(R.id.ll_merchant_03);
		getMerchant04Layout = (LinearLayout) view.findViewById(R.id.ll_merchant_04);
		discountCouponTv = (TextView) view.findViewById(R.id.tv_discount_coupon);

		getMerchant01Layout.setOnClickListener(this);
		getMerchant02Layout.setOnClickListener(this);
		getMerchant03Layout.setOnClickListener(this);
		getMerchant04Layout.setOnClickListener(this);
		nameEdit.setOnClickListener(this);
		allOrderTv.setOnClickListener(this);
		order1Layout.setOnClickListener(this);
		order2Layout.setOnClickListener(this);
		order3Layout.setOnClickListener(this);
		order4Layout.setOnClickListener(this);
		money1Layout.setOnClickListener(this);
		money2Layout.setOnClickListener(this);
		money3Layout.setOnClickListener(this);
		money4Layout.setOnClickListener(this);
		suggestLayout.setOnClickListener(this);
		releaseLayout.setOnClickListener(this);
		cooponLayout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		loginTv.setOnClickListener(this);
		discountCouponTv.setOnClickListener(this);
		if (SharedPrefUtil.getInstance().isLogin()) {
			reqData(1);

		} else {
			DialogUtils.showToast(context, "您尚未登录");
		}
		if (SharedPrefUtil.getInstance().isLogin()) {
			loginTv.setText("退出登录");
		} else {
			loginTv.setText("登录");

		}
		// 定义广播
		String[] filters = { Const.ACTION_BROADCAST_LOGIN_SUCC };
		registerReceiver(filters);
		return view;
	}

	public void update(UserEntity entity) {
		ImageLoader.getInstance().displayImage(entity.getPortrait(), headIv,
				ImgConfig.getAlbumImgOption());
//		order1Tv.setText(entity.getOrder1() + "");
//		order2Tv.setText(entity.getOrder2() + "");
//		order3Tv.setText(entity.getOrder3() + "");
//		order4Tv.setText(entity.getOrder4() + "");
		Log.d("===","orderCount"+entity.getOrder1());
		if(entity.getOrder1()>0){
			orderCountTv.setVisibility(View.VISIBLE);
			orderCountTv.setText(entity.getOrder1()+"");
		}else{
			orderCountTv.setVisibility(View.GONE);
		}

		money1Tv.setText(entity.getMoney1() + "");
		money2Tv.setText(entity.getMoney2() + "");
		money3Tv.setText(entity.getMoney3() + "");
		money4Tv.setText(entity.getMoney4() + "");

		nameTv.setText(entity.getName() + "");
		if (SharedPrefUtil.getInstance().isLogin()) {
			loginTv.setText("退出登录");
		} else {
			loginTv.setText("登录");

		}

	}

	@Override
	public void onRefresh(boolean b) {
		// TODO Auto-generated method stub
		super.onRefresh(b);
		if (SharedPrefUtil.getInstance().isLogin()) {
			reqData(1);
		} else {
			DialogUtils.showToast(context, "您尚未登录");
		}
	}

	/*
	 * 网络请求数据
	 */
	private void reqData(final int page) {

		String url = String.format(UrlConst.USER_INFO_URL);
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
						MeParser parser = new MeParser();
						JsonResult<UserEntity> result = (JsonResult<UserEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							UserEntity entity= result.getRetObj();
							update(entity);
							if(entity.getIs_compny().equals("1")){
								merchantLayout.setVisibility(View.VISIBLE);
							}else if (entity.getIs_compny().equals("0")){
								merchantLayout.setVisibility(View.GONE);
							}
						}else{
							UserEntity entity=new UserEntity();
							entity.setName("未登录");
							entity.setPortrait("");
							entity.setMoney1(0);
							entity.setMoney2(0);
							entity.setMoney3(0);
							entity.setMoney4(0);
							update(entity);
							
						}
					}
				});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (Util.checkLogin(context)) {
			if (v == loginTv) {
				Util.loginOut(context);
				reqData(1);
			} else if (v == nameEdit) {
				Intent intent = new Intent();
				intent.setClass(context, EditUserInfoActivity.class);
				startActivity(intent);
			} else if (v == allOrderTv) {
				Intent intent = new Intent();
				intent.setClass(context, CardListActivity.class);
				intent.putExtra(Const.KEY_TYPE,
						CardListActivity.TYPE_FROM_ALL_ORDER);
				startActivity(intent);
			} else if (v == order1Layout) {
				Intent intent = new Intent();
				intent.setClass(context, CardListActivity.class);
				intent.putExtra(Const.KEY_TYPE,
						CardListActivity.TYPE_FROM_WAIT_PAY);
				startActivity(intent);
			} else if (v == order2Layout) {
				Intent intent = new Intent();
				intent.setClass(context, CardListActivity.class);
				intent.putExtra(Const.KEY_TYPE, CardListActivity.TYPE_FROM_BOOK);
				startActivity(intent);
			} else if (v == order3Layout) {
				Intent intent = new Intent();
				intent.setClass(context, CardListActivity.class);
				intent.putExtra(Const.KEY_TYPE,
						CardListActivity.TYPE_FROM_WAIT_COMMENT);
				startActivity(intent);
			} else if (v == order4Layout) {
//				Intent intent = new Intent();
//				intent.setClass(context, CardListActivity.class);
//				intent.putExtra(Const.KEY_TYPE, CardListActivity.TYPE_FROM_ZERO);
//				startActivity(intent);
				DialogUtils.showToast(context, "暂未开放");
			} else if (v == money2Layout) {
				// 优惠券
				if(Util.checkLogin(context)){
					Intent intent = new Intent(context, CooponListActivity.class);
					context.startActivity(intent);
				}
			} else if (v == money4Layout) {
				// 我的发布,同跳蚤市场
//				Intent intent = new Intent();
//				intent.setClass(context, CardListActivity.class);
//				intent.putExtra(Const.KEY_TYPE, CardListActivity.TYPE_FROM_MARKET);
//				startActivity(intent);
				DialogUtils.showToast(context, "暂未开放");
			} else if (v == suggestLayout) {
//				Intent intent = new Intent();
//				intent.setClass(context, NewestActionActivity02.class);
//				startActivity(intent);
				Intent intent = new Intent();
				intent.setClass(context, SuggestActivity.class);
				startActivity(intent);
			} else if (v == shareLayout) {
//				SnsUtil.getInstance(context).getController()
//						.openShare((Activity) context, false);
				ShareEntity entity=new ShareEntity();
				entity.setDesc("中国教育课程折扣平台");
				entity.setTitle("课比课");
				entity.setImg("http://new.kobiko.cn/Public/img/logo.png");
				entity.setUrl("http://new.kobiko.cn/Html5/Index");
				SnsUtil.getInstance(context).openShare((Activity) context,entity);
			} else if (v == releaseLayout) {
				if(Util.checkLogin(context)){
					Intent intent = new Intent(context, FeedUserPageActivity.class);
					intent.putExtra(Const.KEY_TITLE, "我");
					intent.putExtra(Const.KEY_UID, SharedPrefUtil.getInstance().getUid());
					context.startActivity(intent);
				}
				
			}else if(v ==getMerchant01Layout){
				Intent intent = new Intent(context, MerchantInfoActivity.class);
				intent.putExtra(Const.KEY_TYPE_MERCHANT,Const.TYPE_FROM_MERCHANT_APPOINTMENT);
				startActivity(intent);
			}else if(v == getMerchant02Layout){
				Intent intent = new Intent(context,MerchantInfoActivity.class);
				intent.putExtra(Const.KEY_TYPE_MERCHANT,Const.TYPE_FROM_MERCHANT_ORDER);
				startActivity(intent);
			}else if(v == getMerchant03Layout){
				Intent intent = new Intent(context,MerchantInfoActivity.class);
				intent.putExtra(Const.KEY_TYPE_MERCHANT,Const.TYPE_FROM_MERCHANT_ONE_YUAN);
				startActivity(intent);
			}else if(v == getMerchant04Layout){
				DialogUtils.showToast(context,"暂未开放");
			} else if(v == discountCouponTv){
				mAlertDialog = new AlertDialog.Builder(context).create();
				View view = LayoutInflater.from(context).inflate(R.layout.view_discount_coupon,null);
				mAlertDialog.setView(view);
				mAlertDialog.setCanceledOnTouchOutside(false);
				DiscountCouponView discountCouponView = new DiscountCouponView(context);
				discountCouponView.updataView(new DiscountCouponView.DialogItemClick() {
					@Override
					public void close() {
						mAlertDialog.cancel();
					}

					@Override
					public void commit(String leftEdit, String rightEdit) {
							reqCode(leftEdit,rightEdit);
					}
				});
				mAlertDialog.show();
				mAlertDialog.getWindow().setContentView(discountCouponView);
			}
		}
		if (v == cooponLayout) {
//			if(Util.checkLogin(context)){
//				Intent intent = new Intent(context, CooponListActivity.class);
//				context.startActivity(intent);
//			}
			try {
				KeplerApiManager.getWebViewService().openOrderListWebViewPage("第三方自定义统计6");
			} catch (KeplerBufferOverflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

	}

	private void reqCode(String leftEdit,String rightEdit) {
		final String conversionCode = leftEdit+"-"+rightEdit;
		String url = String.format(HttpUtil.addBaseGetParams(UrlConst.CONVERSION_CODE_URL),conversionCode);
		OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				LogUtil.d(TAG, "error e=" + e.getMessage());
			}

			@Override
			public void onResponse(String response) {
				LogUtil.d(TAG, "response=" + response);
				Gson gson = new Gson();
				ConversionCodeEntity conversionCodeEntity = gson.fromJson(response, ConversionCodeEntity.class);
					if(conversionCodeEntity!=null){
						String info = conversionCodeEntity.getInfo();
						if(conversionCodeEntity.getInfo()!=null){
							DialogUtils.showToast(context,info);
						}
						if(conversionCodeEntity.getStatus() == UrlConst.SUCCESS_CODE){
							Intent intent = new Intent(context, CooponListActivity.class);
							context.startActivity(intent);
							mAlertDialog.cancel();
						}
					}
			}
		});


	}

	// 更新用户信息
	@Override
	public void updateUserInfo(boolean isBackGround) {
		reqData(1);
	}
	public void onEventMainThread(SingnalEvent event)
	{
		if(SingnalEvent.SINGNAL_UPDATE_USERINFO.equals(event.getSingnal())){
			LogUtil.d(TAG, "upload me data");
			reqData(1);
		}
	}
}
