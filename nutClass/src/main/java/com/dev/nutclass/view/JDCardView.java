package com.dev.nutclass.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.JDItemCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JDCardView extends LinearLayout implements OnClickListener{

	private Context context;
	private LinearLayout leftLayout;
	private LinearLayout rightLayout;
	private SquareImageView leftIv;
	
	private SquareImageView rightIv;
	private TextView leftDescTv;
	private TextView rightDescTv;
	private TextView leftPriceTv;
	private TextView rightPriceTv;
	
	private JDItemCardEntity entity=null;

	public JDCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_jd_item, this);
		leftLayout=(LinearLayout) this.findViewById(R.id.ll_left);
		rightLayout=(LinearLayout) this.findViewById(R.id.ll_right);
		
		leftIv=(SquareImageView)this.findViewById(R.id.iv_pic_left);
		rightIv=(SquareImageView)this.findViewById(R.id.iv_pic_right);

		leftDescTv=(TextView)this.findViewById(R.id.tv_desc_left);
		rightDescTv=(TextView)this.findViewById(R.id.tv_desc_right);
		leftPriceTv=(TextView)this.findViewById(R.id.tv_price_left);
		rightPriceTv=(TextView)this.findViewById(R.id.tv_price_right);
		
		
		
	}

	public void updateView(JDItemCardEntity entity) {
		this.entity=entity;
		setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		if(entity==null){
			setVisibility(View.GONE);
		}
		if(TextUtils.isEmpty(entity.getRightId())){
			rightLayout.setVisibility(View.INVISIBLE);
		}else{
			rightLayout.setVisibility(View.VISIBLE);
		}
		ImageLoader.getInstance().displayImage(entity.getPicLeftUrl(),
				leftIv, ImgConfig.getAdItemOption());
		leftDescTv.setText(entity.getLeftDesc());
		leftPriceTv.setText("价格：￥"+entity.getLeftPrice());
		ImageLoader.getInstance().displayImage(entity.getPicRightUrl(),
				rightIv, ImgConfig.getAdItemOption());
		rightDescTv.setText(entity.getRightDesc());
		rightPriceTv.setText("价格：￥"+entity.getRightPrice());
		
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==leftLayout){
			try {
				KeplerApiManager.getWebViewService().openItemDetailsWebViewPage(entity.getLeftId(), "");
			} catch (KeplerBufferOverflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
		}else if(v==rightLayout){
			try {
				KeplerApiManager.getWebViewService().openItemDetailsWebViewPage(entity.getRightId(), "");
			} catch (KeplerBufferOverflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
