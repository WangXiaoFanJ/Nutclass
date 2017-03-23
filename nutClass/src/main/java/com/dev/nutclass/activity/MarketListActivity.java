package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.dev.nutclass.parser.MarketListParser;
import com.dev.nutclass.parser.ZeroListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class MarketListActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private final static String TAG = "MarketListActivity";
	private TitleBar titleBar;
	private LinearLayout leftLayout;
	private TextView leftTv;
	private ImageView leftIv;
	private LinearLayout other1Layout;
	private TextView other1Tv;
	private ImageView other1Iv;
	private LinearLayout other2Layout;
	private TextView other2Tv;
	private ImageView other2Iv;
	private LinearLayout rightLayout;
	private TextView rightTv;
	private ImageView rightIv;
	private RecyclerView cardListView;
	private LinearLayout filterLayout;
	private CardListAdapter adapter;
	private String cid = "";
	private int curPage = 1;
	
	private RecyclerView filterListView;
	private RecyclerView filter2ListView;
	private LinearLayout filterListLayout;

	private CardListAdapter filterAdapter;
	private CardListAdapter filter2Adapter;
	private int showFilter = 0;// 0:全不显示 1：显示城市 2：显示价格
	private RecyclerItemClickListener itemClickListener1;//区
	private RecyclerItemClickListener itemClickListener2;//区下面的小区
	private RecyclerItemClickListener itemClickListener3;//分类
	private RecyclerItemClickListener itemClickListener4;//价格
	private RecyclerItemClickListener itemClickListener5;//个人、商铺

	
	private MaterialRefreshLayout refreshLayout;

	private String distinctId = "";
	private String pid = "";
	private String scope = "";
	private String priceId = "0";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_cardlist);
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
		other1Layout = (LinearLayout) findViewById(R.id.ll_other1);
		other1Tv = (TextView) findViewById(R.id.tv_name_other1);
		other1Iv = (ImageView) findViewById(R.id.iv_arrow_other1);
		other2Layout = (LinearLayout) findViewById(R.id.ll_other2);
		other2Tv = (TextView) findViewById(R.id.tv_name_other2);
		other2Iv = (ImageView) findViewById(R.id.iv_arrow_other2);
		cardListView = (RecyclerView) findViewById(R.id.card_list);
		other2Layout.setVisibility(View.VISIBLE);
		other1Layout.setVisibility(View.VISIBLE);
		
		filterListView = (RecyclerView) findViewById(R.id.filter_list);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		filterListView.setLayoutManager(mLayoutManager);
		
		LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(context);
		mLayoutManager2.setOrientation(LinearLayout.VERTICAL);
		filter2ListView = (RecyclerView) findViewById(R.id.filter_list2);
		filter2ListView.setLayoutManager(mLayoutManager2);
		
		filterListLayout=(LinearLayout)findViewById(R.id.ll_filter_list);
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
		if (getIntent().hasExtra(Const.KEY_ID)) {
			cid = getIntent().getStringExtra(Const.KEY_ID);
		}

	}

	private void initData() {
		titleBar.setMiddleText("跳蚤市场");
		if(Util.getCategoryList()==null){
			titleBar.removeTitleRight1();
		}else{
			titleBar.setTitleRight1(TitleBar.TYPE_RELEASE_IMG);
		}
		reqData(1);

	}

	private void initListener() {
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
		other1Layout.setOnClickListener(this);
		other2Layout.setOnClickListener(this);
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {

			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
//				DialogUtils.showToast(context, "发布图正在出。。。");
				if(Util.checkLogin(context)){
					Intent intent=new Intent(context,MarketReleaseActivity.class);
					startActivity(intent);
				}
				return true;
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
		itemClickListener1 = new RecyclerItemClickListener() {

			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, entity.getDesc());
				for (int i = 0; i < Util.regionList.size(); i++) {
					if(entity.getId().equals(((SingleItemCardEntity)(Util.regionList.get(i))).getId())){
						((SingleItemCardEntity)(Util.regionList.get(i))).setSelected(true);
					}else{
						((SingleItemCardEntity)(Util.regionList.get(i))).setSelected(false);
					}
				}
				if(filterAdapter!=null){
					filterAdapter.notifyDataSetChanged();
				}
				//此处点击重新请求
				updateSecondFilter(entity.getId());
			}

			
		};
		itemClickListener2 = new RecyclerItemClickListener() {
			
			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, "重新请求"+entity.getDesc());
				//此处点击重新请求
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
				DialogUtils.showToast(context, "重新请求"+entity.getDesc());
				//此处点击重新请求
				showFilter=0;
				cid=entity.getId();
				updateFilter();
				reqData(1);
				
			}
			
			
		};
		itemClickListener4 = new RecyclerItemClickListener() {
			
			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, "重新请求"+entity.getDesc());
				//此处点击重新请求
				showFilter=0;
				priceId=entity.getId();
				updateFilter();
				reqData(1);
				
			}
			
			
		};
		itemClickListener5 = new RecyclerItemClickListener() {
			
			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, "重新请求"+entity.getDesc());
				//此处点击重新请求
				showFilter=0;
				scope=entity.getId();
				updateFilter();
				reqData(1);
				
			}
			
			
		};
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
		} else if (other1Layout == v) {
			if (showFilter == 3) {
				showFilter = 0;
			} else {
				showFilter = 3;
			}
			updateFilter();
		} else if (other2Layout == v) {
			if (showFilter == 4) {
				showFilter = 0;
			} else {
				showFilter = 4;
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
		//cid=2&district_id=1&order_price=1&is_personal=1
		String url = HttpUtil
				.addBaseGetParams(UrlConst.MARKET_URL);
		url = url + "&cid=" + cid +"&is_personal=" + scope +"&district_id=" + distinctId + "&order_price="
				+ priceId + "&pageNo=" + page;
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
						MarketListParser parser = new MarketListParser();
						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = result.getList();
							if (list != null && list.size() > 0) {
								curPage=page;
								update(list);
							}else {
								if(page==1&&adapter!=null){
									curPage=1;
									adapter.getList().clear();
									adapter.notifyDataSetChanged();
								}
								refreshLayout.setLoadMore(false);
							}
						}
					}
				});
	}
	private void updateFilter() {
		rightIv.setImageResource(R.drawable.tab_arrow_down);
		leftIv.setImageResource(R.drawable.tab_arrow_down);
		other1Iv.setImageResource(R.drawable.tab_arrow_down);
		other2Iv.setImageResource(R.drawable.tab_arrow_down);
		if (showFilter == 0) {
			filterListLayout.setVisibility(View.GONE);
		} else if (showFilter == 1) {//智能排序
			leftIv.setImageResource(R.drawable.tab_arrow_up);
			filterListLayout.setVisibility(View.VISIBLE);
			filter2ListView.setVisibility(View.VISIBLE);
			for (int i = 0; i < Util.regionList.size(); i++) {
				if(i==0){
					((SingleItemCardEntity)(Util.regionList.get(i))).setSelected(true);
				}else{
					((SingleItemCardEntity)(Util.regionList.get(i))).setSelected(false);
				}
			}
			filterAdapter = new CardListAdapter(context, Util.regionList,
					itemClickListener1);
			filterListView.setAdapter(filterAdapter);
			
			filter2Adapter = new CardListAdapter(context, Util.regionSecondList.get(((SingleItemCardEntity)Util.regionList.get(0)).getId()),
					itemClickListener2);
			filter2ListView.setAdapter(filter2Adapter);
		} else if (showFilter == 2) {//价格
			rightIv.setImageResource(R.drawable.tab_arrow_up);
			filterListLayout.setVisibility(View.VISIBLE);
			filter2ListView.setVisibility(View.GONE);
			filterAdapter = new CardListAdapter(context, Util.priceList,
					itemClickListener4);
			filterListView.setAdapter(filterAdapter);
		}else if (showFilter == 3) {//分类
			other1Iv.setImageResource(R.drawable.tab_arrow_up);
			filterListLayout.setVisibility(View.VISIBLE);
			filter2ListView.setVisibility(View.GONE);
			filterAdapter = new CardListAdapter(context, Util.categoryList,
					itemClickListener3);
			filterListView.setAdapter(filterAdapter);
		}else if (showFilter == 4) {//个人
			other2Iv.setImageResource(R.drawable.tab_arrow_up);
			filterListLayout.setVisibility(View.VISIBLE);
			filter2ListView.setVisibility(View.GONE);
			filterAdapter = new CardListAdapter(context, Util.scopeList,
					itemClickListener5);
			filterListView.setAdapter(filterAdapter);
		}
	}
	private void updateSecondFilter(String pid){
		filter2Adapter = new CardListAdapter(context, Util.regionSecondList.get(pid),
				itemClickListener2);
		filter2ListView.setAdapter(filter2Adapter);
	}
}
