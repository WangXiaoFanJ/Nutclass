package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.parser.ZeroListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class ZeroListActivity extends BaseActivity implements OnClickListener{
	private Context context;
	private final static String TAG = "ZeroListActivity";
	private TitleBar titleBar;
	private LinearLayout leftLayout;
	private TextView leftTv;
	private ImageView leftIv;
	private LinearLayout rightLayout;
	private TextView rightTv;
	private ImageView rightIv;
	private RecyclerView cardListView;
	private LinearLayout filterLayout;
	private CardListAdapter adapter;
	private String cid = "";
	private int curPage = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_cardlist);
		setContentView(R.layout.activity_zero_list);
		context = this;
		initView();
		initIntent();
		initData();
		initListener();
	}
	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		titleBar.removeTitleRight1();
		filterLayout = (LinearLayout) findViewById(R.id.ll_filter);
		leftLayout=(LinearLayout)findViewById(R.id.ll_left);
		leftTv=(TextView)findViewById(R.id.tv_name_left);
		leftIv=(ImageView)findViewById(R.id.iv_arrow_left);
		rightLayout=(LinearLayout)findViewById(R.id.ll_right);
		rightTv=(TextView)findViewById(R.id.tv_name_right);
		rightIv=(ImageView)findViewById(R.id.iv_arrow_right);
		cardListView = (RecyclerView)findViewById(R.id.card_list);
		filterLayout.setVisibility(View.GONE);
	}
	private void initIntent() {
		if(getIntent().hasExtra(Const.KEY_ID)){
			cid=getIntent().getStringExtra(Const.KEY_ID);
		}
		
	}
	private void initData() {
		titleBar.setMiddleText("抢购区");
		 reqData(1);

	}
	private void initListener() {
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
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
			LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
			mLayoutManager.setOrientation(LinearLayout.VERTICAL);
			cardListView.setLayoutManager(mLayoutManager);
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
		if(page==1){
			 
		}
		String url=HttpUtil.addBaseGetParams(UrlConst.ZERO_URL);
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
						
						ZeroListParser parser=new ZeroListParser();
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
