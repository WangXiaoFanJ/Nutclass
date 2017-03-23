package com.dev.nutclass.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.ZeroCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

public class ZeroInfoActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private final static String TAG = "ZeroInfoActivity";
	private ImageView iconIv;
	private TextView nameTv;
	private TextView scapeTv;
	private TextView priceTv;
	private TextView stateTv;
	private TextView detailTv;
	
	private TextView actionTimeTv;
	private TextView praiseTimeTv;
	

	private TextView attendTv;
	private TextView shareTv;
	

	private ZeroCardEntity entity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ZeroInfoActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_zero_info);
		initView();
		initIntent();
		initData();
		initListener();
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	private void initView() {
		iconIv = (ImageView) this.findViewById(R.id.iv_icon);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		scapeTv = (TextView) this.findViewById(R.id.tv_scape);
		priceTv = (TextView) this.findViewById(R.id.tv_price);
		stateTv = (TextView) this.findViewById(R.id.tv_periodical);
		detailTv = (TextView) this.findViewById(R.id.tv_zero_info);
		
		actionTimeTv = (TextView) this.findViewById(R.id.tv_action_time);
		praiseTimeTv = (TextView) this.findViewById(R.id.tv_praise_time);
		
		attendTv = (TextView) this.findViewById(R.id.tv_attend);
		shareTv = (TextView) this.findViewById(R.id.tv_share);
	}

	private void initIntent() {
		if (getIntent().hasExtra(Const.KEY_ENTITY)) {
			entity = (ZeroCardEntity) getIntent().getSerializableExtra(Const.KEY_ENTITY);
		}else{
			finish();
		}
	}

	private void initData() {
		if (entity == null) {
			return;
		}
		ImageLoader.getInstance().displayImage(
				entity.getZeroActivityImageUrl(), iconIv,
				ImgConfig.getPortraitOption());
		nameTv.setText(entity.getZeroActivityTitle());
		scapeTv.setText("全国通用");
		priceTv.setText("市场价："+entity.getZeroActivityPrice());
		stateTv.setText("第"+entity.getZeroActivityNo()+"期");
		
		SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
		String startTime=formate.format(new Date(entity.getStartTime()*1000));
		String endTime=formate.format(new Date(entity.getEndTime()*1000));
		actionTimeTv.setText("活动时间："+startTime+"-"+endTime);
		praiseTimeTv.setText("开奖时间："+endTime);
		detailTv.setText(entity.getActivityDetail());
	}

	private void initListener() {
		attendTv.setOnClickListener(this);
		shareTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (Util.checkLogin(context)) {
			if(v==attendTv){
				reqCanPraise();
			}else if(v==shareTv){
//				SnsUtil.getInstance(context).openShare((Activity) context, false);
				reqSharePraise();
			}
		}
	}

	
	/*
	 * 网络请求数据,判断用户是否有抽奖机会
	 */
	private void reqCanPraise() {
		String url = String.format(HttpUtil
				.addBaseGetParams(UrlConst.ZERO_CAN_PRAISE_URL), entity.getZeroActivityId());
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

						SimpleInfoParser parser = new SimpleInfoParser("hasphone");
						JsonResult<String> result = (JsonResult<String>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							String ret= result.getRetObj();
							if(TextUtil.isNotNull(ret)&&ret.equals("1")){
								reqPraise("");
							}else{
								final EditText et = new EditText(context);  
						        et.setText("");  
						                //获取ip而已，不用在乎  
						        new AlertDialog.Builder(context).setTitle("请绑定手机号")  
						                .setIcon(android.R.drawable.ic_dialog_info).setView(et)  
						                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
						                    @Override  
						                    public void onClick(DialogInterface arg0, int arg1) {  
						                        //数据获取  
						            			String mobile = et.getText().toString().trim();
						            			if (TextUtil.isNotNull(mobile)) {
						            				if ("".equals(mobile)) { // 判断 帐号和密码
						            					DialogUtils.showToast(context, "手机号不能为空");
						            				} else if (!Util.isMobileNO(mobile)) {
						            					DialogUtils.showToast(context, "手机号错误");
						            				} else {
						            					reqPraise(mobile);
						            				}
						            			}
						                    }  
						                }).setNegativeButton("取消", null).show();  
								
							}
						}else{
							DialogUtils.showToast(context, "你已经抽过，请关注开奖状态");
						}
					}
				});

	}
	
	/*
	 * 网络请求数据,请求抽奖
	 */
	private void reqPraise(String mobile) {
		String url = String.format(HttpUtil
				.addBaseGetParams(UrlConst.ZERO_PRAISE_URL), entity.getZeroActivityId(),mobile);
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
				SimpleInfoParser parser = new SimpleInfoParser();
				JsonResult<String> result = (JsonResult<String>) parser
						.parse(response);
				if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
					DialogUtils.showToast(context, "感谢您参与本次活动，请及时关注活动动态");
				}else{
					DialogUtils.showToast(context, "兑奖码生成错误，请联系客服");
				}
			}
		});
	}
	/*
	 * 网络请求数据,分享请求抽奖次数
	 */
	private void reqSharePraise() {
		String url = String.format(HttpUtil
				.addBaseGetParams(UrlConst.ZERO_SHARE_BACK_URL), entity.getZeroActivityId());
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
				SimpleInfoParser parser = new SimpleInfoParser();
				JsonResult<String> result = (JsonResult<String>) parser
						.parse(response);
				if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
//					DialogUtils.showToast(context, "获得抽奖机会成功");
				}else{
					DialogUtils.showToast(context, "获得抽奖机会失败，仅第一次分享有效");
				}
			}
		});
	}
	
	
}
