package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ActionInfoCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.parser.ActionInfoParser;
import com.dev.nutclass.parser.CommentParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.Util;
import com.squareup.okhttp.Request;
import com.umeng.socialize.UMShareAPI;

public class ActionInfoActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private final static String TAG = "ActionInfoActivity";
	private ImageView backIv;
	private LinearLayout telLayout;
	private RecyclerView cardListView;
	private LinearLayoutManager mLayoutManager;
	private CardListAdapter adapter;
	private TextView attendTv;
	private TextView likeTv;
	private TextView shareTv;
	private TextView commentTv;
	private ImageView likeIv;
	private LinearLayout likeLayout;
	private LinearLayout commentLayout;
	private ActionInfoCardEntity entity;
	private String pid;

	private MaterialRefreshLayout refreshLayout;
	private int curPage = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ActionInfoActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_action_info);
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
		telLayout = (LinearLayout) findViewById(R.id.ll_location);
		attendTv = (TextView) findViewById(R.id.tv_attend);
		likeTv = (TextView) findViewById(R.id.tv_buy);
		shareTv = (TextView) findViewById(R.id.tv_book);
		commentTv = (TextView) findViewById(R.id.tv_comment);
		likeIv = (ImageView) findViewById(R.id.iv_like);
		likeLayout = (LinearLayout) findViewById(R.id.ll_like);
		commentLayout = (LinearLayout) findViewById(R.id.ll_comment);

		refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
		// 定义刷新
		refreshLayout.setLoadMore(true);
		refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

			@Override
			public void onRefresh(
					final MaterialRefreshLayout materialRefreshLayout) {
				// reqCommentListData(1);
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
				reqCommentListData(curPage + 1);
			}

		});
	}

	private void initIntent() {
		if (getIntent().hasExtra(Const.KEY_ID)) {
			pid = getIntent().getStringExtra(Const.KEY_ID);
		}
		if (TextUtils.isEmpty(pid)) {
			pid = "6";
		}
	}

	private void initData() {
		reqHeadData();
	}

	private void initListener() {
		backIv.setOnClickListener(this);
		telLayout.setOnClickListener(this);
		attendTv.setOnClickListener(this);
		likeLayout.setOnClickListener(this);
		shareTv.setOnClickListener(this);
		commentLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backIv) {
			finish();
		} else if (v == shareTv) {
			SnsUtil.getInstance(context).openShare((Activity) context,entity.getShareEntity());
		} else {
			if (Util.checkLogin(context)) {
				if (v == commentLayout) {
					final EditText et = new EditText(this);
					new AlertDialog.Builder(this)
							.setTitle("请评论活动")
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

				} else if (v == attendTv) {
					if (entity != null && entity.getHasEnroll() == 0) {
						reqAttend();
					} else if (entity != null && entity.getHasEnroll() == 1) {
						DialogUtils.showToast(context, "您已经参与");
					}
				} else if (v == likeLayout) {
					if (entity != null && entity.getHasEnroll() == 0) {
						reqLike();
					} else if (entity != null && entity.getHasEnroll() == 1) {
						DialogUtils.showToast(context, "您已经赞过");
					}
				}
			}
		}
	}

	/*
	 * 网络请求数据
	 */
	private void reqAttend() {
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.ACTION_DETAIL_ATTEND_URL),
				pid);
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
							entity.setHasEnroll(1);
							reqHeadData();
						} else {

						}
						DialogUtils.showToast(context, result.getErrorMsg());
					}
				});

	}

	/*
	 * 网络请求数据
	 */
	private void reqHeadData() {
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.ACTION_DETAIL_URL), pid);
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
						ActionInfoParser parser = new ActionInfoParser();
						JsonResult<BaseCardEntity> result = (JsonResult<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
							ActionInfoCardEntity entity = (ActionInfoCardEntity) result
									.getRetObj();
							if (entity != null) {
								ActionInfoActivity.this.entity = entity;
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
	private void reqCommentListData(final int page) {
		if (page == 1) {
			curPage = 1;
			refreshLayout.setLoadMore(true);
		}
		String url = String
				.format(HttpUtil
						.addBaseGetParams(UrlConst.ACTION_DETAIL_COMMENT_LIST_URL),
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
	 * 赞网络请求
	 */
	private void reqLike() {
		String url = String
				.format(HttpUtil
						.addBaseGetParams(UrlConst.ACTION_DETAIL_LIKE_URL), pid);
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
							entity.setHasPraise(1);
							updateLikeAndComment();
						} else {

						}
						DialogUtils.showToast(context, result.getErrorMsg());
					}
				});

	}

	/*
	 * 评论网络请求
	 */
	private void reqComment(String str) {
		String url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.ACTION_DETAIL_COMMENT_URL),
				pid, str);
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

	public void update(List<BaseCardEntity> list, int type) {
		if (type == 1) {
			adapter = new CardListAdapter(context, list);
			cardListView.setAdapter(adapter);
			updateLikeAndComment();
			reqCommentListData(1);
		} else {
			adapter.addList(list);
		}
	}

	public void updateLikeAndComment() {
		if (entity.getHasPraise() == 0) {
			likeIv.setImageResource(R.drawable.icon_unlike_action);
			if (entity.getPraiseTotal() == 0) {
				likeTv.setText("赞");
			} else {
				likeTv.setText(entity.getPraiseTotal() + "");
			}
		} else {
			likeIv.setImageResource(R.drawable.icon_like_action);
			likeTv.setText(entity.getPraiseTotal() + "");
		}
		if (entity.getCommentNum() == 0) {
			commentTv.setText("评论");
		} else {
			commentTv.setText(entity.getCommentNum() + "");
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		/** 使用SSO授权必须添加如下代码 */
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

	}
}
