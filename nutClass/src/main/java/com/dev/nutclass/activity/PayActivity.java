package com.dev.nutclass.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dev.nutclass.R;
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CooponCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.FQEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.NewestActionEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.parser.CardListParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.DialogUtils.ItemSelectedListener;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.PayResult;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.ChangePriceView;
import com.dev.nutclass.view.FQView;
import com.dev.nutclass.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayActivity extends BaseActivity implements View.OnClickListener {
	private static final String TAG = "PayActivity";
	private Context context;
	/** Called when the activity is first created. */
	private TextView schoolNameTv;
	private TextView courseNameTv;
	private TextView schoolLittleNameTv;
	private TextView courseTimeTv;
	private LinearLayout courseTimeLayout;
	private TextView addressTv;
	private LinearLayout cardLayout;
	private TextView cardNameTv;
	private TextView cardMoneyTv;
	private TextView moneyTv;
	private TextView phoneTv;
	private TextView submitTv;
	private TextView promotiontTv;

	private LinearLayout phoneLayout;
	private LinearLayout couponLayout;

	private TextView payTypeTv;
	private TextView payChangeTv;
	private LinearLayout payTypeLayout;
	private LinearLayout payChangeLayout;

	private TextView cooponTv;

	private RoundedImageView iconIv;
	private LinearLayout schoolTelLayout;
	private TextView schoolTelTv;
	private TextView nameTv;
	private LinearLayout nameLayout;
	private EditText nameEdit;
	private int type = 0;
	private boolean isBook = false;
	private CourseCardEntity entity = null;
	private String orderId = null;
	private String pid = "";
	private ArrayList<BaseCardEntity> cooponList = new ArrayList<BaseCardEntity>();
	private boolean cooponInit = true;
	private int finallyPrice =0 ;
	private int isPromotion;
	// private CourseCardEntity entity;
	public static final int FROM_DETAIL = 100;
	public static final int FROM_ORDER = 200;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_pay);
		initView();
		initIntent();
		initData();
//		initDialog();
		initListener();
	}

	private void initDialog() {
		if (type == FROM_DETAIL&&isPromotion==0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
			final AlertDialog mAlertDialog = builder.create();
			View view = LayoutInflater.from(context).inflate(R.layout.view_price_change, null);
			mAlertDialog.setView(view);
			mAlertDialog.setCanceledOnTouchOutside(false);
			ChangePriceView changePriceView = new ChangePriceView(context);
			changePriceView.updateView(entity, new ChangePriceView.DialogItemClick() {
				@Override
				public void yes() {
					mAlertDialog.cancel();
				}

				@Override
				public void no() {
				}

				@Override
				public void makesure(int priceChanged, float rate) {
					finallyPrice = priceChanged;
					moneyTv.setText(priceChanged + "元");
					Log.d("===","rate:"+rate);
					cardMoneyTv.setText((int)(rate*Integer.parseInt(entity.getPriceEntity().getBackMoney()))+"元");
					mAlertDialog.cancel();
				}


			});
			mAlertDialog.show();
			mAlertDialog.getWindow().setContentView(changePriceView);
		}

	}

	private void initView() {
		schoolNameTv = (TextView) findViewById(R.id.tv_school_name);
		courseNameTv = (TextView) findViewById(R.id.tv_course_name);
		schoolLittleNameTv = (TextView) findViewById(R.id.tv_school_little);
		courseTimeLayout = (LinearLayout) findViewById(R.id.ll_course_time);
		courseTimeTv = (TextView) findViewById(R.id.tv_course_time);
		addressTv = (TextView) findViewById(R.id.tv_school_address);
		cardLayout = (LinearLayout) findViewById(R.id.ll_card);
		cardMoneyTv = (TextView) findViewById(R.id.tv_card_money);
		promotiontTv = (TextView) findViewById(R.id.card_promotion_tv);
		moneyTv = (TextView) findViewById(R.id.tv_money);
		phoneTv = (TextView) findViewById(R.id.tv_phone);

		phoneLayout = (LinearLayout) findViewById(R.id.ll_phone);
		couponLayout = (LinearLayout) findViewById(R.id.ll_coupon);
		submitTv = (TextView) findViewById(R.id.tv_pay);

		cooponTv = (TextView) findViewById(R.id.tv_coopon);

		payTypeTv = (TextView) findViewById(R.id.tv_pay_type);
		payChangeTv = (TextView) findViewById(R.id.tv_pay_change);
		payTypeLayout = (LinearLayout) findViewById(R.id.ll_pay_type);
		payChangeLayout = (LinearLayout) findViewById(R.id.ll_pay_change);

		iconIv = (RoundedImageView) findViewById(R.id.iv_icon);
		schoolTelLayout = (LinearLayout) findViewById(R.id.ll_school_tel);
		schoolTelTv = (TextView) findViewById(R.id.tv_school_tel);
		nameTv = (TextView) findViewById(R.id.tv_info);
		nameLayout = (LinearLayout) findViewById(R.id.ll_name);
		nameEdit = (EditText) findViewById(R.id.edit_name);
	}

	private void initIntent() {
		if (getIntent().hasExtra(Const.KEY_TYPE)) {
			type = getIntent().getIntExtra(Const.KEY_TYPE, 0);
			if (type == FROM_DETAIL) {
				if (getIntent().hasExtra(Const.KEY_ENTITY)) {
					entity = (CourseCardEntity) getIntent()
							.getSerializableExtra(Const.KEY_ENTITY);
					couponLayout.setVisibility(View.VISIBLE);
				}
				if (getIntent().hasExtra("isbook")) {
					isBook = getIntent().getBooleanExtra("isbook", false);
				}
				if(getIntent().hasExtra("is_promotion")){
					isPromotion= getIntent().getIntExtra("is_promotion",0);
				}
			} else if (type == FROM_ORDER) {
				if (getIntent().hasExtra(Const.KEY_ID)) {
					orderId = getIntent().getStringExtra(Const.KEY_ID);
					pid = getIntent().getStringExtra("pid");
				}
			} else {
				finish();
			}
		} else {
			finish();
		}
	}

	private void initData() {
		if (type == FROM_DETAIL) {
			if(isPromotion == 1){
				if(((int)Double.parseDouble(entity.getPriceEntity().getBackMoney()))>0){
					promotiontTv.setText("【促】立减:");
					moneyTv.setText(Integer.parseInt(entity.getPriceEntity().getPrice())-Integer.parseInt(entity.getPriceEntity().getBackMoney())+ "元");
				}else{
					promotiontTv.setText("【促】领券六折:");
					moneyTv.setText(entity.getPriceEntity().getPrice());
				}
			}else{
				moneyTv.setText(entity.getPriceEntity().getPrice());
			}
			schoolNameTv.setText(entity.getCourseName());
			courseNameTv.setText(entity.getCourseName());
			schoolLittleNameTv
					.setText(entity.getSchoolEntity().getSchoolName());
			courseTimeTv.setText(entity.getPriceEntity().getSchoolHour());
			addressTv.setText(entity.getSchoolEntity().getSchoolAddr());
//			cardMoneyTv.setText(entity.getPriceEntity().getBackMoney());
			if((int)Double.parseDouble(entity.getPriceEntity().getBackMoney())>1){
				cardMoneyTv.setText(entity.getPriceEntity().getBackMoney());
			}else{
				cardMoneyTv.setText(entity.getPriceEntity().getBackMoneyStr());
			}
			if (TextUtil.isNotNull(SharedPrefUtil.getInstance().getMobile())) {
				phoneTv.setText(SharedPrefUtil.getInstance().getMobile());
			}

			payTypeLayout.setVisibility(View.VISIBLE);
			payChangeLayout.setVisibility(View.VISIBLE);
			if (entity != null) {
				if (entity.getPayType().equals("3")) {
					payTypeTv.setText("支付宝支付");
				} else if (entity.getPayType().equals("5")) {
					payTypeTv.setText("微信支付");
				} else {
					payTypeTv.setText("信用卡分期");
				}
			}
			if (TextUtil.isNotNull(entity.getSchoolEntity().getTel())) {
				schoolTelTv.setText(entity.getSchoolEntity().getTel());
			} else {
				schoolTelTv.setText("尚未录入");
			}
			if (isBook) {
				schoolNameTv.setVisibility(View.GONE);
				iconIv.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(entity.getIcon(),
						iconIv, ImgConfig.getPortraitOption());
				nameTv.setVisibility(View.VISIBLE);
				nameLayout.setVisibility(View.VISIBLE);
				courseTimeLayout.setVisibility(View.GONE);
				cardLayout.setVisibility(View.GONE);
				couponLayout.setVisibility(View.GONE);
				payTypeLayout.setVisibility(View.GONE);
				payChangeLayout.setVisibility(View.GONE);
				submitTv.setText("提交预约");
			}
		} else {
			reqData();
		}

	}

	private void initListener() {
		phoneLayout.setOnClickListener(this);
		submitTv.setOnClickListener(this);
		cooponTv.setOnClickListener(this);

		payChangeTv.setOnClickListener(this);
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == phoneLayout) {
			if (TextUtil.isNotNull(SharedPrefUtil.getInstance().getMobile())) {
				return;
			}
			final EditText et = new EditText(context);
			et.setText("");
			// 获取ip而已，不用在乎
			new AlertDialog.Builder(context)
					.setTitle("请绑定手机号")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(et)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// 数据获取
									String mobile = et.getText().toString()
											.trim();
									if (TextUtil.isNotNull(mobile)) {
										if ("".equals(mobile)) { // 判断 帐号和密码
											DialogUtils.showToast(context,
													"手机号不能为空");
										} else if (!Util.isMobileNO(mobile)) {
											DialogUtils.showToast(context,
													"手机号错误");
										} else {
											// reqPraise(mobile);
											SharedPrefUtil.getInstance()
													.setMobile(mobile);
											phoneTv.setText(mobile);
										}
									}
								}
							}).setNegativeButton("取消", null).show();
		} else if (v == submitTv) {
			if (!TextUtil.isNotNull(SharedPrefUtil.getInstance().getMobile())) {
				DialogUtils.showToast(context, "请先绑定手机号");
			} else {
				if (isBook) {
					reqBook();
				} else {
					// DialogUtils.showItemsDialog(context, new String[] {
					// "支付宝支付", "微信支付" }, new ItemSelectedListener() {
					//
					// @Override
					// public void onItemSelected(DialogInterface dialog,
					// String text, int which) {
					// // TODO Auto-generated method stub
					// if (which == 0) {
					// reqOrder(3);
					// } else if (which == 1) {
					// reqOrder(5);
					// }
					// }
					// });
					if (FROM_DETAIL == type) {
						reqOrder(Integer.parseInt(entity.getPayType()),"");
					} else {
						reqFQ();
						
					}
				}

			}
		} else if (v == cooponTv) {
			if (cooponInit) {
				reqCooponData();
				cooponInit = false;
			} else {
				updateCoopon();
			}
//			else if (isPromotion == 1){
//				Toast.makeText(this,"促销商品不能使用优惠券",Toast.LENGTH_SHORT).show();
//			}
		} else if (v == payChangeTv) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PayActivity.this);
			final AlertDialog mAlertDialog = builder.create();
			FQView fqView = new FQView(context);
			fqView.updateView(entity.getPayFQList(),1,
					new FQView.DialogItemClick() {

						@Override
						public void ok(String type, int fq1, int fq2) {
							// TODO Auto-generated method stub
							if (type.equals("3")) {
								entity.setPayType("3");
								mAlertDialog.cancel();
								payTypeTv.setText("支付宝支付");
							} else if (type.equals("5")) {
								entity.setPayType("5");
								mAlertDialog.cancel();
								payTypeTv.setText("微信支付");
							} else {
								payTypeTv.setText("信用卡分期");
								entity.setPayType(entity.getPayFQList()
										.get(fq1).getId());
								entity.setPayFQ(entity.getPayFQList().get(fq1)
										.getList().get(fq2).getId());
								mAlertDialog.cancel();
							}

						}

						@Override
						public void cancel() {
							// TODO Auto-generated method stub
							mAlertDialog.cancel();
						}
					});
			mAlertDialog.show();
			mAlertDialog.getWindow().setContentView(fqView);

			
		}
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
	 * 网络请求数据 { "status": 1, "info": "获取成功", "data": { "order_amount":
	 * "1998.00", "order_sn": "2016030779236", "mobile_phone": "18911206508",
	 * "goods_list": [ { "goods_name": "聚能教育畅学无忧精品套课", "xiaoqu_name":
	 * "聚能教育集团总部", "attr_value": "10课时", "xiaoqu_addr": "北京市海淀区中关村大街1号海龙大厦9层",
	 * "back_type": "京东", "back_money": "100.00元" } ] } }
	 */
	private void reqData() {
		submitTv.setVisibility(View.INVISIBLE);
		String url = HttpUtil.addBaseGetParams(String.format(
				UrlConst.COURSE_ORDER_INFO_BY_ORDERID_URL, orderId));
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
						JSONObject baseJson;
						try {
							baseJson = new JSONObject(response);
							if (baseJson != null
									&& baseJson.optInt("status", 0) == 1) {
								JSONObject dataJson = baseJson
										.optJSONObject("data");

								schoolNameTv.setText(dataJson
										.optString("goods_name"));
								courseNameTv.setText(dataJson
										.optString("goods_name"));
								schoolLittleNameTv.setText(dataJson
										.optString("xiaoqu_name"));
								courseTimeTv.setText(dataJson
										.optString("attr_value"));
								addressTv.setText(dataJson
										.optString("xiaoqu_name"));
//								cardNameTv.setText(dataJson
//										.optString("back_type"));
								cardMoneyTv.setText(dataJson
										.optString("back_money"));
								moneyTv.setText(dataJson
										.optString("order_amount"));
								schoolTelLayout.setVisibility(View.GONE);
								if (TextUtil.isNotNull(dataJson
										.optString("mobile_phone"))) {
									SharedPrefUtil.getInstance().setMobile(
											dataJson.optString("mobile_phone"));
									phoneTv.setText(SharedPrefUtil
											.getInstance().getMobile());
								} else if (TextUtil.isNotNull(SharedPrefUtil
										.getInstance().getMobile())) {
									phoneTv.setText(SharedPrefUtil
											.getInstance().getMobile());
								} else {
									phoneTv.setText("");
								}
								 
								submitTv.setVisibility(View.VISIBLE);
							} else {
								finish();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	/*
	 * 网络请求数据
	 */
	private void reqOrder(final int type,final String pay_fq) {
		 

			api = WXAPIFactory.createWXAPI(getApplicationContext(),
					SnsUtil.WEIXIN_APP_ID);

			String url = null;
			if (this.type == FROM_DETAIL) {
				url = HttpUtil.addBaseGetParams(String.format(
						UrlConst.COURSE_ORDER_NEW_URL, entity.getRetIds(),
						SharedPrefUtil.getInstance().getMobile(), type,
						entity.getPayFQ(), cooponId));
			} else {
				url = HttpUtil.addBaseGetParams(String.format(
						UrlConst.COURSE_ORDER_BY_ORDERID_URL, orderId, type,pay_fq));
			}
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
											// sign = URLDecoder.decode(
											// sign, "utf-8");
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
												String result = alipay.pay(
														payInfo, true);

												Message msg = new Message();
												msg.what = SDK_PAY_FLAG;
												msg.obj = result;
												mHandler.sendMessage(msg);
											}
										};

										// 必须异步调用
										Thread payThread = new Thread(
												payRunnable);
										payThread.start();
									} else if (type == 5) {// 微信支付
										if (null != json) {
											PayReq req = new PayReq();
											// req.appId = "wxf8b4f85f3a794e77";
											// // 测试用appId
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
											// api.registerApp(SnsUtil.WEIXIN_APP_ID);
											boolean a = api.sendReq(req);
											Log.d("PAY_GET", "a=" + a);
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
									} else {
										DialogUtils.showToast(context,
												jsonData.optString("info"));
										String url = jsonData.optString("data");
										Intent intent = new Intent(context,
												WebViewActivity.class);
										intent.putExtra(Const.KEY_FROM,"1");
										intent.putExtra(Const.KEY_URL, url);
										intent.putExtra(Const.KEY_TITLE,
												"信用卡支付");
										context.startActivity(intent);
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

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/*
	 * 预约课程请求
	 */
	private void reqBook() {
		String url = String.format(HttpUtil
				.addBaseGetParams(UrlConst.COURSE_BOOK_URL), entity.getId(),
				entity.getSchoolEntity().getId(), SharedPrefUtil.getInstance()
						.getMobile(), nameEdit.getText().toString());
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
						DialogUtils.showToast(context, result.getErrorMsg());
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							finish();
						}

					}
				});
	}

	private void reqCooponData() {
		String url = UrlConst.GET_COOPON_LIST + "&order_list="
				+ entity.getRetIds();

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
						CardListParser parser = new CardListParser();
						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							cooponList = result.getList();
							updateCoopon();

						}
					}
				});
	}

	private void updateCoopon() {
		if (cooponList != null && cooponList.size() > 0) {
			String[] cooponName = new String[cooponList.size() + 1];
			cooponName[0] = "不使用优惠券";
			for (int i = 0; i < cooponList.size(); i++) {
				cooponName[i + 1] = ((CooponCardEntity) cooponList.get(i))
						.getName();
			}
			DialogUtils.showItemsDialog(context, cooponName,
					new ItemSelectedListener() {

						@Override
						public void onItemSelected(DialogInterface dialog,
								String text, int which) {
							// TODO Auto-generated method stub
							updateUI(which);

						}
					});
		} else {

			DialogUtils.showToast(context, "您暂时没有优惠券");
		}

	}

	private String cooponId = "";

	private void updateUI(int index) {
		if (index == 0) {
			cooponId = "";
			cooponTv.setText("请选择优惠券");
			if(((int)Double.parseDouble(entity.getPriceEntity().getBackMoney()))>0){
				moneyTv.setText(Integer.parseInt(entity.getPriceEntity().getPrice())-Integer.parseInt(entity.getPriceEntity().getBackMoney())+ "元");
			}else{
				moneyTv.setText(entity.getPriceEntity().getPrice()+"元");
			}


		} else {
			cooponId = ((CooponCardEntity) cooponList.get(index - 1)).getId();
			cooponTv.setText(((CooponCardEntity) cooponList.get(index - 1))
					.getName());
//			if(finallyPrice!=0){
//				moneyTv.setText(String.valueOf(Double.parseDouble(entity.getPriceEntity().getPrice())
//						- Double.parseDouble(((CooponCardEntity) cooponList
//						.get(index - 1)).getMoney()))
//						+ "元");
//			}else{
				if(((CooponCardEntity) cooponList.get(index-1)).getBonus_type().equals("1")){
					Log.d("===","money:"+((CooponCardEntity)cooponList
							.get(index - 1)).getMoney());
					moneyTv.setText((int)(Double.parseDouble(entity.getPriceEntity().getPrice())-((int)Double.parseDouble(entity.getPriceEntity().getBackMoney()))
                                                - Double.parseDouble(((CooponCardEntity) cooponList
                                                .get(index - 1)).getMoney()))+"元");
				}else if(((CooponCardEntity) cooponList.get(index-1)).getBonus_type().equals("2")){
					moneyTv.setText((int)(Double.parseDouble(entity.getPriceEntity().getPrice())*Double.parseDouble(((CooponCardEntity) cooponList
                                                .get(index - 1)).getMoney()))+"元");
				}
//				(String.valueOf(finallyPrice)+"元");
//				moneyTv.setText(String.valueOf(Double.parseDouble(entity.getPriceEntity().getPrice())
//						- Double.parseDouble(((CooponCardEntity) cooponList
//						.get(index - 1)).getMoney()))
//						+ "元");
			}

		}

	// 通过att以及goods请求分期信息
	private void reqFQ() {
		String url = HttpUtil.addBaseGetParams(String.format(
				UrlConst.GET_FQ_LIST, pid, "",orderId));
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
						JSONObject baseJson;
						try {
							baseJson = new JSONObject(response);
							if (baseJson != null
									&& baseJson.optInt("status", 0) == 1) {
								JSONArray dataJson = baseJson
										.optJSONArray("data");
								if (dataJson != null && dataJson.length() > 0) {
									final List<FQEntity> fqList = new ArrayList<FQEntity>();
									for (int i = 0; i < dataJson.length(); i++) {
										FQEntity fqEntity = new FQEntity(
												dataJson.optJSONObject(i));
										fqList.add(fqEntity);
									}
									AlertDialog.Builder builder = new AlertDialog.Builder(
											PayActivity.this);
									final AlertDialog mAlertDialog = builder
											.create();
									FQView fqView = new FQView(context);
									fqView.updateView(fqList, 1,
											new FQView.DialogItemClick() {

												@Override
												public void ok(String type,
														int fq1, int fq2) {
													// TODO Auto-generated
													// method stub
													if (type.equals("3")) {
														//entity.setPayType("3");
														mAlertDialog.cancel();
														payTypeTv.setText("支付宝支付");
														reqOrder(Integer.parseInt(type),"");
													} else if (type.equals("5")) {
														//entity.setPayType("5");
														mAlertDialog.cancel();
														payTypeTv.setText("微信支付");
														reqOrder(Integer.parseInt(type),"");
													} else {
														payTypeTv.setText("信用卡分期");

														reqOrder(Integer.parseInt(fqList.get(fq1).getId()),fqList.get(fq1).getList().get(fq2).getId());
														mAlertDialog.cancel();
													}
													
												}

												@Override
												public void cancel() {
													// TODO Auto-generated
													// method stub
													mAlertDialog.cancel();
												}
											});
									mAlertDialog.show();
									mAlertDialog.getWindow().setContentView(
											fqView);

								} else {
									DialogUtils.showToast(context,
											baseJson.optString("info"));
								}

							} else {
								DialogUtils.showToast(context,
										baseJson.optString("info"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}
}
