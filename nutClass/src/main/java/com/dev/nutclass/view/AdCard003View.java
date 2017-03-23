package com.dev.nutclass.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AdCard003View extends LinearLayout {

	private Context context;
	private LinearLayout container;

	public AdCard003View(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_ad_003, this);
		container = (LinearLayout) this.findViewById(R.id.ll_container);
	}

	@SuppressLint("NewApi")
	public void updateView(AdCardEntity entity) {
		List<ImageEntity> adList=null;
		if(entity!=null){
			adList=entity.getList();
		}else{
			return;
		}
		if(adList==null||adList.size()<=0){
			return;
		}
		container.removeAllViews();

//		if (entity != null && entity.getList() != null) {
//			for (int i = 0; i < entity.getList().size(); i++) {
//				AdCardItemView itemView = new AdCardItemView(context, entity
//						.getList().get(i),i);
//				container.addView(itemView, params);
//			}
//		}
		LinearLayout rowLayout=null;
		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		 
		int size=DensityUtil.getDisplayWidth(context)/4;
		for (int i = 0; i < adList.size(); i++) {
			LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(size,(int) ((size)*110*1.0f/130));
			if(i%4==0){
				rowLayout=new LinearLayout(context);
				rowLayout.setLayoutDirection(HORIZONTAL);
			}
			final ImageEntity iEntity=adList.get(i);
			
			View v=LayoutInflater.from(getContext()).inflate(R.layout.card_ad_003_item, null);
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,CourseListActivity.class);
					intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_SCHOOL);
					intent.putExtra(Const.KEY_ID, iEntity.getId());
					intent.putExtra(Const.KEY_TITLE, iEntity.getImgName());
					context.startActivity(intent);
				}
			});
			RoundedImageView itemView=(RoundedImageView)v.findViewById(R.id.iv_img);
			ImageLoader.getInstance().displayImage(adList.get(i).getSmallPath(), itemView,ImgConfig.getAdItemOption());
			rowLayout.addView(v,cellParams);
			if(i%4==3||i==adList.size()-1){
				container.addView(rowLayout, rowParams);
			}


		}

	}
}
