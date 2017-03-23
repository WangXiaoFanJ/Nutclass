package com.dev.nutclass.view;

import java.util.List;

import org.json.JSONObject;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.BrandListActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.adapter.AlbumDirAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.StorageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class BrandCardView extends LinearLayout implements OnClickListener {

	private Context context;
	private RoundedImageView imgIv;
	private View  root;
	private ImageEntity entity;

	public BrandCardView(Context context,ImageEntity imgEntity) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.entity=imgEntity;
		initView();
		initListener();
	}
	public BrandCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		root=LayoutInflater.from(context).inflate(R.layout.card_brand,this);
		imgIv=(RoundedImageView)root.findViewById(R.id.iv_img);
//		adTv.setText(imgEntity.getImgName());
//		ImageLoader.getInstance().displayImage(StorageUtil.getPid2Url(imgEntity.getSmallPath()), adIv,ImgConfig.getCardImgOption());
	}
	private void initListener(){
		root.setOnClickListener(this);
	}
	public void updateView(ImageEntity imgEntity){
		this.entity=imgEntity;
		if(imgEntity==null||TextUtils.isEmpty(imgEntity.getSmallPath())){
			return;
		}
		ImageLoader.getInstance().displayImage(imgEntity.getSmallPath(),imgIv,ImgConfig.getCardImgOption());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(context, CourseListActivity.class);
		intent.putExtra(Const.KEY_ID, entity.getId());
		intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BRAND);
		context.startActivity(intent);
	}
}
