package com.dev.nutclass.fragment;
//package com.dev.beautydiary.fragment;
//
//import java.util.List;
//import java.util.concurrent.RejectedExecutionException;
//
//import org.apache.http.NameValuePair;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.view.animation.AnimationUtils;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.dev.beautydiary.R;
//import com.dev.beautydiary.adapter.BaseListAdapter;
//import com.dev.beautydiary.entity.JsonDataList;
//import com.dev.beautydiary.parser.IParser;
//import com.dev.beautydiary.request.ITaskFinishListener;
//import com.dev.beautydiary.request.RequestTask;
//import com.dev.beautydiary.request.TaskParams;
//import com.dev.beautydiary.request.TaskResult;
//import com.dev.beautydiary.utils.DensityUtil;
//import com.dev.beautydiary.utils.DialogUtils;
//import com.dev.beautydiary.utils.HttpUtil;
//import com.dev.beautydiary.utils.LogUtil;
//import com.dev.beautydiary.utils.TextUtil;
//import com.dev.beautydiary.view.ListFooterView;
//import com.dev.beautydiary.view.PullListView;
//import com.dev.beautydiary.view.RoundedImageView;
//
//
///**
// * 分页网络连接选项卡页面
// * 
// * @author LJ
// * 
// */
//public abstract class BaseHttpListFragment extends BaseFragment implements
//		OnItemClickListener, ITaskFinishListener/**, OnScrollListener**/ {
//
//	private static final String TAG = "BaseHttpListFragment";
//	protected LayoutInflater mInflater;
//
//	//请求网络分页
//	private int mCurPage = 0;
//	private int maxId=0;//分页加载记录最后一条id
//	public int getMaxId() {
//		return maxId;
//	}
//	public void setMaxId(int maxId) {
//		this.maxId = maxId;
//	}
//	private static final int PAGE_NUM = 10;
//	
//
//	
//	private View mRootView;
//	public PullListView mList;
//	private BaseListAdapter<?> mAdapter;
//	private View loadingView;
//	private View failView;
//	private View emptyView;
//	private ListFooterView footView;
//	private View headView;
//
//	private boolean isLoading = false;// 是否正在加载数据
//	private boolean autoLoading = true;// 是否自动加载数据
//	private RequestTask reqTask;
//	private String reqUrl;
//	private IParser parser;
//	private boolean isPageLoad=true;//是否分页加载
//	private boolean isPullDownEnable = false;//是否可以下拉刷新
//	private boolean isPageEnd=false;//是否分页结束
//	
//	public boolean isPullDownEnable() {
//		return isPullDownEnable;
//	}
//
//	public void setPullDownEnable(boolean isPullDownEnable) {
//		this.isPullDownEnable = isPullDownEnable;
//	}
//
//	private String httpMethod = RequestTask.HTTP_GET;
//	private List<NameValuePair> paramsList = null;
//	/**
//	 * 设置Fragment属性
//	 * 
//	 * @param reqUrl      接口地址
//	 * @param mIParser    解析器
//	 * @param isAutoLoad  是否自动加载数据
//	 * @param isPageLoad  是否分页加载
//	 * @param isReload  加载失败后是否保留原页面。true
//	 */
//	public void setHttpReqInfo(String reqUrl, IParser mIParser,boolean isAutoLoad, boolean isPageLoad) {
//		this.reqUrl = reqUrl;
//		this.parser = mIParser;
//		this.autoLoading = isAutoLoad;
//		this.isLoading = isPageLoad;
//	}
//	
//	public void setPostHttpReqInfo(String reqUrl, IParser mIParser,boolean isAutoLoad, boolean isPageLoad,List<NameValuePair> paramsList) {
//		this.reqUrl = reqUrl;
//		this.parser = mIParser;
//		this.autoLoading = isAutoLoad;
//		this.isLoading = isPageLoad;
//		this.httpMethod = RequestTask.HTTP_POST;
//		this.paramsList = paramsList;
//	}
//	
//	public BaseListAdapter<?> getListAdapter() {
//		return mAdapter;
//	}
//
//	public ListView getListView() {
//		return mList;
//	}
//
//	 
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		LogUtil.d(TAG, "onCreate");
//		Bundle bundle = getArguments();
//		if (bundle != null) {
//
//		}
//
//	}
//
//	@Override
//	public void onDestroy() {
//		LogUtil.d(TAG, "onDestroy");
//		if (reqTask != null) {
//			reqTask.cancel(true);
//			isLoading = false;
//		}
//		super.onDestroy();
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//		LogUtil.d(TAG, "onCreateView");
//		View v = inflater.inflate(R.layout.fragment_base_http_list, container, false);
//		mAdapter = createListAdapter();
//		initView(v);
//		initReqInfo();
//		initListener(v);
//		return v;
//	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		LogUtil.d(TAG, "onViewCreated");
//		super.onViewCreated(view, savedInstanceState);
//		if (autoLoading) {
//			reqData(1);
//		}
//	}
//
//	@Override
//	public void onDestroyView() {
//		LogUtil.d(TAG, "onDestroyView");
//		super.onDestroyView();
//	}
//	
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//
//	}
//
//
//
//	private void initView(View root) {
//		mInflater = LayoutInflater.from(getActivity());
//		mRootView = root;
//		mList = (PullListView) root.findViewById(R.id.list);
//		loadingView = root.findViewById(R.id.view_loading);
//		failView = root.findViewById(R.id.view_failed);
//		emptyView = root.findViewById(R.id.view_empty);
//		
//		headView = createHeaderView();
//		if (headView != null) {
//			mList.addHeaderView(headView, null, false);
//		}
//		footView=new ListFooterView(getActivity());
//		if(footView!=null){
//			mList.addFooterView(footView);
//			footView.setVisibility(View.GONE);
//		}
//		updateUIByStatus(TYPE_STATUS_NORMAL);
//	}
//
//	 
//	private void initListener(View root) {
//
//		mList.setAdapter(mAdapter);
//		mList.setOnItemClickListener(this);
//		failView.setOnClickListener(mClickListener);
//		//滑动时暂停图片下载，腾出更多资源渲染界面，使滑动更流程
//		mList.setOnScrollListener(pageLoadListener);
//		footView.setOnClickListener(mClickListener);
//
//	}
//	
//	private final OnScrollListener pageLoadListener = new OnScrollListener() {
//		
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//			// TODO Auto-generated method stub
//			if (scrollState == SCROLL_STATE_IDLE) {
//			}
//		}
//		
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
//			// TODO Auto-generated method stub
//			//非分页加载、正在加载中、分页加载结束 
//			if (!isPageLoad||isLoading||isPageEnd) {
//				return;
//			}
//			if (firstVisibleItem + visibleItemCount != totalItemCount) {
//				return;
//			}
//			if (!HttpUtil.isConnected(getActivity())) {
//				updateUIByStatus(TYPE_STATUS_FAILURE);
//				return;
//			}
//			reqData(mCurPage + 1);
//		}
//	};
//
//	OnClickListener mClickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			if (v == failView) {
//				reqData(1);
//			}else if(v==footView){
//				if(footView.getType()==ListFooterView.TYPE_STATUS_FAILURE){
//					reqData(mCurPage);
//				}else if(footView.getType()==ListFooterView.TYPE_STATUS_MORE){
//					
//				}
//			}
//		}
//	};
//
//
//	private void reqData(int page) {
//		if (isLoading) {
//			return;
//		}
//		//如果是当前页请求
//		if (page != 1 && page == mCurPage) {
//			return;
//		}
//		mCurPage = page;
//		updateUIByStatus(TYPE_STATUS_LOADING);
//		String url = reqUrl;
//		if (isPageLoad) {// 分页显示
//			int offset = (page - 1) * PAGE_NUM;
//			LogUtil.d(TAG, "page=" + page + ",offset=" + offset + ",num="+ PAGE_NUM);
//			if(page==1){
//				maxId=0;
//			}
//			url = reqUrl + makePageParam(page);
//		}
//		try {
//			reqTask = new RequestTask(getActivity(), parser);
//			reqTask.setTaskFinishListener(this);
//			reqTask.setExtra(page);
//			TaskParams params = new TaskParams();
//			params.put(RequestTask.PARAM_URL, url);
//
//			if (RequestTask.HTTP_POST.equals(httpMethod)) {
//				params.put(RequestTask.PARAM_HTTP_METHOD, RequestTask.HTTP_POST);
//				reqTask.setPostParams(paramsList);
//			}else {
//				params.put(RequestTask.PARAM_HTTP_METHOD, RequestTask.HTTP_GET);	
//			}
//			reqTask.execute(params);
//		} catch (RejectedExecutionException localRejectedExecutionException) {
//			LogUtil.e(TAG, localRejectedExecutionException.getMessage());
//		}
//	}
//
//	private String makePageParam(int page) {
//		StringBuilder sBuilder = new StringBuilder();
//		sBuilder.append("&page=").append(page);
//		sBuilder.append("&num=").append(PAGE_NUM);
//		sBuilder.append("&maxid=").append(maxId);
//		return sBuilder.toString();
//	}
//	@SuppressWarnings("rawtypes")
//	@Override
//	public final void onSucc(TaskResult taskResult) {
//		LogUtil.i(TAG, "===========onSucc===========");
//		RequestTask task = (RequestTask) taskResult.task;
//		int page = (Integer) task.getExtra();
//		if (taskResult.retObj != null) {
//			mCurPage = page;
//			updateUIByStatus(TYPE_STATUS_NORMAL);
//			// 检查分页是否结束
//			if (taskResult.retObj instanceof JsonDataList) {
//				JsonDataList pageResult = (JsonDataList) taskResult.retObj;
//				int totalPage = (int)Math.ceil((double)pageResult.getTotal() / PAGE_NUM);
//				if (pageResult.getTotal() > 0 && mCurPage >= totalPage) {
//					isPageEnd = true;
//				}
//				if (pageResult.getList() == null || pageResult.getList().size() <= 0) {
//					isPageEnd = true;
//				}
//			}
//		} else {
//			updateUIByStatus(TYPE_STATUS_FAILURE);
//		}
//		mList.stopRefresh();
//		onLoadFinished(taskResult);
//	}
//	@Override
//	public final void onErr(TaskResult taskResult) {
//		LogUtil.i(TAG, "===========onErr===========");
//		updateUIByStatus(TYPE_STATUS_FAILURE);
//		mList.stopRefresh();
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		LogUtil.d(TAG, "onResume");
//
//	}
//
// 
//	/**
//	 * 生成ReqInfo
//	 */
//	public abstract void initReqInfo();
//	/**
//	 * 加载数据完成
//	 */
//	public abstract void onLoadFinished(TaskResult taskResult);
//
//	/**
//	 * 创建Adapter
//	 */
//	public abstract BaseListAdapter<?> createListAdapter();
//
//	/**
//	 * ListView头
//	 */
//	public abstract View createHeaderView();
//	public View createMessageView(){
//		return null;
//	}
//
//	
//	@Override
//	public void onSelected(){
//		if(hasData()){
//			return;
//		}
//		if (mCurPage == 0) {
//			reqData(1);
//		}else {
//			reqData(mCurPage);
//		}
//	}
//
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//	
//	private void updateUIByStatus(int status) {
//		updateFootUIByStatus(status);//设置footview
//		loadingView.setVisibility(View.GONE);
//		failView.setVisibility(View.GONE);
//		emptyView.setVisibility(View.GONE);
//		mList.setVisibility(View.GONE);
//		isLoading=false;
//		switch(status){
//		case TYPE_STATUS_NORMAL:
//			mList.setVisibility(View.VISIBLE);
//			break;
//		case TYPE_STATUS_LOADING:
//			if(!hasData()){
//				loadingView.setVisibility(View.VISIBLE);
//			}
//			mList.setVisibility(View.VISIBLE);
//			isLoading=true;
//			break;
//		case TYPE_STATUS_EMPTY:
//			emptyView.setVisibility(View.VISIBLE);
//			break;
//		case TYPE_STATUS_FAILURE:
//			if(hasData()){
//				DialogUtils.showToast(getActivity(),R.string.tip_net_req_failure);
//				mList.setVisibility(View.VISIBLE);
//			}else{
//				failView.setVisibility(View.VISIBLE);
//			}
//			break;
//		}
//	}
//
//	private void updateFootUIByStatus(int status){
//		footView.setVisibility(View.GONE);
//		if(mCurPage>1){
//			if(status==TYPE_STATUS_LOADING){
//					footView.updateView(ListFooterView.TYPE_STATUS_LOADING);
//			}else if(status==TYPE_STATUS_FAILURE){
//				footView.updateView(ListFooterView.TYPE_STATUS_FAILURE);
//			}
//		}
//		
//		
//	}
//	private boolean hasData(){
//		if(mAdapter!=null&&mAdapter.getTotal()>0){
//			return true;
//		}
//		return false;
//	}
//}
