package com.dev.nutclass.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dev.nutclass.R;
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.db.NutClassDB;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.DBEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.SchoolEntity;
import com.dev.nutclass.parser.CommentParser;
import com.dev.nutclass.parser.CourseInfoParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.PayResult;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;

public class CourseInfoActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private final static String TAG = "CourseInfoActivity";

	private RecyclerView cardListView;
	private LinearLayoutManager mLayoutManager;
	private CardListAdapter adapter;

	private ImageView backIv;
	private ImageView shareIv;
	private ImageView brandIv;
//	private TextView commentTv;

	private LinearLayout telLayout;
	private TextView addBusTv;
	private LinearLayout bookLayout;
	private LinearLayout locationLayout;
	private TextView buyTv;

	private MaterialRefreshLayout refreshLayout;
	private int curPage = 1;

	private CourseCardEntity entity;
	private String pid;
	private String schoolId="";
	private ImageView commentIv;
	private List<String> plusGoodsList;
	private	StringBuffer stringBuffer = null;
	private Double plusPriceTotalMoney;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = CourseInfoActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_course_info);
		initView();
		initIntent();
		initData();
		initListener();
		setOnGestureBackEnable(false);
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	private void initView() {
		cardListView = (RecyclerView) findViewById(R.id.card_list);
		mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		cardListView.setLayoutManager(mLayoutManager);
		backIv = (ImageView) findViewById(R.id.iv_back);
		shareIv = (ImageView) findViewById(R.id.iv_share);
		brandIv = (ImageView) findViewById(R.id.iv_brand);
//		commentTv = (TextView) findViewById(R.id.tv_comment);
		commentIv  = (ImageView) findViewById(R.id.iv_comment);
		telLayout = (LinearLayout) findViewById(R.id.ll_tel);
		bookLayout = (LinearLayout) findViewById(R.id.ll_book);
		locationLayout = (LinearLayout) findViewById(R.id.ll_loc);

		buyTv = (TextView) findViewById(R.id.tv_buy);
		addBusTv = (TextView) findViewById(R.id.tv_add_bus);

		refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
		// 定义刷新
		refreshLayout.setLoadMore(true);
		refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(
					final MaterialRefreshLayout materialRefreshLayout) {
				reqHeadData();
			}

			@Override
			public void onfinish() {
				// Toast.makeText(getActivity(), "刷新完成",
				// Toast.LENGTH_LONG).show();

			}

			@Override
			public void onRefreshLoadMore(
					final MaterialRefreshLayout materialRefreshLayout) {
				reqCommentData(curPage + 1);
			}

		});
		plusGoodsList = new ArrayList<>();
	}

	private void initIntent() {
		if (getIntent().hasExtra(Const.KEY_ID)) {
			pid = getIntent().getStringExtra(Const.KEY_ID);
		}
//		if (TextUtils.isEmpty(pid)) {
//			pid = "273";
//		}
		if (getIntent().hasExtra(Const.KEY_SCHOOL_ID)) {
			schoolId = getIntent().getStringExtra(Const.KEY_SCHOOL_ID);
		}
//		if (TextUtils.isEmpty(schoolId)) {
//			schoolId = "6";
//		}
	}

	private void initData() {
		reqHeadData();
	}

	private void initListener() {
		backIv.setOnClickListener(this);
		shareIv.setOnClickListener(this);
		brandIv.setOnClickListener(this);
//		commentTv.setOnClickListener(this);
		commentIv.setOnClickListener(this);
		bookLayout.setOnClickListener(this);
		telLayout.setOnClickListener(this);
		addBusTv.setOnClickListener(this);
		buyTv.setOnClickListener(this);
		locationLayout.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backIv) {
			finish();
		} else if (v == telLayout) {
			if (entity != null && entity.getSchoolEntity() != null
					&& TextUtil.isNotNull(entity.getSchoolEntity().getTel())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(entity.getSchoolEntity().getTel());
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
								+ entity.getSchoolEntity().getTel()));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				builder.show();
			}

		} else if (v == shareIv) {
			
			SnsUtil.getInstance(context).openShare((Activity) context,entity.getShareEntity());
		} else if (v == brandIv) {
			Intent intent = new Intent();
			intent.setClass(context, CourseListActivity.class);
			intent.putExtra(Const.KEY_ID, entity.getBrandId() + "");
			intent.putExtra(Const.KEY_TYPE, CourseListActivity.TYPE_FROM_BRAND);
			context.startActivity(intent);
		} else {
			if (Util.checkLogin(context)) {
				if (v == commentIv) {

					final EditText et = new EditText(this);
					et.setText("");
					// 获取ip而已，不用在乎
					new AlertDialog.Builder(this)
							.setTitle("评论")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(et)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											String str = et.getText()
													.toString().trim();
											// 将输入的用户名和密码打印出来
											if (TextUtil.isNotNull(str)) {
												reqComment(str);
											} else {
												DialogUtils.showToast(context,
														"请输入点评内容");
											}
										}
									}).setNegativeButton("取消", null).show();
				} else if (v == buyTv) {
					// DialogUtils.showItemsDialog(context, new String[] {
					// "支付宝支付", "微信支付" },
					// new ItemSelectedListener() {
					//
					// @Override
					// public void onItemSelected(DialogInterface dialog,
					// String text, int which) {
					// // TODO Auto-generated method stub
					// if(which==0){
					// reqOrder(3);
					// }else if(which==1){
					// reqOrder(5);
					// }
					// }
					// });
//					Intent intent = new Intent();
//					intent.setClass(context, PayActivity.class);
//					intent.putExtra(Const.KEY_TYPE, PayActivity.FROM_DETAIL);
//					intent.putExtra(Const.KEY_ENTITY, entity);
//					intent.putExtra(Const.KEY_PROMOTION,entity.getIsPromotion());
//					startActivity(intent);
					Log.d("===","listssssss:"+adapter.getPlusPriceList().size());
					if(adapter.getPlusPriceList()!=null &&adapter.getPlusPriceList().size()>0){
						plusGoodsList.clear();
						plusGoodsList.addAll(adapter.getPlusPriceList());
						stringBuffer= new StringBuffer();
						for(int i=0;i<plusGoodsList.size();i++){
							String plusPrice = entity.getId()+"-"+plusGoodsList.get(i);
							stringBuffer.append(plusPrice);
							if(plusGoodsList.size()>1 && i<plusGoodsList.size()-1){
								stringBuffer.append(",");
							}
						}
					}
					Intent intent = new Intent();
					intent.setClass(context,PayActivityNew.class);
					intent.putExtra(Const.KEY_ORDER_INFO,entity.getRetIds());
					if(stringBuffer!=null){
						Log.d("===","StringBuffer"+stringBuffer.toString());
						intent.putExtra(Const.KEY_PLUS_PRICE_BUY,stringBuffer.toString());
					}
					startActivity(intent);
				} else if (v == bookLayout) {
					if(entity.getFlag5()==0){
						DialogUtils.showToast(context, "本课程不支持免费试听");
					}else{
						Intent intent = new Intent();
						intent.setClass(context, PayActivity.class);
						intent.putExtra(Const.KEY_TYPE, PayActivity.FROM_DETAIL);
						intent.putExtra("isbook", true);
						intent.putExtra(Const.KEY_ENTITY, entity);
						startActivity(intent);
					}
					

					// reqBook();
				} else if (v == addBusTv) {
					plusPriceTotalMoney=0.0;
					if(adapter.getPlusPriceList()!=null &&adapter.getPlusPriceList().size()>0){
						plusGoodsList.clear();
						plusGoodsList.addAll(adapter.getPlusPriceList());
						stringBuffer= new StringBuffer();
						for(int i=0;i<plusGoodsList.size();i++){
							for(int j=0;j<entity.getPlusPriceBuyList().size();j++){
								if(	plusGoodsList.get(i).equals(entity.getPlusPriceBuyList().get(j).getId())){
									plusPriceTotalMoney = plusPriceTotalMoney+Double.parseDouble(entity.getPlusPriceBuyList().get(j).getCurentPrice());
								}
							}
							String plusPrice = entity.getId()+"-"+plusGoodsList.get(i);
							stringBuffer.append(plusPrice);
							if(plusGoodsList.size()>1 && i<plusGoodsList.size()-1){
								stringBuffer.append(",");
							}
						}
					}
					if(stringBuffer!=null){
						Log.d("===","stringBuffer:"+stringBuffer.toString());
						entity.setPlus_price_buy(stringBuffer.toString());
						entity.setPlus_price_total_money(String.valueOf(plusPriceTotalMoney));
						entity.setPlus_price_total_num(String.valueOf(plusGoodsList.size()));
					}
					Log.d("===","schoolHour"+adapter.getSchoolHour());
					entity.setSchoolHour(adapter.getSchoolHour());
					DBEntity dbentity = new DBEntity();
					dbentity.setTime(System.currentTimeMillis());
					dbentity.setType(DBEntity.TYPE_COURSE_SHOPPING);
					dbentity.setUid(SharedPrefUtil.getInstance().getUid());
					dbentity.setJsonStr(entity.obj2JsonStr());
					dbentity.setOutId(pid);
					NutClassDB.getInstance(getApplicationContext())
							.addDBEntity(dbentity);
					DialogUtils.showToast(context, "添加成功");
				} else if (v == locationLayout) {
					Util.location(getApplicationContext(),
							new AMapLocationListener() {

								@Override
								public void onLocationChanged(
										AMapLocation amapLocation) {
									// TODO Auto-generated method stub
									if (amapLocation != null) {
										if (amapLocation.getErrorCode() == 0) {
											Intent intent = new Intent(context,
													NavActivity.class);
											intent.putExtra("src_lon", String
													.valueOf(amapLocation
															.getLongitude()));
											intent.putExtra("src_lat", String
													.valueOf(amapLocation
															.getLatitude()));
											intent.putExtra("dst_lon", entity
													.getSchoolEntity().getLon());
											intent.putExtra("dst_lat", entity
													.getSchoolEntity().getLat());
											startActivity(intent);
											Log.e("AmapError", "lon:"
													+ amapLocation.getLongitude() + ", lat:"
													+ amapLocation.getLatitude()+"district:"+amapLocation.getDistrict());
										} else {
											// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
											Log.e("AmapError",
													"location Error, ErrCode:"
															+ amapLocation
																	.getErrorCode()
															+ ", errInfo:"
															+ amapLocation
																	.getErrorInfo());
										}
									}
								}
							});
				}
			}
		}

	}

	/*
	 * 网络请求数据
	 */
	private void reqHeadData() {
		String versionNum = SharedPrefUtil.getInstance().getVersionName();
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.COURSE_DETAIL_URL), pid,
				schoolId,versionNum);
		// String url =
		// String.format(HttpUtil.addBaseGetParams(UrlConst.COURSE_DETAIL_URL),
		// 273,6);
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
						CourseInfoParser parser = new CourseInfoParser();
						JsonResult<BaseCardEntity> result = (JsonResult<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
							CourseCardEntity entity = (CourseCardEntity) result
									.getRetObj();
							if (entity != null) {
								CourseInfoActivity.this.entity = entity;
								list.add(entity);
							}
							if (list != null && list.size() > 0) {
								update(list, 1);
							}
						}
					}
				});
	}

	/*
	 * 网络请求数据
	 */
	private void reqCommentData(final int page) {
		if (page == 1) {
			curPage = 1;
			refreshLayout.setLoadMore(true);
		}
		String url = HttpUtil
				.addBaseGetParams(UrlConst.COURSE_DETAIL_COMMENT_URL) + pid;
		url = url + "&pageNo=" + page;
		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
						refreshLayout.setLoadMore(false);
						refreshLayout.finishRefreshLoadMore();
						refreshLayout.finishRefresh();
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						refreshLayout.finishRefreshLoadMore();
						refreshLayout.finishRefresh();
						CommentParser parser = new CommentParser();
						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = result.getList();
							if (list != null && list.size() > 0) {
								curPage = page;
								update(list, 0);
							} else {
								refreshLayout.setLoadMore(false);
							}
						}
					}
				});

	}

	private static final int SDK_PAY_FLAG = 1;
	private IWXAPI api;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					DialogUtils.showToast(context, "支付成功");
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						DialogUtils.showToast(context, "支付结果确认中");

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						DialogUtils.showToast(context, "支付失败");

					}
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/*
	 * 网络请求数据
	 */
	private void reqOrder(final int type) {
		api = WXAPIFactory.createWXAPI(this, SnsUtil.WEIXIN_APP_ID);
		String url = HttpUtil.addBaseGetParams(UrlConst.COURSE_ORDER_URL)
				+ entity.getRetIds() + "&pay_id=" + type + "&mobile_type=2";
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
							JSONObject jsonData;
							try {
								jsonData = new JSONObject(response);
								JSONObject json = jsonData
										.optJSONObject("data");
								if (type == 3) {// 支付宝支付
									String orderInfo = json
											.optString("orderInfo");
									String sign = json.optString("sign");
									try {
										orderInfo = URLDecoder.decode(
												orderInfo, "utf-8");
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									/**
									 * 完整的符合支付宝参数规范的订单信息
									 */
									final String payInfo = orderInfo
											+ "&sign=\"" + sign + "\"&"
											+ getSignType();

									Runnable payRunnable = new Runnable() {

										@Override
										public void run() {
											// 构造PayTask 对象
											PayTask alipay = new PayTask(
													(Activity) context);
											// 调用支付接口，获取支付结果
											String result = alipay.pay(payInfo,
													true);

											Message msg = new Message();
											msg.what = SDK_PAY_FLAG;
											msg.obj = result;
											mHandler.sendMessage(msg);
										}
									};

									// 必须异步调用
									Thread payThread = new Thread(payRunnable);
									payThread.start();
								} else if (type == 5) {// 微信支付
									if (null != json) {
										PayReq req = new PayReq();
										// req.appId = "wxf8b4f85f3a794e77"; //
										// 测试用appId
										req.appId = json.optString("appid");
										req.partnerId = json
												.optString("partnerid");
										req.prepayId = json
												.optString("prepayid");
										req.nonceStr = json
												.optString("noncestr");
										req.timeStamp = json
												.optString("timestamp");
										req.packageValue = json
												.optString("package");
										req.sign = json.optString("sign");
										req.extData = "app data"; // optional
										Toast.makeText(context, "正常调起支付",
												Toast.LENGTH_SHORT).show();
										// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
										api.sendReq(req);
									} else {
										Log.d("PAY_GET",
												"返回错误"
														+ json.getString("retmsg"));
										Toast.makeText(
												context,
												"返回错误"
														+ json.getString("retmsg"),
												Toast.LENGTH_SHORT).show();
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						DialogUtils.showToast(context, result.getErrorMsg());
					}
				});

	}

	public void update(List<BaseCardEntity> list, int type) {
		if (type == 1) {
			adapter = new CardListAdapter(context, list);
			cardListView.setAdapter(adapter);
			reqCommentData(1);
		} else {
			adapter.addList(list);
		}
	}

	/*
	 * 评论网络请求
	 */
	private void reqComment(String str) {
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.COURSE_COMMENT_URL), pid,
				str);
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
							reqHeadData();
						}
						DialogUtils.showToast(context, result.getErrorMsg());
					}
				});

	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		/** 使用SSO授权必须添加如下代码 */
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

	}
}
