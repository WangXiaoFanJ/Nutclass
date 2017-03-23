package com.dev.nutclass.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.ActionInfoCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ActionInfoCardView extends LinearLayout implements OnClickListener {

	private Context context;
	private ImageView imgIv;
	private TextView locationTv;
	private TextView titleTv;
	
	private RoundedImageView profileView;
	private TextView nameTv;
	private TextView descTv;
	
	
	private TextView feeTv;
	private TextView timeTv;
	private TextView ageTv;
	
	private LinearLayout attendLayout;
	private LinearLayout attendIconLayout;
	private TextView attendTv;
	
	
	private ActionInfoCardEntity entity;

	public ActionInfoCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_action_info, this);
		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		imgIv = (ImageView) this.findViewById(R.id.iv_img);
		titleTv = (TextView) this.findViewById(R.id.tv_title);
		locationTv = (TextView) this.findViewById(R.id.tv_location);
		
		profileView=(RoundedImageView)this.findViewById(R.id.iv_icon);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		descTv = (TextView) this.findViewById(R.id.tv_desc);
		feeTv = (TextView) this.findViewById(R.id.tv_fee);
		timeTv = (TextView) this.findViewById(R.id.tv_time);
		ageTv = (TextView) this.findViewById(R.id.tv_age);
		
		attendLayout=(LinearLayout)this.findViewById(R.id.ll_attend);
		attendIconLayout=(LinearLayout)this.findViewById(R.id.ll_attend_icon);
		attendTv=(TextView)this.findViewById(R.id.tv_attend);
	}
	private void initListener(){
		setOnClickListener(this);
	}
	public void updateView(ActionInfoCardEntity entity){
		this.entity=entity;
		if(entity==null){
			return;
		}
		ImageLoader.getInstance().displayImage(entity.getIcon(), imgIv,ImgConfig.getCardImgOption());
		titleTv.setText(entity.getActivityTitle());
		locationTv.setText(entity.getAddress());
		
		ImageLoader.getInstance().displayImage(entity.getUserProfile(), profileView,ImgConfig.getPortraitOption());
		nameTv.setText(entity.getUserName());
		descTv.setText(entity.getDetailInfo());
		feeTv.setText(entity.getReturnFee());
		timeTv.setText(entity.getActivityTime());
		ageTv.setText(entity.getActivityAge());
		if(entity.getHeaderList()!=null&&entity.getHeaderList().size()>0){
			attendLayout.setVisibility(View.VISIBLE);
			List<String> headList=entity.getHeaderList();
			attendTv.setText("已报名"+headList.size()+"人");
			for(int i=0;i<headList.size();i++){
				View likerView = LayoutInflater.from(context).inflate(
						R.layout.item_like, null);
				RoundedImageView itemIv = (RoundedImageView) likerView
						.findViewById(R.id.iv_like);
				itemIv.setOval(true);
				itemIv.setScaleType(ScaleType.FIT_XY);
				ImageLoader.getInstance().displayImage(
						headList.get(i), itemIv,
						ImgConfig.getPortraitOption());
				attendIconLayout.addView(likerView);
			}
		}else{
			attendLayout.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		DialogUtils.showToast(context, "热门活动详情");
		
	}
	 

}
