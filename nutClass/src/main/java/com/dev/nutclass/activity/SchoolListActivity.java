package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.SingleItemCardEntity;
import com.dev.nutclass.parser.BaseParser;
import com.dev.nutclass.parser.BrandDetailListParser;
import com.dev.nutclass.parser.CourseListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class SchoolListActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private final static String TAG = "CourseListActivity";
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
	private RecyclerView filter2ListView;
	private LinearLayout filterListLayout;

	private CardListAdapter filterAdapter;
	private CardListAdapter filter2Adapter;
	private CardListAdapter adapter;
	private String cid = "";
	private int curPage = 1;
	private int showFilter = 0;// 0:全不显示 1：显示城市 2：显示价格

	private int type = 0;// 判断来源
	public static final int TYPE_FROM_BUY = 0;
	public static final int TYPE_FROM_BOOK = 1;
	public static final int TYPE_FROM_BRAND = 2;
	public static final int TYPE_FROM_CATEGORY = 3;

	private RecyclerItemClickListener itemClickListener1;
	private RecyclerItemClickListener itemClickListener2;
	private RecyclerItemClickListener itemClickListener3;

	private MaterialRefreshLayout refreshLayout;

	private String distinctId = "";
	private String priceId = "0";

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

		leftLayout = (LinearLayout) findViewById(R.id.ll_left);
		leftTv = (TextView) findViewById(R.id.tv_name_left);
		leftIv = (ImageView) findViewById(R.id.iv_arrow_left);
		rightLayout = (LinearLayout) findViewById(R.id.ll_right);
		rightTv = (TextView) findViewById(R.id.tv_name_right);
		rightIv = (ImageView) findViewById(R.id.iv_arrow_right);
		cardListView = (RecyclerView) findViewById(R.id.card_list);

		filterLayout = (LinearLayout) findViewById(R.id.ll_filter);
		filterListView = (RecyclerView) findViewById(R.id.filter_list);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		filterListView.setLayoutManager(mLayoutManager);

		LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(context);
		mLayoutManager2.setOrientation(LinearLayout.VERTICAL);
		filter2ListView = (RecyclerView) findViewById(R.id.filter_list2);
		filter2ListView.setLayoutManager(mLayoutManager2);

		filterListLayout = (LinearLayout) findViewById(R.id.ll_filter_list);
		
		LinearLayoutManager rootManager = new LinearLayoutManager(context);
		rootManager.setOrientation(LinearLayout.VERTICAL);
		cardListView.setLayoutManager(rootManager);
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
		if (getIntent().hasExtra(Const.KEY_ID)) {
			cid = getIntent().getStringExtra(Const.KEY_ID);
		}

	}

	private void initData() {
		switch (type) {
		case TYPE_FROM_BOOK:
			titleBar.setMiddleText("预约试听");
			titleBar.setTitleRight1(TitleBar.TYPE_SEARCH_IMG);

			break;
		case TYPE_FROM_BUY:
			titleBar.setMiddleText("购买课程");
			titleBar.setTitleRight1(TitleBar.TYPE_SEARCH_IMG);
			break;
		case TYPE_FROM_BRAND:
			titleBar.setMiddleText("品牌馆");
			filterLayout.setVisibility(View.GONE);
			break;
		case TYPE_FROM_CATEGORY:
			titleBar.setMiddleText("分类");
			titleBar.setTitleRight1(TitleBar.TYPE_SEARCH_IMG);
			break;

		default:
			break;
		}
		reqData(1);
	}

	private void initListener() {
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
		itemClickListener1 = new RecyclerItemClickListener() {

			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, entity.getDesc());
				// 此处点击重新请求
				updateSecondFilter(entity.getId());
			}

			 
		};
		itemClickListener2 = new RecyclerItemClickListener() {

			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, "重新请求" + entity.getDesc());
				// 此处点击重新请求
				showFilter=0;
				distinctId=entity.getId();
				updateFilter();
				reqData(1);
			}

			 
		};
		itemClickListener3 = new RecyclerItemClickListener() {

			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, "重新请求" + entity.getDesc());
				// 此处点击重新请求
				showFilter=0;
				priceId=entity.getId();
				updateFilter();
				reqData(1);

			}

			
		};
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {

			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SearchActivity.class);
				startActivity(intent);
				return false;
			}

			@Override
			public boolean onClickMiddle() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickLeft() {
				// TODO Auto-generated method stub
				return false;
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
		if (leftLayout == v) {
			if (showFilter == 1) {
				showFilter = 0;
			} else {
				showFilter = 1;
			}
			updateFilter();
		} else if (rightLayout == v) {
			if (showFilter == 2) {
				showFilter = 0;
			} else {
				showFilter = 2;
			}
			updateFilter();
		}
	}

	public void update(List<BaseCardEntity> list) {
		if (curPage == 1) {
			adapter = new CardListAdapter(context, list);
			cardListView.setAdapter(adapter);
		} else {
			adapter.addList(list);
		}
	}

	/*
	 * 网络请求数据
	 */
	private void reqData(final int page) {
		if (page == 1) {
			curPage=1;
			refreshLayout.setLoadMore(true);
		}
		String url = null;
		String versionNum = SharedPrefUtil.getInstance().getVersionName();
		url = String.format(
				HttpUtil.addBaseGetParams(UrlConst.GET_SCHOOL), cid,versionNum);
		url = url +"&pageNo=" + page;

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
						if (type == TYPE_FROM_BRAND) {
							parser = new BrandDetailListParser();
						} else {
							parser = new CourseListParser();
						}
						JsonDataList<BaseCardEntity> result;
						try {
							result = (JsonDataList<BaseCardEntity>) parser
									.parse(response);
							if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
								ArrayList<BaseCardEntity> list = result
										.getList();
								if (list != null && list.size() > 0) {
									curPage = page;
									update(list);
								} else {
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

	private void updateFilter() {
		rightIv.setImageResource(R.drawable.tab_arrow_down);
		leftIv.setImageResource(R.drawable.tab_arrow_down);
		if (showFilter == 0) {
			filterListLayout.setVisibility(View.GONE);
		} else if (showFilter == 1) {
			leftIv.setImageResource(R.drawable.tab_arrow_up);
			filterListLayout.setVisibility(View.VISIBLE);
			filter2ListView.setVisibility(View.VISIBLE);
			filterAdapter = new CardListAdapter(context, Util.regionList,
					itemClickListener1);
			filterListView.setAdapter(filterAdapter);

			filter2Adapter = new CardListAdapter(
					context,
					Util.regionSecondList
							.get(((SingleItemCardEntity) Util.regionList.get(0))
									.getId()), itemClickListener2);
			filter2ListView.setAdapter(filter2Adapter);
		} else if (showFilter == 2) {
			rightIv.setImageResource(R.drawable.tab_arrow_up);
			filterListLayout.setVisibility(View.VISIBLE);
			filter2ListView.setVisibility(View.GONE);
			filterAdapter = new CardListAdapter(context, Util.priceList,
					itemClickListener3);
			filterListView.setAdapter(filterAdapter);
		}
	}

	private void updateSecondFilter(String pid) {
		filter2Adapter = new CardListAdapter(context,
				Util.regionSecondList.get(pid), itemClickListener2);
		filter2ListView.setAdapter(filter2Adapter);
	}
}
