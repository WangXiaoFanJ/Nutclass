package com.dev.nutclass.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.MarketInfoActivity;
import com.dev.nutclass.activity.ZeroInfoActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.MarketCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.parser.BrandListParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

public class MarketCardView extends LinearLayout implements OnClickListener {
	private static final String TAG = "MarketCardView";
	private Context context;
	private ImageView iconIv;
	private TextView nameTv;
	private TextView scapeTv;// 范围
	private TextView timeTv;// 范围
	private TextView priceTv;
	private TextView personalTv;
	private TextView statusTv;

	private MarketCardEntity entity;

	public MarketCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MarketCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub

	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.card_market, this);
		setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		iconIv = (ImageView) this.findViewById(R.id.iv_icon);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		scapeTv = (TextView) this.findViewById(R.id.tv_scape);
		timeTv = (TextView) this.findViewById(R.id.tv_time);
		priceTv = (TextView) this.findViewById(R.id.tv_price);
		personalTv = (TextView) this.findViewById(R.id.tv_personal);
		statusTv = (TextView) this.findViewById(R.id.tv_status);
		setOnClickListener(this);
		statusTv.setOnClickListener(this);
	}

	public void updateView(MarketCardEntity entity) {
		this.entity = entity;
		if (entity == null) {
			setVisibility(View.GONE);
			return;
		}
		ImageLoader.getInstance().displayImage(
				UrlConst.BASE_NET_HOST + entity.getImage(), iconIv,
				ImgConfig.getPortraitOption());
		nameTv.setText(entity.getTitle());
		scapeTv.setText(entity.getDistrict());
		timeTv.setText(entity.getTime());
		priceTv.setText("￥ "+entity.getPrice() + "元");
		personalTv.setText(entity.getIsPersonal());
		if(entity.isMine()){
			statusTv.setVisibility(View.VISIBLE);
			statusTv.setText(entity.getStatusInfo());
		}else{
			statusTv.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==statusTv){
			reqData();
		}else{
			Intent intent = new Intent();
			intent.setClass(context, MarketInfoActivity.class);
			intent.putExtra(Const.KEY_ID, entity.getId());
			context.startActivity(intent);
		}
//		DialogUtils.showToast(context, "进入Market详情页");
	}
	/*
	 * 网络请求数据
	 */
	private void reqData(){
		int type=0;
		if(entity.getStatus()==0){
			type=1;
		}else{
			type=0;
		}
		String url=HttpUtil.addToken(String.format(UrlConst.USERINFO_MODIFY_MARKET_STATUS,entity.getId(),type));
		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
					}
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						SimpleInfoParser parser=new SimpleInfoParser("status");
						JsonResult<String>  result=(JsonResult<String>) parser.parse(response);
						if(result.getErrorCode()==UrlConst.SUCCESS_CODE){
							DialogUtils.showToast(context, result.getErrorMsg());
							int status=Integer.valueOf(result.getRetObj());
							if(status==0){
								entity.setStatus(status);
								entity.setStatusInfo("交易中");
							}else{
								entity.setStatus(status);
								entity.setStatusInfo("已下架");
							}
							statusTv.setText(entity.getStatusInfo());
						}
					}
				});
		 
	}
}
