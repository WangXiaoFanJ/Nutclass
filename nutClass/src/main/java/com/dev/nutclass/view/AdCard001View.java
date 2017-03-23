package com.dev.nutclass.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AdCard001View extends LinearLayout {

	private Context context;
	private LinearLayout container;
	private ImageView leftIv;
	private ImageView right1Iv;
	private ImageView right2Iv;

	public AdCard001View(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_ad_001, this);
		container = (LinearLayout) this.findViewById(R.id.ll_container);
		leftIv = (ImageView) this.findViewById(R.id.iv_left);
		right1Iv = (ImageView) this.findViewById(R.id.iv_right1);
		right2Iv = (ImageView) this.findViewById(R.id.iv_right2);
	}

	@SuppressLint("NewApi")
	public void updateView(AdCardEntity entity) {

		if (entity == null) {
			return;
		}
		final List<ImageEntity> adList = entity.getList();
		if (adList == null || adList.size() < 3) {
			return;
		}

		// if (entity != null && entity.getList() != null) {
		// for (int i = 0; i < entity.getList().size(); i++) {
		// AdCardItemView itemView = new AdCardItemView(context, entity
		// .getList().get(i),i);
		// container.addView(itemView, params);
		// }
		// }
		ImageLoader.getInstance().displayImage(adList.get(0).getSmallPath(),
				leftIv, ImgConfig.getAdItemOption());
		ImageLoader.getInstance().displayImage(adList.get(1).getSmallPath(),
				right1Iv, ImgConfig.getAdItemOption());
		ImageLoader.getInstance().displayImage(adList.get(2).getSmallPath(),
				right2Iv, ImgConfig.getAdItemOption());

		leftIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,CourseListActivity.class);
				intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_YIYUAN);
				intent.putExtra(Const.KEY_ID, "1003");
				context.startActivity(intent);
			}
		});
		right1Iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, CourseInfoActivity.class);
				intent.putExtra(Const.KEY_ID, adList.get(1).getId());
				context.startActivity(intent);
			}
		});
		right2Iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, CourseInfoActivity.class);
				intent.putExtra(Const.KEY_ID, adList.get(2).getId());
				context.startActivity(intent);
			}
		});
	}
}
