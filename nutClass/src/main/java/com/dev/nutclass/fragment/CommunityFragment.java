package com.dev.nutclass.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dev.nutclass.R;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.view.TitleBar;

public class CommunityFragment extends BaseFragment {
	private static final String TAG = "CommunityFragment";
	private Activity mActivity;

	private String mReqUrl="http://kobiko.wsq.umeng.com?";
	private WebView mWebView;
//	public CommunityFragment(Activity activity) {
//		// TODO Auto-generated constructor stub
//		LogUtil.d(TAG, "CommunityFragment");
//		mActivity=activity;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mActivity=getActivity();
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_webview, null);
		
		initView(view);
		loadUrl();
		return view;
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initView(View view) {
		// TODO Auto-generated method stub
		mWebView = (WebView) view.findViewById(R.id.webview);
		
		// JavaScript使能(如果要加载的页面中有JS代码，则必须使能JS)
        WebSettings webSettings = mWebView.getSettings();;
        // 设置可以支持缩放 
        webSettings.setSupportZoom(true); 
        // 设置出现缩放工具 
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        
        //设置WebView属性，能够执行Javascript脚本  
        webSettings.setJavaScriptEnabled(true);
        

        // 更强的打开链接控制：自己覆写一个WebViewClient类：除了指定链接从WebView打开，其他的链接默认打开
        mWebView.setWebViewClient(new MyWebViewClient());
		
	}

	
	public void loadUrl(){
		LogUtil.i(TAG, "mReqUrl="+mReqUrl);

		if (!HttpUtil.isConnected(mActivity)) {
			mWebView.setVisibility(View.GONE);
			return;
		}
		mWebView.setVisibility(View.VISIBLE);
		mWebView.loadUrl(mReqUrl);
	}
	
	   /**
     * 自定义的WebViewClient类，将特殊链接从WebView打开，其他链接仍然用默认浏览器打开
     * 
     * @author Mr.Huang
     * 
     */
    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
        	view.loadUrl(url);
            return true;
        }
     }
    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
   
    public boolean back(){
    	if(mWebView != null &&  mWebView.canGoBack()){
    		mWebView.goBack();
    		return true;
    	}else{
    		return false;
    	}
    }
    @Override
	public void onRefresh(boolean b) {
		// TODO Auto-generated method stub
		super.onRefresh(b);
//		loadUrl();
	}
    
	

	

}
