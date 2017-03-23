package com.dev.nutclass.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.activity.SearchActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class AdCard002View extends LinearLayout implements OnClickListener {

	private Context context;
	private LinearLayout container;
	private LinearLayout ad1Layout;
	private LinearLayout ad2Layout;
	private LinearLayout ad3Layout;
	private RoundedImageView ad1View;
	private TextView ad1Tv;
	private RoundedImageView ad2View;
	private TextView ad2Tv;
	private RoundedImageView ad3View;
	private TextView ad3Tv;

	private ImageView headIv;
	private TextView moreTv;
	private LinearLayout llNearby;

	public AdCard002View(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_ad_002, this);
		container = (LinearLayout) this.findViewById(R.id.ll_container);
		ad1Layout = (LinearLayout) this.findViewById(R.id.ll_ad1);
		ad2Layout = (LinearLayout) this.findViewById(R.id.ll_ad2);
		ad3Layout = (LinearLayout) this.findViewById(R.id.ll_ad3);
		ad1View = (RoundedImageView) this.findViewById(R.id.iv_ad1);
		ad2View = (RoundedImageView) this.findViewById(R.id.iv_ad2);
		ad3View = (RoundedImageView) this.findViewById(R.id.iv_ad3);

		ad1Tv = (TextView) this.findViewById(R.id.tv_ad1);
		ad2Tv = (TextView) this.findViewById(R.id.tv_ad2);
		ad3Tv = (TextView) this.findViewById(R.id.tv_ad3);

		headIv = (ImageView) this.findViewById(R.id.iv_head);
		moreTv = (TextView) this.findViewById(R.id.tv_more);
		llNearby = (LinearLayout) this.findViewById(R.id.ll_nearby);
	}

	private List<ImageEntity> adList = null;
	private AdCardEntity entity = null;
	private int size = 0;
	private ImageLoadingListener listener;

	@SuppressLint("NewApi")
	public void updateView(AdCardEntity entity) {
		this.entity = entity;
		if (entity != null) {
			adList = entity.getList();
		} else {
			return;
		}
		if (adList == null || adList.size() < 2) {
			return;
		}
		size = adList.size();
		if(entity.getHeadName().equals("升学辅导")){
			llNearby.setVisibility(View.VISIBLE);
		}else{
			llNearby.setVisibility(View.GONE);
		}
		if (adList.size() > 2) {
			listener = new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingFailed(String arg0, View arg1,
						FailReason arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingComplete(String arg0, View arg1,
						Bitmap arg2) {
					// TODO Auto-generated method stub
					int width = arg2.getWidth();
					int height = arg2.getHeight();
					int targetW = 0;
					int targetH = 0;
					targetW = (DensityUtil.getDisplayWidth(context) - 60) / 3;
					targetH = targetW * height / width;
					LinearLayout.LayoutParams params = new LayoutParams(
							targetW, targetH);
					arg1.setLayoutParams(params);
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub

				}
			};
			ad3Layout.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					adList.get(0).getSmallPath(), ad1View,
					ImgConfig.getAdItemOption(), listener);
			ImageLoader.getInstance().displayImage(
					adList.get(1).getSmallPath(), ad2View,
					ImgConfig.getAdItemOption(), listener);
			ImageLoader.getInstance().displayImage(
					adList.get(2).getSmallPath(), ad3View,
					ImgConfig.getAdItemOption(), listener);
			ad1Tv.setText(adList.get(0).getImgName());
			ad2Tv.setText(adList.get(1).getImgName());
			ad3Tv.setText(adList.get(2).getImgName());
		} else {
			
			listener = new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingFailed(String arg0, View arg1,
						FailReason arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingComplete(String arg0, View arg1,
						Bitmap arg2) {
					// TODO Auto-generated method stub
					int width = arg2.getWidth();
					int height = arg2.getHeight();
					int targetW = 0;
					int targetH = 0;
					targetW = (DensityUtil.getDisplayWidth(context) - 60) / 2;
					targetH = targetW * height / width;
					LinearLayout.LayoutParams params = new LayoutParams(
							targetW, targetH);
					arg1.setLayoutParams(params);
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub

				}
			};
			ad3Layout.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					adList.get(0).getSmallPath(), ad1View,
					ImgConfig.getAdItemOption(),listener);
			ImageLoader.getInstance().displayImage(
					adList.get(1).getSmallPath(), ad2View,
					ImgConfig.getAdItemOption(),listener);
			ad1Tv.setText(adList.get(0).getImgName());
			ad2Tv.setText(adList.get(1).getImgName());
		}
		ImageLoader.getInstance().displayImage(entity.getHeadUrl(), headIv,
				ImgConfig.getAdItemOption());
		headIv.setOnClickListener(this);
		moreTv.setOnClickListener(this);
		ad1Layout.setOnClickListener(this);
		ad2Layout.setOnClickListener(this);
		ad3Layout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (entity == null || adList == null)
			return;
		if (v == moreTv) {
			Intent intent = new Intent(context, SearchActivity.class);
			intent.putExtra(Const.KEY_KEYWORD, entity.getHeadName());
			context.startActivity(intent);
		} else if (v == ad1Layout) {
			Intent intent = new Intent(context, SearchActivity.class);
			intent.putExtra(Const.KEY_KEYWORD, adList.get(0).getImgName());
			context.startActivity(intent);
		} else if (v == ad2Layout) {
			Intent intent = new Intent(context, SearchActivity.class);
			intent.putExtra(Const.KEY_KEYWORD, adList.get(1).getImgName());
			context.startActivity(intent);
		} else if (v == ad3Layout) {
			Intent intent = new Intent(context, SearchActivity.class);
			intent.putExtra(Const.KEY_KEYWORD, adList.get(2).getImgName());
			context.startActivity(intent);
		}
	}
}
