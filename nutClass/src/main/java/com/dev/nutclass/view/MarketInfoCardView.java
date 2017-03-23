package com.dev.nutclass.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.ActionInfoCardEntity;
import com.dev.nutclass.entity.MarketInfoCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MarketInfoCardView extends LinearLayout implements OnClickListener {

	private Context context;
	
	private RoundedImageView profileView;
	private TextView nameTv;
	private TextView districtTv;
	private TextView timeTv;

	private TextView descTv;
	private TextView priceTv;
	private TextView scanTv;

	private LinearLayout bannerLayout;
	
	
	private MarketInfoCardEntity entity;

	public MarketInfoCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_market_info, this);
		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		profileView=(RoundedImageView)this.findViewById(R.id.iv_icon);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		timeTv = (TextView) this.findViewById(R.id.tv_time);
		descTv = (TextView) this.findViewById(R.id.tv_desc);
		priceTv = (TextView) this.findViewById(R.id.tv_price);
		scanTv = (TextView) this.findViewById(R.id.tv_scan);
		districtTv = (TextView) this.findViewById(R.id.tv_title);
		bannerLayout=(LinearLayout)this.findViewById(R.id.ll_banner);
	}
	private void initListener(){
		setOnClickListener(this);
	}
	public void updateView(MarketInfoCardEntity entity){
		this.entity=entity;
		if(entity==null){
			return;
		}
		ImageLoader.getInstance().displayImage(entity.getPortrait(), profileView,ImgConfig.getPortraitOption());
		
		nameTv.setText(entity.getTitle());
		timeTv.setText(entity.getDate());
		districtTv.setText(entity.getDistrict());
		descTv.setText(entity.getDescription());
		priceTv.setText("售卖价格："+entity.getPrice()+"元");
		scanTv.setText(entity.getViews());
		bannerLayout.removeAllViews();
		BannerCardView bannerView=new BannerCardView(context);
		bannerView.updateView(entity.getBannerCardEntity(),3);
		bannerLayout.addView(bannerView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		DialogUtils.showToast(context, "热门活动详情");
		
	}
	 

}
