package com.dev.nutclass.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dev.nutclass.NutClassApplication;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.CircleReleaseActivity;
import com.dev.nutclass.activity.HomeActivity;
import com.dev.nutclass.activity.NewestActionActivity;
import com.dev.nutclass.activity.NewestActionActivity02;
import com.dev.nutclass.activity.PayActivity;
import com.dev.nutclass.activity.ReqDiscountActivity;
import com.dev.nutclass.activity.SearchActivity;
import com.dev.nutclass.activity.SelectAddressActivity;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ADAlertEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.parser.CardListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.MyPopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

public class HomeFragmentNew extends BaseFragment {
	private static final String TAG = "HomeFragmentNew";
	private RecyclerView cardListView;
	private Context mContxt;
	private CardListAdapter adapter;
	private int curPage = 1;
	private ImageView searchImageView;
	private ImageView releaseImageView;
	private MaterialRefreshLayout refreshLayout;
	private MyPopupWindow menuWindow;
	private boolean isFirstOpen = true;
	public HomeFragmentNew() {
		// TODO Auto-generated constructor stub
		LogUtil.d(TAG, "HomeFragmentNew");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContxt=getActivity();
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_home, null);
		cardListView = (RecyclerView) view.findViewById(R.id.card_list);
		//标题栏跳转；
		searchImageView = (ImageView) view.findViewById(R.id.iv_home_search);
		releaseImageView = (ImageView) view.findViewById(R.id.iv_home_release);
		searchImageView.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContxt, SearchActivity.class);
				startActivity(intent);
			}
		});
		releaseImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogUtils.showToast(mContxt,"暂未开放");
//				Intent intent = new Intent(mContxt, CircleReleaseActivity.class);
//				startActivity(intent);
			}
		});
		
		String[] filters = { Const.ACTION_BROADCAST_LOGIN_SUCC,
				Const.ACTION_BROADCAST_FEED_CHANGED,
				Const.ACTION_BROADCAST_RECEIVE_MESSAGE,
				Const.ACTION_BROADCAST_FEED_RELEASE_CHANGED };
		registerReceiver(filters);
		
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(
				getActivity());
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		cardListView.setLayoutManager(mLayoutManager);
		refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
		// 定义刷新
		refreshLayout.setLoadMore(false);
		refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(
					final MaterialRefreshLayout materialRefreshLayout) {
				reqData(1);
			}

			@Override
			public void onfinish() {
				// Toast.makeText(getActivity(), "刷新完成",
				// Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void onRefreshLoadMore(
					final MaterialRefreshLayout materialRefreshLayout) {
//				reqData(curPage + 1);
			}

		});
		reqData(1);
		return view;
	}

	public void update(List<BaseCardEntity> list) {
		if (curPage == 1) {
			for(int i = 0;i<list.size();i++){
				if (list.get(i).getCardType() == BaseCardEntity.CARD_TYPE_AlERT){
					list.remove(i);
				}
			}
			adapter = new CardListAdapter(getActivity(), list);
			cardListView.setAdapter(adapter);
		} else {
			adapter.addList(list);
		}
	}

	@Override
	public void onRefresh(boolean b) {
		// TODO Auto-generated method stub
		super.onRefresh(b);
		reqData(1);
	}
	
	/*
	 * 网络请求数据
	 */
	private void reqData(final int page) {
		if (page == 1) {
			curPage=1;
		}
		String device_token = NutClassApplication.device_token;
		String versionNum = SharedPrefUtil.getInstance().getVersionName();
		Util.location(mContxt.getApplicationContext(), null);
		String url = String.format(HttpUtil.addBaseGetParams(UrlConst.GET_HOME),device_token,page,versionNum);
//		String url = "http://dev.kobiko.cn/service_v2.php?act=get_index_data2&pageNo=1&userId=60808&token=35ad8f63f8739a73e0711ba09bcda68f&longitude=116.49702654684&latitude=39.8977879699";
		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
						refreshLayout.setLoadMore(false);
						refreshLayout.finishRefreshLoadMore();
						refreshLayout.finishRefresh();
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						refreshLayout.finishRefreshLoadMore();
						refreshLayout.finishRefresh();
						CardListParser parser = new CardListParser();
						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = result.getList();
							if (list != null && list.size() > 0) {
								curPage = page;
								for(int i = 0;i<list.size();i++){
									if(list.get(i).getCardType()==BaseCardEntity.CARD_TYPE_AlERT){
										ADAlertEntity entity = (ADAlertEntity)list.get(i);
										if(entity.getTan_status()==UrlConst.SUCCESS_CODE){
											if(isFirstOpen){
												alertShow(entity);
											}
											isFirstOpen = false;
										}
									}
								}
								update(list);
							}else{
								refreshLayout.setLoadMore(false);
							}
						}
					}
				});

	}

	public void alertShow(final ADAlertEntity entity) {
		LayoutInflater inflater = (LayoutInflater) mContxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_home_alert, null);
		final ImageView centerImg = (ImageView) view.findViewById(R.id.iv_center_show);
		ImageLoader.getInstance().displayImage(entity.getImg(), centerImg, ImgConfig.getAdItemOption()
				, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String s, View view) {

					}

					@Override
					public void onLoadingFailed(String s, View view, FailReason failReason) {

					}

					@Override
					public void onLoadingComplete(String s, View view, Bitmap bitmap) {
						int width;
						int height;
						width = bitmap.getWidth();
						height = bitmap.getHeight();
						int targetW = 0;
						int targetH = 0;
						targetW = DensityUtil.getDisplayWidth(mContxt) - 50;
						targetH = targetW * height / width;
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(targetW, targetH);
						params.addRule(RelativeLayout.CENTER_IN_PARENT);
						centerImg.setLayoutParams(params);
					}

					@Override
					public void onLoadingCancelled(String s, View view) {

					}
				});
		menuWindow = new MyPopupWindow((Activity) mContxt, view);
		menuWindow.showAtLocation(((Activity) mContxt).findViewById(R.id.rl_parent), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
		menuWindow.setOutsideTouchable(true);
		popWindowSetting();
		centerImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (entity.getApp_jump_key().equals("cuxiao_id")) {
					int id = Integer.parseInt(entity.getApp_jump_value());
					Log.d("===", "id:" + id);
					if (id > 0) {
//						if (id == 24) {
							Intent intent = new Intent(mContxt, NewestActionActivity02.class);
							intent.putExtra("id", entity.getApp_jump_value());
							mContxt.startActivity(intent);
//						} else {
//							Intent intent = new Intent(mContxt, NewestActionActivity.class);
//							intent.putExtra("id", entity.getApp_jump_value());
//							mContxt.startActivity(intent);
//						}
//						Intent intent = new Intent(mContxt, NewestActionActivity.class);
//						intent.putExtra("id",entity.getApp_jump_value());
//						mContxt.startActivity(intent);
					}
				}
				menuWindow.dismiss();

			}
		});
		ImageView closeIv = (ImageView) view.findViewById(R.id.iv_close);
		closeIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
	}

	private void popWindowSetting() {
		WindowManager.LayoutParams lp = ((Activity)mContxt).getWindow().getAttributes();
		lp.alpha = 0.5f;
		((Activity)mContxt).getWindow().setAttributes(lp);
		menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = ((Activity)mContxt).getWindow().getAttributes();
				lp.alpha = 1f;
				((Activity)mContxt).getWindow().setAttributes(lp);
			}
		});
	}

//	private void alertShow(final ADAlertEntity entity) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(mContxt);
//		final AlertDialog mAlertDialog = builder.create();
//		View view = LayoutInflater.from(mContxt).inflate(R.layout.view_home_alert, null);
//		mAlertDialog.setView(view);
//		mAlertDialog.setCanceledOnTouchOutside(false);
//		final ImageView centerImg= (ImageView) view.findViewById(R.id.iv_center_show);
//		ImageLoader.getInstance().displayImage(entity.getImg(), centerImg, ImgConfig.getAdItemOption()
//				, new ImageLoadingListener() {
//					@Override
//					public void onLoadingStarted(String s, View view) {
//
//					}
//
//					@Override
//					public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//					}
//
//					@Override
//					public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//						int width ;
//						int height ;
//						width=bitmap.getWidth();
//						height=bitmap.getHeight();
//						int targetW=0;
//						int targetH=0;
//						targetW=DensityUtil.getDisplayWidth(mContxt)-50;
//						targetH=targetW*height/width;
//						RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(targetW,targetH);
//						params.addRule(RelativeLayout.CENTER_IN_PARENT);
//						centerImg.setLayoutParams(params);
//					}
//
//					@Override
//					public void onLoadingCancelled(String s, View view) {
//
//					}
//				});
//		centerImg.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(entity.getApp_jump_key().equals("cuxiao_id")){
//					int id = Integer.parseInt(entity.getApp_jump_value());
//					Log.d("===","id:"+id);
//					if(id > 0){
//						if(id==24){
//							Intent intent = new Intent(mContxt, NewestActionActivity02.class);
//							intent.putExtra("id",entity.getApp_jump_value());
//							mContxt.startActivity(intent);
//						}else {
//							Intent intent = new Intent(mContxt, NewestActionActivity.class);
//							intent.putExtra("id",entity.getApp_jump_value());
//							mContxt.startActivity(intent);
//						}
////						Intent intent = new Intent(mContxt, NewestActionActivity.class);
////						intent.putExtra("id",entity.getApp_jump_value());
////						mContxt.startActivity(intent);
//					}
//				}
//				mAlertDialog.cancel();
//			}
//		});
//		ImageView closeIv = (ImageView) view.findViewById(R.id.iv_close);
//		closeIv.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mAlertDialog.cancel();
//			}
//		});
//		mAlertDialog.show();
//
//	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			if(data!=null&&data.hasExtra(Const.KEY_CONTENT)){
				reqData(1);
			}
		}
	}

}
