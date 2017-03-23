package com.dev.nutclass.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CommentCardEntity;
import com.dev.nutclass.entity.FeedCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.parser.CircleListParser;
import com.dev.nutclass.parser.HomeListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.HomeNavView;
import com.squareup.okhttp.Request;
import com.umeng.socialize.UMShareAPI;

public class CooponListFragment extends BaseFragment {
	private static final String TAG = "CooponListFragment";
	private RecyclerView cardListView;
private Context mContext;
	private CardListAdapter adapter;
	private int curPage = 1;

	

	private MaterialRefreshLayout refreshLayout;
	
	private RecyclerItemClickListener listener;
	
	private List<BaseCardEntity> list;

//	public CooponListFragment(List<BaseCardEntity> list) {
//		// TODO Auto-generated constructor stub
//		LogUtil.d(TAG, "CooponListFragment");
//		this.list=list;
//
//	}
public static CooponListFragment newInstance() {
	CooponListFragment newFragment = new CooponListFragment();

	return newFragment;

}
	public void setCooponList(List<BaseCardEntity> list){
		this.list=list;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext=getActivity();
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_cardlist, null);
		cardListView = (RecyclerView) view.findViewById(R.id.card_list);
		
		
		
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(
				getActivity());
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		cardListView.setLayoutManager(mLayoutManager);
		refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
		// 定义刷新
		refreshLayout.setLoadMore(false);
		refreshLayout.setRefresh(false);
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
		listener=new RecyclerItemClickListener() {
			
			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				DialogUtils.showToast(mContext, "uid:"+((FeedCardEntity)obj).getUid());
			//	showComment((FeedCardEntity)obj);
			}
			
			 
		};
		reqData(1);
		return view;
	}

	public void update(List<BaseCardEntity> list) {
		if (curPage == 1) {
//			BaseCardEntity adEntity1 = new BaseCardEntity();
//			adEntity1.setCardType(BaseCardEntity.CARD_TYPE_AD1);
//			list.add(1, adEntity1);
//			BaseCardEntity adEntity2 = new BaseCardEntity();
//			adEntity2.setCardType(BaseCardEntity.CARD_TYPE_AD2);
//			list.add(2, adEntity2);
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
	 
	/*
	 * 网络请求数据
	 */
	private void reqData(final int page) {
//		if (page == 1) {
//			curPage=1;
//			refreshLayout.setLoadMore(true);
//		}
//		String url = UrlConst.GET_ARTICAL_LIST;
//		url=url+"&pageNo="+page;
//		OkHttpClientManager.getAsyn(url,
//				new OkHttpClientManager.ResultCallback<String>() {
//
//					@Override
//					public void onError(Request request, Exception e) {
//						// TODO Auto-generated method stub
//						LogUtil.d(TAG, "error e=" + e.getMessage());
//						refreshLayout.setLoadMore(false);
//						refreshLayout.finishRefreshLoadMore();
//						refreshLayout.finishRefresh();
//					}
//
//					@Override
//					public void onResponse(String response) {
//						// TODO Auto-generated method stub
//						LogUtil.d(TAG, "response=" + response);
//						refreshLayout.finishRefreshLoadMore();
//						refreshLayout.finishRefresh();
//						CircleListParser parser = new CircleListParser();
//						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
//								.parse(response);
//						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
//							ArrayList<BaseCardEntity> list = result.getList();
//							if (list != null && list.size() > 0) {
//								curPage = page;
//								update(list);
//							}else{
//								refreshLayout.setLoadMore(false);
//							}
//						}
//					}
//				});
		refreshLayout.finishRefresh();
		if(list!=null&&list.size()>0){
			update(list);
		}else{
			
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
	}
	
	public void refreshUI(Object obj,String type){
		FeedCardEntity entity=null;
		if(obj instanceof FeedCardEntity){
			entity=(FeedCardEntity)obj;
		}
		if(entity==null){
			return;
		}
		int index = -1;
		List<BaseCardEntity> list = adapter.getList();
		 
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCardType() == BaseCardEntity.CARD_TYPE_FEED) {
				if (entity.getId().equals(((FeedCardEntity) list.get(i)).getId())) {
					index = i;
					break;
				}
			}
		}
		if (index >= 0) {
			if (type.equals(FeedCardEntity.TYPE_DEL + "")) {
				adapter.removeItem(index);
			}else if(type.equals(FeedCardEntity.TYPE_COMMENT+"")){
				((FeedCardEntity) list.get(index)).setCommentList(entity.getCommentList());
				adapter.notifyItemChanged(index);
			}else if(type.equals(FeedCardEntity.TYPE_LIKE+"")){
				((FeedCardEntity) list.get(index)).setUserList(entity.getUserList());
				((FeedCardEntity) list.get(index)).setIslike(entity.isIslike());
				adapter.notifyItemChanged(index);
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
