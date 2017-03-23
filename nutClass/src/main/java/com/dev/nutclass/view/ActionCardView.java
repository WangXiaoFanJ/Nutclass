package com.dev.nutclass.view;

import java.util.List;

import org.json.JSONObject;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.ActionInfoActivity;
import com.dev.nutclass.activity.ActionListActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.adapter.AlbumDirAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.ActionCardEntity;
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

public class ActionCardView extends LinearLayout implements OnClickListener {

	private Context context;
	private ImageView imgIv;
	private TextView locationTv;
	private TextView titleTv;
	private TextView stateTv;
	private ActionCardEntity entity;

	public ActionCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_action, this);
		imgIv = (ImageView) this.findViewById(R.id.iv_img);
		titleTv = (TextView) this.findViewById(R.id.tv_title_new);
		locationTv = (TextView) this.findViewById(R.id.tv_location);
		stateTv = (TextView) this.findViewById(R.id.tv_state_new);
	}
	private void initListener(){
		setOnClickListener(this);
	}
	public void updateView(ActionCardEntity entity){
		this.entity=entity;
		if(entity==null){
			return;
		}
		ImageLoader.getInstance().displayImage(entity.getIcon(), imgIv,ImgConfig.getCardImgOption());
		titleTv.setText(entity.getActivityTitle());
		locationTv.setText(entity.getAddress());
		stateTv.setText(entity.getActivityTag());
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		DialogUtils.showToast(context, "热门活动详情");
		Intent intent=new Intent(context,ActionInfoActivity.class);
		intent.putExtra(Const.KEY_ID, entity.getId());
		context.startActivity(intent);
	}
	 

}
