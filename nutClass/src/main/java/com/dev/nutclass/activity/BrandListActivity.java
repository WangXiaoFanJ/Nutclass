package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.R.id;
import com.dev.nutclass.R.layout;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.parser.BrandListParser;
import com.dev.nutclass.parser.CourseListParser;
import com.dev.nutclass.parser.CardListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class BrandListActivity extends BaseActivity implements OnClickListener{
	private Context context;
	private final static String TAG = "BrandListActivity";
	private TitleBar titleBar;
	private RecyclerView cardListView;
	
	private CardListAdapter adapter;
	private String cid = "";
	private int curPage = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cardlist);
		context = this;
		initView();
		initIntent();
		initData();
		initListener();
	}
	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		titleBar.setMiddleText("品牌馆");
		cardListView = (RecyclerView)findViewById(R.id.card_list);
 
	}
	private void initIntent() {
		if(getIntent().hasExtra(Const.KEY_ID)){
			cid=getIntent().getStringExtra(Const.KEY_ID);
		}
		
	}
	private void initData() {
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
			adapter=new CardListAdapter(context, list);
			cardListView.setAdapter(adapter);
			GridLayoutManager mLayoutManager = new GridLayoutManager(context,3);
			cardListView.setLayoutManager(mLayoutManager);
		}else{
			adapter.addList(list);
		}
	}
	/*
	 * 网络请求数据
	 */
	private void reqData(final int page){
		String url=HttpUtil.addBaseGetParams(UrlConst.BRANDS_URL);
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
						
						BrandListParser parser=new BrandListParser();
						JsonDataList<BaseCardEntity> result=(JsonDataList<BaseCardEntity>) parser.parse(response);
						if(result.getErrorCode()==UrlConst.SUCCESS_CODE){
							ArrayList<BaseCardEntity> list=result.getList();
							if(list!=null&&list.size()>0){
								update(list);
							}
						}
					}
				});
		 
	}
}
