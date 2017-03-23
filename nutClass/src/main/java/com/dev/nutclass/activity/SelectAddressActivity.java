package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.dev.nutclass.R;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.SingleItemCardEntity;
import com.dev.nutclass.parser.BaseParser;
import com.dev.nutclass.parser.BrandDetailListParser;
import com.dev.nutclass.parser.CourseListParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class SelectAddressActivity extends BaseActivity implements OnClickListener {
	private Context context;
	private final static String TAG = "SelectAddressActivity";
	private TitleBar titleBar;


	private CardListAdapter filterAdapter;
	private CardListAdapter filter2Adapter;
	private RecyclerView filterListView;
	private RecyclerView filter2ListView;

	private RecyclerItemClickListener itemClickListener1;
	private RecyclerItemClickListener itemClickListener2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address);
		context = this;
		initView();
		initIntent();
		initListener();
		initData();
	}
	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		
		
		filterListView = (RecyclerView) findViewById(R.id.filter_list);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		filterListView.setLayoutManager(mLayoutManager);
		
		LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(context);
		mLayoutManager2.setOrientation(LinearLayout.VERTICAL);
		filter2ListView = (RecyclerView) findViewById(R.id.filter_list2);
		filter2ListView.setLayoutManager(mLayoutManager2);
		
	}
	private void initIntent() {

	}
	private void initData() {
		updateFilter();
	}

	private void initListener() {
		itemClickListener1 = new RecyclerItemClickListener() {

			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, entity.getDesc());
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
				parseAddress(entity.getDesc());
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
		
	}

	
	private void updateFilter() {
		filter2ListView.setVisibility(View.VISIBLE);
		filterAdapter = new CardListAdapter(context, Util.regionList,
				itemClickListener1);
		filterListView.setAdapter(filterAdapter);
		
		filter2Adapter = new CardListAdapter(context, Util.regionSecondList.get(((SingleItemCardEntity)Util.regionList.get(0)).getId()),
				itemClickListener2);
		filter2ListView.setAdapter(filter2Adapter);
	}
	private void updateSecondFilter(String pid){
		filter2Adapter = new CardListAdapter(context, Util.regionSecondList.get(pid),
				itemClickListener2);
		filter2ListView.setAdapter(filter2Adapter);
	}
	/*
	 * 评论网络请求
	 */
	private void parseAddress(final String str) {
		String url = String.format(HttpUtil
				.addBaseGetParams(UrlConst.GET_POSITION_URL), str);
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
						JSONObject result;
						try {
							result = new JSONObject(response);
							int errorCode = result.optInt("status", UrlConst.SUCCESS_CODE);
							String errorMsg = result.optString("info", "request success");
							if(errorCode==UrlConst.SUCCESS_CODE){
								JSONObject dataObj=result.optJSONObject("data");
								SharedPrefUtil.getInstance().setLon(dataObj.optString("lng"));
								SharedPrefUtil.getInstance().setLat(dataObj.optString("lat"));
								SharedPrefUtil.getInstance().setLocation(str);
								Intent intent=new Intent();
								intent.putExtra(Const.KEY_CONTENT, str);
								setResult(RESULT_OK,intent);
								finish();
							}else{
								DialogUtils.showToast(context, "更换地址失败");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				});

	}
}
