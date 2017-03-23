package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.db.NutClassDB;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.DBEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.MarketInfoCardEntity;
import com.dev.nutclass.parser.CommentParser;
import com.dev.nutclass.parser.MarketInfoParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.squareup.okhttp.Request;

public class MarketInfoActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private final static String TAG = "MarketInfoActivity";
	private ImageView backIv;
	private RecyclerView cardListView;
	private LinearLayoutManager mLayoutManager;
	private CardListAdapter adapter;
	private MarketInfoCardEntity entity;
	private String pid;
	private TextView commentTv;

	private MaterialRefreshLayout refreshLayout;
	private int curPage = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = MarketInfoActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_market_info);
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
		cardListView = (RecyclerView) findViewById(R.id.card_list);
		mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		cardListView.setLayoutManager(mLayoutManager);
		backIv = (ImageView) findViewById(R.id.iv_back);
		commentTv = (TextView) findViewById(R.id.tv_comment);

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
	}

	private void initIntent() {
		if (getIntent().hasExtra(Const.KEY_ID)) {
			pid = getIntent().getStringExtra(Const.KEY_ID);
		}
		if (TextUtils.isEmpty(pid)) {
			pid = "11";
		}
	}

	private void initData() {
		reqHeadData();
	}

	private void initListener() {
		backIv.setOnClickListener(this);
		commentTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backIv) {
			finish();
		} else {
			if (Util.checkLogin(context)) {
				if (v == commentTv) {
					final EditText et = new EditText(this);
					new AlertDialog.Builder(this)
							.setTitle("请评论商品")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(et)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											if (TextUtils.isEmpty(et.getText()
													.toString())) {
												DialogUtils.showToast(context,
														"评论不能为空");
												return;
											}
											// 数据获取
											reqComment(et.getText().toString());
										}
									}).setNegativeButton("取消", null).show();

				}
			}
		}
	}

	/*
	 * 网络请求数据
	 */
	private void reqHeadData() {
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.MARKET_INFO_URL), pid);
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
						MarketInfoParser parser = new MarketInfoParser();
						JsonResult<BaseCardEntity> result = (JsonResult<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
							MarketInfoCardEntity entity = (MarketInfoCardEntity) result
									.getRetObj();
							if (entity != null) {
								MarketInfoActivity.this.entity = entity;
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

		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.MARKET_COMMENT_LIST_URL),
				pid);
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

	/*
	 * 评论网络请求
	 */
	private void reqComment(String str) {
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.MARKET_COMMENT_URL), pid,
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
						} else {

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
}
