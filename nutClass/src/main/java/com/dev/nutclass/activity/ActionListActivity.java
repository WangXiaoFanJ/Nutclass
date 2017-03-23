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
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.SingleItemCardEntity;
import com.dev.nutclass.parser.ActionListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class ActionListActivity extends BaseActivity implements OnClickListener{
	private Context context;
	private final static String TAG = "ActionListActivity";
	private TitleBar titleBar;
	private LinearLayout leftLayout;
	private TextView leftTv;
	private ImageView leftIv;
	private LinearLayout rightLayout;
	private TextView rightTv;
	private ImageView rightIv;
	private RecyclerView cardListView;
	
	private CardListAdapter adapter;
	private int curPage = 1;
	
	private RecyclerView filterListView;
	private RecyclerView filter2ListView;
	private LinearLayout filterListLayout;

	private CardListAdapter filterAdapter;
	private CardListAdapter filter2Adapter;
	private int showFilter = 0;// 0:全不显示 1：显示城市 2：显示价格
	private RecyclerItemClickListener itemClickListener1;
	private RecyclerItemClickListener itemClickListener2;
	private RecyclerItemClickListener itemClickListener3;

	private MaterialRefreshLayout refreshLayout;
	
	private String distinctId="";
	private String priceId="0";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_cardlist);
		setContentView(R.layout.activity_courselist);
		context = this;
		initView();
		initIntent();
		initData();
		initListener();
	}
	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		leftLayout=(LinearLayout)findViewById(R.id.ll_left);
		leftTv=(TextView)findViewById(R.id.tv_name_left);
		leftIv=(ImageView)findViewById(R.id.iv_arrow_left);
		rightLayout=(LinearLayout)findViewById(R.id.ll_right);
		rightTv=(TextView)findViewById(R.id.tv_name_right);
		rightTv.setText("人气排序");
		rightIv=(ImageView)findViewById(R.id.iv_arrow_right);
		cardListView = (RecyclerView)findViewById(R.id.card_list);
		
		
		filterListView = (RecyclerView) findViewById(R.id.filter_list);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		filterListView.setLayoutManager(mLayoutManager);
		
		LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(context);
		mLayoutManager2.setOrientation(LinearLayout.VERTICAL);
		filter2ListView = (RecyclerView) findViewById(R.id.filter_list2);
		filter2ListView.setLayoutManager(mLayoutManager2);
		
		filterListLayout=(LinearLayout)findViewById(R.id.ll_filter_list);
		
		LinearLayoutManager rootManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true);
//		rootManager.setOrientation(LinearLayout.VERTICAL);
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
		
	}
	private void initData() {
		titleBar.setMiddleText("最新活动");
		titleBar.setTitleRight1(TitleBar.TYPE_SEARCH_IMG);
		
		 reqData(1);

	}
	private void initListener() {
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {
			
			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,SearchActivity.class);
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
				priceId=entity.getId();
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
		}
	}
	public void update(List<BaseCardEntity> list){
		if(curPage==1){
			adapter=new CardListAdapter(context, list);
			cardListView.setAdapter(adapter);
		}else{
			adapter.addList(list);
		}
	}
	/*
	 * 网络请求数据
	 */
	private void reqData(final int page){
		if (page == 1) {
			curPage=page;
			refreshLayout.setLoadMore(true);
		}
//		http://182.92.7.222/service.php?act=get_market_data_list&cid=2&district_id=1&order_price=1&is_personal=1
		String url=HttpUtil.addBaseGetParams(UrlConst.HOT_URL);
		url=url+"&district_id="+distinctId+"&order_price="+priceId+"&pageNo="+page;
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
						ActionListParser parser=new ActionListParser();
						JsonDataList<BaseCardEntity> result=(JsonDataList<BaseCardEntity>) parser.parse(response);
						if(result.getErrorCode()==UrlConst.SUCCESS_CODE){
							ArrayList<BaseCardEntity> list=result.getList();
							if(list!=null&&list.size()>0){
								curPage=page;
								update(list);
							}else{
								refreshLayout.setLoadMore(false);
							}
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
		} else if (showFilter == 2) {
			rightIv.setImageResource(R.drawable.tab_arrow_up);
			filterListLayout.setVisibility(View.VISIBLE);
			filter2ListView.setVisibility(View.GONE);
			filterAdapter = new CardListAdapter(context, Util.priceList,
					itemClickListener3);
			filterListView.setAdapter(filterAdapter);
		}
	}
	private void updateSecondFilter(String pid){
		filter2Adapter = new CardListAdapter(context, Util.regionSecondList.get(pid),
				itemClickListener2);
		filter2ListView.setAdapter(filter2Adapter);
	}
}
