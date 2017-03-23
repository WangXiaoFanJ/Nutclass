package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dev.nutclass.R;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.FeedCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.parser.CircleListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class FeedUserPageActivity extends BaseActivity implements OnClickListener{
	private Context context;
	private final static String TAG = "FeedUserPageActivity";
	private TitleBar titleBar;
	
	private RecyclerView cardListView;
	
	private CardListAdapter adapter;
	private int curPage = 1;
	
	private String otherUid;
	private String otherUsername;

	private LinearLayout filterLayout;
	

	private MaterialRefreshLayout refreshLayout;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courselist);
//		setContentView(R.layout.activity_cardlist);
		context = this;
		registerReceiver();
		initView();
		initIntent();
		initData();
		initListener();
	}
	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		titleBar.removeTitleRight1();
		cardListView = (RecyclerView)findViewById(R.id.card_list);
		
		filterLayout = (LinearLayout) findViewById(R.id.ll_filter);
		filterLayout.setVisibility(View.GONE);
		
		
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
		if(getIntent()!=null&&getIntent().hasExtra(Const.KEY_TITLE)){
			otherUid=getIntent().getStringExtra(Const.KEY_UID);
			otherUsername=getIntent().getStringExtra(Const.KEY_TITLE);
		}else{
			return;
		}
	}
	private void initData() {
		titleBar.setMiddleText(otherUsername+"的圈子");
		 reqData(1);

	}
	private void initListener() {
		
	}

	
	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	public void update(List<BaseCardEntity> list){
		if(curPage==1){
			if(otherUid.equals(SharedPrefUtil.getInstance().getUid())){
				FeedCardEntity e=new FeedCardEntity();
				e.setSelfProfile(true);
				e.setName(SharedPrefUtil.getInstance().getSession().getName());
				e.setUid(SharedPrefUtil.getInstance().getUid());
				e.setHeadImgUrl(SharedPrefUtil.getInstance().getSession().getPortrait());
				list.add(0,e);
			}
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
		String url=String.format(HttpUtil.addBaseGetParams(UrlConst.GET_ARTICAL_LIST_BY_ID), otherUid);
		url=url+"&pageNo="+page;
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
						CircleListParser parser=new CircleListParser();
						JsonDataList<BaseCardEntity> result=(JsonDataList<BaseCardEntity>) parser.parse(response);
						if(result.getErrorCode()==UrlConst.SUCCESS_CODE){
							ArrayList<BaseCardEntity> list=result.getList();
							
							if(list!=null&&list.size()>0){
								curPage=page;
								update(list);
							}else{
								if(page==1){
									update(list);
								}
								refreshLayout.setLoadMore(false);
							}
						}
					}
				});
		 
	}
	
	public void refreshUI(String id,String type){
		int index = -1;
		List<BaseCardEntity> list = adapter.getList();
		FeedCardEntity entity = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCardType() == BaseCardEntity.CARD_TYPE_FEED) {
				entity = (FeedCardEntity) list.get(i);
				if (id.equals(entity.getId())) {
					index = i;
					break;
				}
			}
		}
		if (index >= 0) {
			if (type.equals(FeedCardEntity.TYPE_DEL + "")) {
				adapter.removeItem(index);
			}
		}
	}
	@Override
	public void feedCreatedSucc() {
		// TODO Auto-generated method stub
		super.feedCreatedSucc();
		reqData(1);
	}
	
	
}
