package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dev.nutclass.R;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.SingleItemCardEntity;
import com.dev.nutclass.parser.BaseParser;
import com.dev.nutclass.parser.MeCourseListParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class CardListActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private final static String TAG = "CardListActivity";
	private TitleBar titleBar;
	private LinearLayout filterLayout;
	private LinearLayout leftLayout;
	private TextView leftTv;
	private ImageView leftIv;
	private LinearLayout rightLayout;
	private TextView rightTv;
	private ImageView rightIv;
	private RecyclerView cardListView;
	private RecyclerView filterListView;

	private CardListAdapter filterAdapter;
	private CardListAdapter adapter;
	private int curPage = 1;
	private int showFilter = 0;// 0:全不显示 1：显示城市 2：显示价格
	private String filter1 = "";
	private String filter2 = "";

	private int type = 0;// 判断来源
	public static final int TYPE_FROM_WAIT_PAY = 1000;
	public static final int TYPE_FROM_BOOK = 1001;
	public static final int TYPE_FROM_WAIT_COMMENT = 1002;
	public static final int TYPE_FROM_ZERO = 1003;
	public static final int TYPE_FROM_ALL_ORDER = 1004;
	public static final int TYPE_FROM_MARKET = 1005;
	public static final int TYPE_FROM_SHOPPING = 1006;

	private RecyclerItemClickListener itemClickListener;
	
	private MaterialRefreshLayout refreshLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courselist);
		context = this;
		initView();
		initIntent();
		initData();
		initListener();
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		filterLayout = (LinearLayout) findViewById(R.id.ll_filter);
		leftLayout = (LinearLayout) findViewById(R.id.ll_left);
		leftTv = (TextView) findViewById(R.id.tv_name_left);
		leftIv = (ImageView) findViewById(R.id.iv_arrow_left);
		rightLayout = (LinearLayout) findViewById(R.id.ll_right);
		rightTv = (TextView) findViewById(R.id.tv_name_right);
		rightIv = (ImageView) findViewById(R.id.iv_arrow_right);
		cardListView = (RecyclerView) findViewById(R.id.card_list);
		filterListView = (RecyclerView) findViewById(R.id.filter_list);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		filterListView.setLayoutManager(mLayoutManager);
		
		LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(context);
		mLayoutManager1.setOrientation(LinearLayout.VERTICAL);
		cardListView.setLayoutManager(mLayoutManager1);
		titleBar.removeTitleRight1();
		
		
		refreshLayout = (MaterialRefreshLayout)findViewById(R.id.refresh);
		// 定义刷新
		refreshLayout.setLoadMore(true);
		refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(
					final MaterialRefreshLayout materialRefreshLayout) {
				reqData(1);
			}

			@Override
			public void onfinish() {
				// Toast.makeText(getActivity(), "刷新完成",
				// Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void onRefreshLoadMore(
					final MaterialRefreshLayout materialRefreshLayout) {
				reqData(curPage + 1);
			}

		});
	}

	private void initIntent() {
		if (getIntent().hasExtra(Const.KEY_TYPE)) {
			type = getIntent().getIntExtra(Const.KEY_TYPE, 0);
		}

	}

	private void initData() {
		filterLayout.setVisibility(View.GONE);
		switch (type) {
		case TYPE_FROM_BOOK:
			titleBar.setMiddleText("预约试听");
			break;
		case TYPE_FROM_WAIT_PAY:
			titleBar.setMiddleText("待付款");
			break;
		case TYPE_FROM_WAIT_COMMENT:
			titleBar.setMiddleText("待评论");
			break;
		case TYPE_FROM_ZERO:
			titleBar.setMiddleText("0元抢");
			break;
		case TYPE_FROM_ALL_ORDER:
			titleBar.setMiddleText("全部订单");
			break;
		case TYPE_FROM_MARKET:
			titleBar.setMiddleText("我的发布");
			break;

		default:
			break;
		}
		reqData(1);
	}

	private void initListener() {
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
		itemClickListener = new RecyclerItemClickListener() {

			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				if(obj instanceof CourseCardEntity){
					CourseCardEntity entity=(CourseCardEntity)obj;
					if (type == TYPE_FROM_WAIT_COMMENT) {
						comment(entity.getOrderSn());
					}else if(type==TYPE_FROM_WAIT_PAY){
						reqOrder(entity.getOrderId(),entity.getId());
					}else if(type==TYPE_FROM_ALL_ORDER){
						if(TextUtil.isNotNull(entity.getOrderStatus())&&entity.getOrderStatus().equals(CourseCardEntity.STATUS_ORDER_1)){
							reqOrder(entity.getOrderId(),entity.getId());
						}else{
							comment(entity.getOrderSn());
						}
					}
				}
				
			}

			 
		};
	}
	private void comment(final String orderNo){
		final EditText et = new EditText(context);
		et.setText("");
		// 获取ip而已，不用在乎
		new AlertDialog.Builder(context)
				.setTitle("请评价")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(et)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface arg0, int arg1) {
								// 数据获取
								String str=et.getText().toString();
								if(TextUtils.isEmpty(str)){
									DialogUtils.showToast(context, "评价内容不能为空");												
								}else{
									reqComment(str,orderNo);
								}
							}
						}).setNegativeButton("取消", null).show();
	}
	private void reqOrder(String orderId,String pid){
		Intent intent=new Intent();
		intent.setClass(context, PayActivity.class);
		intent.putExtra(Const.KEY_TYPE, PayActivity.FROM_ORDER);
		intent.putExtra(Const.KEY_ID, orderId);
		intent.putExtra("pid", pid);
		startActivity(intent);
	}

	/*
	 * 评论网络请求
	 */
	private void reqComment(String str,String orderNo) {
		String url = String.format(HttpUtil
				.addBaseGetParams(UrlConst.USERINFO_COMMENT_URL),orderNo, str);
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
							reqData(1);
						} else {

						}
						DialogUtils.showToast(context, result.getErrorMsg());
					}
				});
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		if (leftLayout == v) {
//			if (showFilter == 1) {
//				showFilter = 0;
//			} else {
//				showFilter = 1;
//			}
//			updateFilter();
//		} else if (rightLayout == v) {
//			if (showFilter == 2) {
//				showFilter = 0;
//			} else {
//				showFilter = 2;
//			}
//			updateFilter();
//		}
	}

	public void update(List<BaseCardEntity> list) {
		if (curPage == 1) {
			adapter = new CardListAdapter(context, list,itemClickListener);
			cardListView.setAdapter(adapter);
		} else {
			adapter.addList(list);
		}
	}

	private String getUrl() {
		String url = "";
		switch (type) {
		case TYPE_FROM_WAIT_PAY:
			url = UrlConst.USERINFO_WAIT_PAY_URL;
			break;
		case TYPE_FROM_BOOK:
			url = UrlConst.USERINFO_BOOK_URL;
			break;
		case TYPE_FROM_WAIT_COMMENT:
			url = UrlConst.USERINFO_WAIT_COMMENT_URL;
			break;
		case TYPE_FROM_ZERO:
			url = UrlConst.USERINFO_ZERO_URL;
			break;
		case TYPE_FROM_ALL_ORDER:
			url = UrlConst.USERINFO_ALL_ORDER_URL;
			break;
		case TYPE_FROM_MARKET:
			url = UrlConst.USERINFO_RELEASE_URL;
			break;

		default:
			break;
		}
		return url;
	}

	/*
	 * 网络请求数据
	 */
	private void reqData(final int page) {
		if (page == 1) {
			curPage=1;
			refreshLayout.setLoadMore(false);
		}
		refreshLayout.setLoadMore(false);
		String url = getUrl();
		if (TextUtils.isEmpty(url)) {
			return;
		}
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
						BaseParser<BaseCardEntity> parser = null;
						// if (type ==
						// TYPE_FROM_WAIT_PAY||type==TYPE_FROM_BOOK||type==TYPE_FROM_WAIT_COMMENT)
						// {
						// parser = new MeCourseListParser(type);
						// } else {
						// parser = new ZeroListParser();
						// }
						curPage=page;
						parser = new MeCourseListParser(type);
						JsonDataList<BaseCardEntity> result;
						try {
							result = (JsonDataList<BaseCardEntity>) parser
									.parse(response);
							if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
								ArrayList<BaseCardEntity> list = result
										.getList();
								if (list != null && list.size() > 0) {
									update(list);
									if(list.size()<10){
										refreshLayout.setLoadMore(false);
									}
								}else {
									cardListView.setVisibility(View.INVISIBLE);
									refreshLayout.setLoadMore(false);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	public void refreshAdapter(){
		reqData(1);
	}
//	private void updateFilter() {
//		rightIv.setImageResource(R.drawable.tab_arrow_down);
//		leftIv.setImageResource(R.drawable.tab_arrow_down);
//		if (showFilter == 0) {
//			filterListView.setVisibility(View.GONE);
//		} else if (showFilter == 1) {
//			leftIv.setImageResource(R.drawable.tab_arrow_up);
//			filterListView.setVisibility(View.VISIBLE);
//			filterAdapter = new CardListAdapter(context, Util.regionList,
//					itemClickListener);
//			filterListView.setAdapter(filterAdapter);
//		} else if (showFilter == 2) {
//			rightIv.setImageResource(R.drawable.tab_arrow_up);
//			filterListView.setVisibility(View.VISIBLE);
//			filterAdapter = new CardListAdapter(context, Util.priceList,
//					itemClickListener);
//			filterListView.setAdapter(filterAdapter);
//		}
//	}
}
