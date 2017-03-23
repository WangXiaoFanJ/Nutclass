package com.dev.nutclass.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.SearchActivity;
import com.dev.nutclass.activity.SelectAddressActivity;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.parser.HomeListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.squareup.okhttp.Request;

public class HomeFragment extends BaseFragment {
	private static final String TAG = "HomeFragment";
	private RecyclerView cardListView;

	private CardListAdapter adapter;
	private int curPage = 1;

	private LinearLayout locationLayout;
	private ImageView searchIv;
	private  TextView locationTv;

	private MaterialRefreshLayout refreshLayout;

	public HomeFragment() {
		// TODO Auto-generated constructor stub
		LogUtil.d(TAG, "HomeFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_home, null);
		cardListView = (RecyclerView) view.findViewById(R.id.card_list);
		locationLayout = (LinearLayout) view.findViewById(R.id.ll_location);
		searchIv = (ImageView) view.findViewById(R.id.iv_search);
		locationTv=(TextView)view.findViewById(R.id.tv_location);
		locationTv.setText(SharedPrefUtil.getInstance().getLocation());
	
		locationLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),SelectAddressActivity.class);
				startActivityForResult(intent, 100);


			}
		});
		searchIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),SearchActivity.class);
				startActivity(intent);
			}
		});
		String[] filters = { Const.ACTION_BROADCAST_LOGIN_SUCC,
				Const.ACTION_BROADCAST_FEED_CHANGED,
				Const.ACTION_BROADCAST_RECEIVE_MESSAGE,
				Const.ACTION_BROADCAST_FEED_RELEASE_CHANGED };
		registerReceiver(filters);
		
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(
				getActivity());
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		cardListView.setLayoutManager(mLayoutManager);
		refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
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
		reqData(1);
		return view;
	}

	public void update(List<BaseCardEntity> list) {
		if (curPage == 1) {
			BaseCardEntity adEntity1 = new BaseCardEntity();
			adEntity1.setCardType(BaseCardEntity.CARD_TYPE_AD1);
			list.add(1, adEntity1);
			BaseCardEntity adEntity2 = new BaseCardEntity();
			adEntity2.setCardType(BaseCardEntity.CARD_TYPE_AD2);
			list.add(2, adEntity2);
			adapter = new CardListAdapter(getActivity(), list);
			cardListView.setAdapter(adapter);
		} else {
			adapter.addList(list);
		}
	}

	@Override
	public void onRefresh(boolean b) {
		// TODO Auto-generated method stub
		super.onRefresh(b);
		reqData(1);
	}
	public void updateLocation(){
		locationTv.setText(SharedPrefUtil.getInstance().getLocation());
	}
	/*
	 * 网络请求数据
	 */
	private void reqData(final int page) {
		if (page == 1) {
			curPage=1;
			refreshLayout.setLoadMore(true);
		}
		String url = UrlConst.HOME_URL;
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
						HomeListParser parser = new HomeListParser();
						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = result.getList();
							if (list != null && list.size() > 0) {
								curPage = page;
								update(list);
							}else{
								refreshLayout.setLoadMore(false);
							}
						}
					}
				});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			if(data!=null&&data.hasExtra(Const.KEY_CONTENT)){
				locationTv.setText(data.getStringExtra(Const.KEY_CONTENT));
				reqData(1);
			}
		}
	}

}
