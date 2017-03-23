package com.dev.nutclass.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.activity.SearchActivity;
import com.dev.nutclass.activity.SelectAddressActivity;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.db.NutClassDB;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.DBEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.SimpleTextEntity;
import com.dev.nutclass.parser.CardListParser;
import com.dev.nutclass.parser.SimpleTextListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.view.BannerCardView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

public class CategoryFragment extends BaseFragment {
	private static final String TAG = "CategoryFragment";
	private RecyclerView categoryListView;
	private RecyclerView categoryChildListView;
private Context mCotnext;
	private CardListAdapter categoryAdapter;
	private CardListAdapter categoryChildAdapter;
	private int curPage = 1;
	private RecyclerItemClickListener itemClickListener;

	private BannerCardView bannerView;
	private LinearLayout containerLayout;
	private LinearLayout locationLayout;
	private LinearLayout brandLayout;
	private ImageView searchIv;
	private  TextView locationTv;
	
	

	public CategoryFragment() {
		// TODO Auto-generated constructor stub
		LogUtil.d(TAG, "CategoryFragment");
	}
	public void updateLocation(){
		locationTv.setText(SharedPrefUtil.getInstance().getLocation());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mCotnext=getActivity();
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_category, null);
		categoryListView = (RecyclerView) view.findViewById(R.id.category_list);
		categoryChildListView = (RecyclerView) view
				.findViewById(R.id.category_child_list);
		// bannerView=(BannerCardView) view.findViewById(R.id.view_banner);
		containerLayout = (LinearLayout) view.findViewById(R.id.ll_container);
		locationLayout = (LinearLayout) view.findViewById(R.id.ll_location);
		brandLayout=(LinearLayout)view.findViewById(R.id.ll_brand);
		locationTv=(TextView)view.findViewById(R.id.tv_location);
		locationTv.setText(SharedPrefUtil.getInstance().getLocation());
		searchIv = (ImageView) view.findViewById(R.id.iv_search);
		searchIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});
		locationLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),SelectAddressActivity.class);
				startActivityForResult(intent, 100);


			}
		});
		
		reqBanner();
		reqData(1);
		return view;
	}

	public void update(List<BaseCardEntity> list) {
		if (curPage == 1) {
			itemClickListener = new RecyclerItemClickListener() {

				@Override
				public void onItemClick(View view, Object obj) {
					// TODO Auto-generated method stub
					SimpleTextEntity entity = (SimpleTextEntity) obj;
					if (entity == null) {
						return;
					}
					for (int i = 0; i < categoryAdapter.getList().size(); i++) {
						if (((SimpleTextEntity) (categoryAdapter.getList()
								.get(i))).getId().equals(entity.getId())) {
							((SimpleTextEntity) (categoryAdapter.getList()
									.get(i))).setSelected(true);
							//更新brand
							updateBrandLayout(((SimpleTextEntity) (categoryAdapter.getList()
									.get(i))).getBrandList());
						} else {
							((SimpleTextEntity) (categoryAdapter.getList()
									.get(i))).setSelected(false);
						}
					}
					categoryAdapter.notifyDataSetChanged();
					reqChildData(entity.getId());
				}

				 
			};
			updateBrandLayout(((SimpleTextEntity) list.get(0)).getBrandList());
			reqChildData(((SimpleTextEntity) list.get(0)).getId());
			((SimpleTextEntity) list.get(0)).setSelected(true);
			categoryAdapter = new CardListAdapter(getActivity(), list,
					itemClickListener);
			categoryListView.setAdapter(categoryAdapter);
			LinearLayoutManager mLayoutManager = new LinearLayoutManager(
					getActivity());
			mLayoutManager.setOrientation(LinearLayout.VERTICAL);
			categoryListView.setLayoutManager(mLayoutManager);
		} else {
			categoryAdapter.addList(list);
		}
	}

	public void updateChild(List<BaseCardEntity> list) {
		// if(curPage==1){
		// categoryChildAdapter=new CardListAdapter(getActivity(), list);
		// categoryChildListView.setAdapter(categoryChildAdapter);
		// GridLayoutManager mLayoutManager = new
		// GridLayoutManager(getActivity(), 2);
		// categoryChildListView.setLayoutManager(mLayoutManager);
		// }else{
		// categoryChildAdapter.addList(list);
		// }
		categoryChildAdapter = new CardListAdapter(getActivity(), list);
		categoryChildListView.setAdapter(categoryChildAdapter);
		GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),
				2);
		categoryChildListView.setLayoutManager(mLayoutManager);
	}

	@Override
	public void onRefresh(boolean b) {
		// TODO Auto-generated method stub
		super.onRefresh(b);
		reqData(1);
		reqBanner();
	}

	private void reqBanner() {
		String url = UrlConst.CATEGORY_BANNER_URL;
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
						try {
							BannerCardEntity entity = new BannerCardEntity();
							List<ImageEntity> imgList = new ArrayList<ImageEntity>();
							entity.setList(imgList);
							JSONArray jsonArray = new JSONArray(response);
							if (jsonArray != null) {
								for (int i = 0; i < jsonArray.length(); i++) {
									String jsonStr = jsonArray.optString(i);
									ImageEntity item = new ImageEntity();
									item.setSmallPath(jsonStr);
									imgList.add(item);
								}
							}
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT);
							bannerView = new BannerCardView(getActivity(),1);
							containerLayout.addView(bannerView, params);
							bannerView.updateView(entity,3);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/*
	 * 网络请求数据
	 */
	private void reqData(final int page) {
		String url = UrlConst.CATEGORY_URL;
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

						SimpleTextListParser parser = new SimpleTextListParser(
								SimpleTextEntity.TYPE_CATEGORY);
						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = result.getList();
							if (list != null && list.size() > 0) {
								update(list);
							}
							
						}
					}
				});
	}

	/*
	 * 网络请求数据
	 */
	private void reqChildData(String cid) {
		String url = UrlConst.CATEGORY_CHILD_URL + cid;
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
						SimpleTextListParser parser = new SimpleTextListParser(
								SimpleTextEntity.TYPE_CATEGORY_CHILD);
						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							ArrayList<BaseCardEntity> list = result.getList();
							if (list != null && list.size() > 0) {
								updateChild(list);
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
			}
		}
	}
	
	private void updateBrandLayout(final List<ImageEntity> imgList){
		brandLayout.removeAllViews();
		if(imgList==null||imgList.size()==0){
			return;
		}
		int length=imgList.size();
		for(int i=0;i<length;i++){
			View view=LayoutInflater.from(mCotnext).inflate(R.layout.view_brand_list, null);
			ImageView leftIv;
			ImageView rightIv;
			leftIv=(ImageView)view.findViewById(R.id.iv_brand_left);
			rightIv=(ImageView)view.findViewById(R.id.iv_brand_right);
			final int leftPos=i;
			ImageLoader.getInstance().displayImage(imgList.get(leftPos).getSmallPath(), leftIv);
			leftIv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.setClass(mCotnext, CourseListActivity.class);
					intent.putExtra(Const.KEY_ID, imgList.get(leftPos).getId());
					intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BRAND);
					startActivity(intent);
				}
			});
			final int rightPos=++i;
			if(rightPos==length){
				rightIv.setVisibility(View.INVISIBLE);
			}else{
				rightIv.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(imgList.get(rightPos).getSmallPath(), rightIv);
				rightIv.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent();
						intent.setClass(mCotnext, CourseListActivity.class);
						intent.putExtra(Const.KEY_ID, imgList.get(rightPos).getId());
						intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BRAND);
						startActivity(intent);
					}
				});
			}
			brandLayout.addView(view);
		}
	}

}
