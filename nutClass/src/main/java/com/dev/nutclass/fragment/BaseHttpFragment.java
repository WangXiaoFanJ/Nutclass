package com.dev.nutclass.fragment;
//package com.dev.beautydiary.fragment;
//
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import com.dev.beautydiary.R;
//import com.dev.beautydiary.parser.IParser;
//import com.dev.beautydiary.request.ITaskFinishListener;
//import com.dev.beautydiary.request.RequestTask;
//import com.dev.beautydiary.request.TaskParams;
//import com.dev.beautydiary.request.TaskResult;
//import com.dev.beautydiary.utils.DialogUtils;
//import com.dev.beautydiary.utils.LogUtil;
//
//
//
//public abstract class BaseHttpFragment extends BaseFragment {
//    private final String TAG = "BaseHttpFragment";
//    
//    
//	private FrameLayout containLayout;
//	private View loadingView;
//	private View failView;
//	private View emptyView;
//	
//	private boolean isLoading = false;// 是否正在加载数据
//	private boolean autoLoading = false;// 是否自动加载数据
//	private RequestTask reqTask;
//	private String reqUrl;
//	private IParser parser;
//	
//	private String httpMethod = RequestTask.HTTP_GET;
//	private List<NameValuePair> paramsList = null;
//	
//	/**
//	 * 设置Fragment属性
//	 * 
//	 * @param reqUrl      接口地址
//	 * @param mIParser    解析器
//	 */
//	public void setHttpReqInfo(String reqUrl, IParser mIParser,boolean isAutoLoad) {
//		this.httpMethod = RequestTask.HTTP_GET;
//		this.reqUrl = reqUrl;
//		this.parser = mIParser;
//		this.paramsList = null;
//		this.autoLoading = isAutoLoad;
//	}
//	
//	public void setPostHttpReqInfo(String reqUrl, IParser mIParser,List<NameValuePair> paramsList,boolean isAutoLoad) {
//		this.reqUrl = reqUrl;
//		this.parser = mIParser;
//		this.httpMethod = RequestTask.HTTP_POST;
//		this.paramsList = paramsList;
//		this.autoLoading = isAutoLoad;
//	}
//	
//	@Override
//	public final void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		LogUtil.d(TAG, "onCreate");
//		initIntent();
//		initReqInfo();
//	}
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//		LogUtil.d(TAG, "onCreateView");
//		View v = inflater.inflate(R.layout.fragment_base_http, container, false);
//		initView(v);
//		initListener();
//		return v;
//	}
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		LogUtil.d(TAG, "onViewCreated");
//		super.onViewCreated(view, savedInstanceState);
//		if (autoLoading) {
//			initData();
//		}
//	}
//
//	@Override
//	public void onDestroyView() {
//		LogUtil.d(TAG, "onDestroyView");
//		super.onDestroyView();
//	}
//
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		LogUtil.d(TAG, "onActivityResult");
//		super.onActivityResult(requestCode, resultCode, data);
//
//	}
//	private void initData() {
//		// TODO Auto-generated method stub
//		LogUtil.i(TAG, "send success log");
//		reqData();
//	}
//
//	private void initListener() {
//		// TODO Auto-generated method stub
//		failView.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				reqData();
//			}
//		});
//		
//	}
//	private void initView(View root) {
//		// TODO Auto-generated method stub
//		containLayout = (FrameLayout)root.findViewById(R.id.container);
//		loadingView = root.findViewById(R.id.view_loading);
//		failView = root.findViewById(R.id.view_failed);
//		emptyView = root.findViewById(R.id.view_empty);
//
//		//加入具体的显示View
//		View view = getView();
//		if (view != null) {
//			containLayout.setVisibility(View.VISIBLE);
//			containLayout.removeAllViews();
//			containLayout.addView(view);
//		}else {
//			containLayout.setVisibility(View.GONE);
//		}
//	
//	}
//
//	@Override
//	public void onDestroy() {
//		LogUtil.d(TAG, "onDestroy");
//
//		if (reqTask != null) {
//			reqTask.cancel(true);
//		}
//
//		super.onDestroy();
//	}
//	private void reqData() {
//		if (isLoading) return;
//		// 用户详情接口
//		String url = reqUrl;
//		try {
//			if (url != null) {
//				reqTask.cancel(true);
//			}
//			reqTask = new RequestTask(getActivity(), parser);
//			reqTask.setTaskFinishListener(mTaskFinishListener);
//			reqTask.setExtra(1);
//			TaskParams params = new TaskParams();
//			params.put(RequestTask.PARAM_URL, url);
//			if (RequestTask.HTTP_POST.equals(httpMethod)) {
//				params.put(RequestTask.PARAM_HTTP_METHOD, RequestTask.HTTP_POST);
//				reqTask.setPostParams(paramsList);
//			}else {
//				params.put(RequestTask.PARAM_HTTP_METHOD, RequestTask.HTTP_GET);	
//			}
//			reqTask.setTaskParams(params);
//			reqTask.addToQueue(params);
//			updateUIByStatus(TYPE_STATUS_LOADING);
//		} catch (Exception e) {
//			e.printStackTrace();
//			updateUIByStatus(TYPE_STATUS_FAILURE);
//		}
//
//	}
//
//	ITaskFinishListener mTaskFinishListener = new ITaskFinishListener() {
//
//		@Override
//		public void onSucc(TaskResult taskResult) {
//			// TODO Auto-generated method stub
//			LogUtil.d(TAG, "onTaskFinished......................");
//			if (taskResult.retObj == null) {
//				updateUIByStatus(TYPE_STATUS_EMPTY);
//			}else {
//				updateUIByStatus(TYPE_STATUS_NORMAL);
//			}
//			onLoadFinished(taskResult);
//		}
//		@Override
//		public void onErr(TaskResult taskResult) {
//			// TODO Auto-generated method stub
//			updateUIByStatus(TYPE_STATUS_FAILURE);
//		}
//	};
//
//	
//	private void updateUIByStatus(int status) {
//		loadingView.setVisibility(View.GONE);
//		failView.setVisibility(View.GONE);
//		emptyView.setVisibility(View.GONE);
//		containLayout.setVisibility(View.GONE);
//		isLoading=false;
//		switch(status){
//		case TYPE_STATUS_NORMAL:
//			containLayout.setVisibility(View.VISIBLE);
//			break;
//		case TYPE_STATUS_LOADING:
//			isLoading=true;
//			containLayout.setVisibility(View.VISIBLE);
//			loadingView.setVisibility(View.VISIBLE);
//			break;
//		case TYPE_STATUS_EMPTY:
//			emptyView.setVisibility(View.VISIBLE);
//			break;
//		case TYPE_STATUS_FAILURE:
////			if(loadWithContext){
////				DialogUtils.showToast(getActivity(),R.string.tip_net_req_failure);
////				containLayout.setVisibility(View.VISIBLE);
////			}else{
////				failView.setVisibility(View.VISIBLE);
////			}
//			failView.setVisibility(View.VISIBLE);
//			break;
//		}
//	}
//
// 
//
//
//	/**
//	 * 初始化Intent信息
//	 */
//	public void initIntent() {}
//	/**
//	 * 初始化请求信息
//	 */
//	public abstract void initReqInfo();
//	/**
//	 * 加载数据完成
//	 */
//	public abstract void onLoadFinished(TaskResult taskResult);
//	/**
//	 * 显示的view
//	 * @return
//	 */
//	public abstract View getView();
//	
//
//
//}
//
