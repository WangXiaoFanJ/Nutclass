package com.dev.nutclass.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.activity.BigImgActivity;
import com.dev.nutclass.activity.CircleInfoActivity;
import com.dev.nutclass.activity.CircleReleaseActivity;
import com.dev.nutclass.activity.FeedUserPageActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.CommentCardEntity;
import com.dev.nutclass.entity.FeedCardEntity;
import com.dev.nutclass.entity.ShareEntity;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

public class FeedUserCardView extends LinearLayout implements OnClickListener {
	private static final String TAG = "FeedUserCardView";
	private Context context;

	private RoundedImageView imgIv;
	private TextView timeLineTv;
	private TextView descTv;
	private TextView itemTV;

	 

	private LinearLayout contentLayout;
	private ImageView cameraIv;
	
	private FeedCardEntity entity;

	public FeedUserCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_feed_user, this);

		imgIv = (RoundedImageView) this.findViewById(R.id.view_img);
		descTv = (TextView) this.findViewById(R.id.tv_desc);
		itemTV = (TextView) this.findViewById(R.id.tv_item);
		timeLineTv = (TextView) this.findViewById(R.id.tv_timeline);
		contentLayout = (LinearLayout) this.findViewById(R.id.ll_container);
		cameraIv = (ImageView) this.findViewById(R.id.iv_camera);
	}

	private void initListener() {
		imgIv.setOnClickListener(this);
		cameraIv.setOnClickListener(this);
		setOnClickListener(this);
	}

	// 完善信息
	public void updateView(final FeedCardEntity entity) {
		this.entity = entity;

		// Author
		if(TextUtil.isNotNull(entity.getTime())){
			String[] str=entity.getTime().split("-");
			if(str!=null&&str.length==3){
				timeLineTv.setText(str[1]+" "+str[2]);
			}
		}

		// 描述
		if (TextUtils.isEmpty(entity.getContent())) {
			descTv.setVisibility(View.GONE);
		} else {
			descTv.setVisibility(View.VISIBLE);
			descTv.setText(entity.getContent());
		}
		// 图片
		if (entity.getImgList() != null) {
			imgIv.setVisibility(View.VISIBLE);
			itemTV.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(entity.getImgList().get(0).getSmallPath(), imgIv);
			itemTV.setText("共"+entity.getImgList().size()+"项");
		}else{
			imgIv.setVisibility(View.GONE);
			itemTV.setVisibility(View.GONE);
		}
		
		
		
		// 评论
		//updateComment();
		if(entity.isSelfProfile()){
			contentLayout.setVisibility(View.GONE);
			cameraIv.setVisibility(View.VISIBLE);
			timeLineTv.setText("今天");
//			ImageLoader.getInstance().displayImage(UrlConst.BASE_NET_HOST+entity.getHeadImgUrl(), iconIv);
		}else{
			contentLayout.setVisibility(View.VISIBLE);
			cameraIv.setVisibility(View.GONE);
//			ImageLoader.getInstance().displayImage(entity.getHeadImgUrl(), iconIv);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == imgIv) {
			Intent intent = new Intent();
			intent.setClass(context, BigImgActivity.class);
			intent.putExtra(Const.KEY_ENTITY_LIST, entity.getImgList());
			intent.putExtra(Const.KEY_POSITION, 0);
			context.startActivity(intent);
		}else if(v==cameraIv){
			Intent intent = new Intent(context, CircleReleaseActivity.class);
			context.startActivity(intent);
		} else {
			if(context instanceof CircleInfoActivity){
				
			}else{
				Intent intent = new Intent(context, CircleInfoActivity.class);
				intent.putExtra(Const.KEY_ID, this.entity.getId());
				context.startActivity(intent);
			}
			
		}
	}
}
