package com.dev.nutclass.view;

import java.util.List;

import org.json.JSONObject;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.ActionListActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.activity.MarketListActivity;
import com.dev.nutclass.activity.WebViewActivity;
import com.dev.nutclass.adapter.AlbumDirAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.StorageUtil;
import com.dev.nutclass.utils.TextUtil;
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

public class AdItemView extends LinearLayout implements OnClickListener {

	private Context context;
	private LinearLayout adLayout;
	private RoundedImageView adIv;
	private TextView adTv;
	private ImageEntity imgEntity;
	private int pos=0;

	public AdItemView(Context context,ImageEntity imgEntity,int pos) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.imgEntity=imgEntity;
		this.pos=pos;
		initView();
		initData();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.item_ad, this);
		adLayout = (LinearLayout) this.findViewById(R.id.rl_ad);
		adIv = (RoundedImageView) this.findViewById(R.id.iv_ad);
		adTv = (TextView) this.findViewById(R.id.tv_ad);
		
//		adTv.setText(imgEntity.getImgName());
//		ImageLoader.getInstance().displayImage(StorageUtil.getPid2Url(imgEntity.getSmallPath()), adIv,ImgConfig.getCardImgOption());
	}
	private void initListener(){
		adLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		Intent intent=new Intent(context,CourseListActivity.class);
//		switch (pos) {
//		case 0:
//			DialogUtils.showToast(context, "购买课程");
//			intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BUY);
//			context.startActivity(intent);
//			break;
//		case 1:
//			DialogUtils.showToast(context, "预约试听");
//			intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BOOK);
//			context.startActivity(intent);
//			break;
//		case 2:
//			DialogUtils.showToast(context, "最新活动");
//			intent=new Intent(context,ActionListActivity.class);
//			context.startActivity(intent);
//			break;
//		case 3:
//			DialogUtils.showToast(context, "跳蚤市场");
//			intent=new Intent(context,MarketListActivity.class);
//			context.startActivity(intent);
//			break;
//
//		default:
//			break;
//		}
		if(TextUtil.isNotNull(imgEntity.getH5Url())){
			Intent intent=new Intent(context,WebViewActivity.class);
			intent.putExtra(Const.KEY_URL, imgEntity.getH5Url());
			intent.putExtra(Const.KEY_TITLE, imgEntity.getImgName());
			context.startActivity(intent);
		}else{
			/**
			 * 1001  游玩票务
1002  跳蚤市场
1003 1元购课
1004 最新活动
			 * */
			if("1001".equals(imgEntity.getId())){
				DialogUtils.showToast(context, "敬请期待");
			}else if("1002".equals(imgEntity.getId())){
				Intent intent=new Intent(context,MarketListActivity.class);
				context.startActivity(intent);
			}else if("1003".equals(imgEntity.getId())){
				Intent intent=new Intent(context,CourseListActivity.class);
				intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_YIYUAN);
				intent.putExtra(Const.KEY_ID, "1003");
				context.startActivity(intent);
			}else if("1004".equals(imgEntity.getId())){
				Intent intent=new Intent(context,ActionListActivity.class);
				context.startActivity(intent);
				
			}else if("-1".equals(imgEntity.getId())){
				DialogUtils.showToast(context, "敬请期待");
				
			}else{
				Intent intent=new Intent(context,CourseListActivity.class);
				intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_SCHOOL);
				intent.putExtra(Const.KEY_ID, imgEntity.getId());
				intent.putExtra(Const.KEY_TITLE, imgEntity.getImgName());
				context.startActivity(intent);
			}
			
		}
		
	}
	private void initData(){
		if(imgEntity==null){
			return;
		}
		if("-1".equals(imgEntity.getId())){
			adTv.setVisibility(View.GONE);
			adIv.setImageResource(R.drawable.icon_default_add);
		}else{
			adTv.setText(imgEntity.getImgName());
			ImageLoader.getInstance().displayImage(imgEntity.getSmallPath(), adIv,ImgConfig.getAdItemOption());
		}
		
	}

}
