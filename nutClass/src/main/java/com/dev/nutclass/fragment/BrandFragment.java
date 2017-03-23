package com.dev.nutclass.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.adapter.BrandListAdapter;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.db.NutClassDB;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.BrandCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.DBEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.parser.BrandListParser;
import com.dev.nutclass.parser.BrandListParserNew;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class BrandFragment extends BaseFragment {
	private static final String TAG = "BrandFragment";
//	private RecyclerView cardListView;
	private ListView cardListView;
	private BrandListAdapter adapter;
	private int curPage = 1;

	public BrandFragment() {
		// TODO Auto-generated constructor stub
		LogUtil.d(TAG, "ShoppingFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_brand, null);
		cardListView = (ListView) view.findViewById(R.id.card_list);
		reqData(false);
		return view;
	}

	public void update(List<List<BrandCardEntity>> list) {
//		if(curPage==1){
//			adapter=new CardListAdapter(getActivity(), list);
//			cardListView.setAdapter(adapter);
//			GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),3);
//			cardListView.setLayoutManager(mLayoutManager);
//		}else{
//			adapter.addList(list);
//		}
		adapter = new BrandListAdapter(getActivity(),list);
		cardListView.setAdapter(adapter);
	}

	@Override
	public void onRefresh(boolean b) {
		// TODO Auto-generated method stub
		super.onRefresh(b);
		reqData(false);
	}

	/*
	 * 从数据库获取当前用户的购物车数据
	 */
	private void reqData(boolean isEdit) {
		String url=HttpUtil.addBaseGetParams(UrlConst.BRANDS_URL_NEW);
//		String url = "http://dev.kobiko.cn/Api/Appapi/get_hot_brands2";
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
						BrandListParserNew parser = new BrandListParserNew();
						List<List<BrandCardEntity>> result = (List<List<BrandCardEntity>>) parser.parse(response);
						if(parser.getStatus()==UrlConst.SUCCESS_CODE){
							List<List<BrandCardEntity>> list = result;
							if(list!=null&&list.size()>0){
								update(list);
							}
						}
					}
				});
	}
	

}
