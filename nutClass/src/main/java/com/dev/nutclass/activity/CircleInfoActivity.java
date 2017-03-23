package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class CircleInfoActivity extends BaseActivity implements OnClickListener{
	private Context context;
	private final static String TAG = "CircleInfoActivity";
	private TitleBar titleBar;
	
	private RecyclerView cardListView;
	
	private CardListAdapter adapter;
	private int curPage = 1;
	
	private String pageId;
	
	private LinearLayout inputLayout;
	private EditText inputEdit;
	private TextView inputBtn;
	
	private LinearLayout filterLayout;
	

	private MaterialRefreshLayout refreshLayout;
	
	private RecyclerItemClickListener listener;
	
	private FeedCardEntity entity=null;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courselist);
//		setContentView(R.layout.activity_cardlist);
		context = this;
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
		
		inputLayout = (LinearLayout)findViewById(R.id.ll_input);
		inputEdit = (EditText) findViewById(R.id.edit_input);
		inputBtn = (TextView) findViewById(R.id.btn_input);
		inputLayout.setVisibility(View.VISIBLE);
		
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
		if(getIntent()!=null&&getIntent().hasExtra(Const.KEY_ID)){
			pageId=getIntent().getStringExtra(Const.KEY_ID);
		}else{
			return;
		}
	}
	private void initData() {
		titleBar.setMiddleText("详情");
		reqData(1);

	}
	private void initListener() {
		inputBtn.setOnClickListener(this);
		listener=new RecyclerItemClickListener() {
			
			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
//				if(Util.)
//				Util.showSoftInput(context);
				Util.showSoftInput((Activity)context);
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
		if(v==inputBtn){
			if(Util.checkLogin(context)){
				reqComment();
			}
			
		}
		
	}
	public void update(List<BaseCardEntity> list){
		if(curPage==1){
			entity=(FeedCardEntity) list.get(0);
//			if(entity.getUid()!=null&&entity.getUid().equals(SharedPrefUtil.getInstance().getUid())){
//				titleBar.setTitleRight1(TitleBar.TYPE_RELEASE_IMG);
//				titleBar.setBarClickListener(new TitleBar.BarClickListener() {
//					
//					@Override
//					public boolean onClickRight2() {
//						// TODO Auto-generated method stub
//						return false;
//					}
//					
//					@Override
//					public boolean onClickRight1() {
//						// TODO Auto-generated method stub
//						if(Util.checkLogin(context)){
//							Intent intent = new Intent(context, CircleReleaseActivity.class);
//							context.startActivity(intent);
//						}
//						
//						return true;
//					}
//					
//					@Override
//					public boolean onClickMiddle() {
//						// TODO Auto-generated method stub
//						return false;
//					}
//					
//					@Override
//					public boolean onClickLeft() {
//						// TODO Auto-generated method stub
//						return false;
//					}
//				});
//			}else{
//				titleBar.removeTitleRight1();
//			}
			adapter=new CardListAdapter(context, list,listener);
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
		refreshLayout.setLoadMore(false);
//		http://182.92.7.222/service.php?act=get_market_data_list&cid=2&district_id=1&order_price=1&is_personal=1
		String url=String.format(HttpUtil.addBaseGetParams(UrlConst.GET_ARTICAL_DETAIL), pageId);
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
//								curPage=page;
								curPage=1;
								update(list);
							}else{
								refreshLayout.setLoadMore(false);
							}
						}
					}
				});
		 
	}
	/**
	 * 添加评论
	 *          
	 * */
	private void reqComment() {
		final String comment=inputEdit.getText().toString();
		if(TextUtils.isEmpty(comment)){
			DialogUtils.showToast(context, "请输入评论内容");
			return;
		}
		String url = "";
		url = String.format(HttpUtil.addBaseGetParams(UrlConst.GET_ARTICAL_COMMENT), pageId,comment);

		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
						Util.hideSoftInput((Activity)context, inputEdit);
						DialogUtils.showToast(context, "评论失败");
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						reqData(0);
						Util.hideSoftInput((Activity)context, inputEdit);
						DialogUtils.showToast(context, "评论成功");
						inputEdit.setText("");
						CommentCardEntity c=new CommentCardEntity();
						c.setDesc(comment);
						c.setId(entity.getId());
						c.setName(SharedPrefUtil.getInstance().getSession().getName());
						 
						
						entity.getCommentList().add(0,c);
						adapter.notifyItemChanged(0);
						
						Intent intent = new Intent();
						intent.setAction(Const.ACTION_BROADCAST_FEED_CHANGED);
						intent.putExtra(Const.KEY_ENTITY, entity);
						intent.putExtra(Const.KEY_TYPE, FeedCardEntity.TYPE_COMMENT);
						intent.setPackage(ApplicationConfig.getInstance()
								.getPackageName());
						sendBroadcast(intent);
						
						
					}
				});
	}
	/**
	 * 删除
	 *          
	 * */
	private void reqDel() {
		
		String url = "";
		url = String.format(HttpUtil.addBaseGetParams(UrlConst.GET_ARTICAL_DELETE), pageId);

		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
						Util.hideSoftInput((Activity)context, inputEdit);
						DialogUtils.showToast(context, "删除失败");
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						DialogUtils.showToast(context, "删除成功");
						Intent intent = new Intent();
						intent.setAction(Const.ACTION_BROADCAST_FEED_CHANGED);
						intent.putExtra(Const.KEY_ID, pageId);
						intent.putExtra(Const.KEY_TYPE, FeedCardEntity.TYPE_DEL);
						intent.setPackage(ApplicationConfig.getInstance()
								.getPackageName());
						context.sendBroadcast(intent);
						finish();
					}
				});
	}
	public AlertDialog dialog;

	private void showDeleteDialog() {
		String title = null;
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_dialog_two, null);
		dialog = DialogUtils.showCustomDialog(context, title,
				DialogUtils.ICON_RES_ID_DEFAULT, view,
				new DialogUtils.CustomListener() {

					@Override
					public void onClicked(DialogInterface dialog, View view,
							int which) {
						// TODO Auto-generated method

					}

					@Override
					public void init(View view) {
						// TODO Auto-generated method
						// stub
						((TextView) view.findViewById(R.id.tv_desc))
								.setText(R.string.dialog_tip_delete_feed);
						((Button) view.findViewById(R.id.btn_ok))
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO
										LogUtil.d(TAG, "ok");

										dialog.dismiss();
										reqDel();
									}
								});
						((Button) view.findViewById(R.id.btn_cancel))
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO
										LogUtil.d(TAG, "cancel");
										dialog.dismiss();
									}
								});
					}
				}, R.id.btn_ok, R.id.btn_cancel);
	}
//	public void refreshUI(Object obj,String type){
//		FeedCardEntity entity=null;
//		if(obj instanceof FeedCardEntity){
//			entity=(FeedCardEntity)obj;
//		}
//		if(entity==null){
//			return;
//		}
//		int index = -1;
//		List<BaseCardEntity> list = adapter.getList();
//		 
//		for (int i = 0; i < list.size(); i++) {
//			if (list.get(i).getCardType() == BaseCardEntity.CARD_TYPE_FEED) {
//				if (entity.getId().equals(((FeedCardEntity) list.get(i)).getId())) {
//					index = i;
//					break;
//				}
//			}
//		}
//		if (index >= 0) {
//			if(type.equals(FeedCardEntity.TYPE_COMMENT+"")){
//				((FeedCardEntity) list.get(index)).setCommentList(entity.getCommentList());
//				adapter.notifyItemChanged(index);
//			}
//			if (type.equals(FeedCardEntity.TYPE_DEL + "")) {
//				adapter.removeItem(index);
//			}else if(type.equals(FeedCardEntity.TYPE_COMMENT+"")){
//				((FeedCardEntity) list.get(index)).setCommentList(entity.getCommentList());
//				adapter.notifyItemChanged(index);
//			}else if(type.equals(FeedCardEntity.TYPE_LIKE+"")){
//				((FeedCardEntity) list.get(index)).setUserList(entity.getUserList());
//				((FeedCardEntity) list.get(index)).setIslike(entity.isIslike());
//				adapter.notifyItemChanged(index);
//			}
//		}
//	}
}
