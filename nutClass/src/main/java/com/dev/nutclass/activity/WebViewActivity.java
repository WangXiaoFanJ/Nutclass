package com.dev.nutclass.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.ActionShareEntity;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.view.TitleBar;
import com.dev.nutclass.view.TitleBar.BarClickListener;
import com.squareup.okhttp.Request;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends BaseActivity {
    private final String TAG = "WebViewActivity";
	private Activity mActivity;

	private TitleBar mTitleBar;
	private String mReqUrl;
	private WebView mWebView;

	private String titleH5;
	private String contextH5;
	private String imageH5;
	private String urlH5;
	private String urlParameter;
	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "onCreate");
		mActivity = this;
		setContentView(R.layout.activity_webview);
		mTitleBar = (TitleBar) findViewById(R.id.tb_title);
		initIntent();
		initView();
		initTitleBar();
		initListener();
		initData();
	}

	public void initIntent() {
		// TODO Auto-generated method stub
		if(getIntent().hasExtra(Const.KEY_URL)){
			mReqUrl=getIntent().getStringExtra(Const.KEY_URL);
		} else{
			mReqUrl="https://www.baidu.com/";
		}
		if(getIntent().hasExtra(Const.KEY_TITLE)){
			mTitleBar.setMiddleText(getIntent().getStringExtra(Const.KEY_TITLE));
		}
		if(getIntent().hasExtra(Const.KEY_TITLE_H5)){
			titleH5 = getIntent().getStringExtra(Const.KEY_TITLE_H5);
		}
		if(getIntent().hasExtra(Const.KEY_CONTEXT_H5)){
			contextH5 = getIntent().getStringExtra(Const.KEY_CONTEXT_H5);
		}else{
			contextH5 = "中国教育课程折扣平台";
		}
		if(getIntent().hasExtra(Const.KEY_IMAGE_H5)){
			imageH5 = getIntent().getStringExtra(Const.KEY_IMAGE_H5);
		}else{
			imageH5 = "http://new.kobiko.cn/Public/img/logo.png";
		}
		if(getIntent().hasExtra(Const.KEY_URL_H5)){
			urlH5 = getIntent().getStringExtra(Const.KEY_URL_H5);
		}else{
			urlH5 = "http://new.kobiko.cn/Html5/Index";
		}
		mTitleBar.setTitleRight1(TitleBar.TYPE_SHARE_TXT);
	}
	public void initTitleBar() {
		mTitleBar.setBarClickListener(new TitleBar.BarClickListener() {
			
			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
//				DialogUtils.showToast(mActivity,"分享开始");
				new ShareAction(WebViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
						.withText(contextH5)
						.withTitle(titleH5)
						.withMedia(new UMImage(mActivity,imageH5))
						.withTargetUrl(urlH5)
						.setCallback(new UMShareListener() {
							@Override
							public void onResult(SHARE_MEDIA share_media) {
								if (share_media ==SHARE_MEDIA.WEIXIN_CIRCLE && getIntent().getStringExtra(Const.KEY_FROM)==null){
									String url = "http://new.kobiko.cn/Api/Ld/AppShareFriend";
									OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
										@Override
										public void onError(Request request, Exception e) {
											Log.d("error",e.getMessage());
										}

										@Override
										public void onResponse(String response){
											try {
												JSONObject jsonObject = new JSONObject(response);
												ActionShareEntity entity = new ActionShareEntity();
												entity.optJsonObj(jsonObject);
												if(entity.getStatus().equals("success")){
													DialogUtils.showToast(mActivity,entity.getMsg());
													mWebView.postUrl(mReqUrl, EncodingUtils.getBytes(urlParameter, "base64"));
												}else{
													DialogUtils.showToast(mActivity,entity.getMsg());
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}

									},new OkHttpClientManager.Param("mobile_phone",SharedPrefUtil.getInstance().getMobile()));
								}
							}

							@Override
							public void onError(SHARE_MEDIA share_media, Throwable throwable) {

							}

							@Override
							public void onCancel(SHARE_MEDIA share_media) {

							}
						})
						.share();
				return false;
			}
			
			@Override
			public boolean onClickLeft() {
				// TODO Auto-generated method stub
		        if (mWebView != null &&  mWebView.canGoBack()){
		            // 返回键退回
		        	mWebView.goBack();
		            return true;
		        }else {
		        	mActivity.finish();
				}
				return true;
			}

			@Override
			public boolean onClickMiddle() {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	private void initData() {
		// TODO Auto-generated method stub
		
		loadUrl();
	}

	private void initListener() {
	
	
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		// TODO Auto-generated method stub
		
		mWebView = (WebView) findViewById(R.id.webview);
		
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

	public TitleBar getTitleBar() {
		return mTitleBar;
	}
	public void loadUrl(){
		LogUtil.i(TAG, "mReqUrl="+mReqUrl);

		if (!HttpUtil.isConnected(mActivity)) {
			mWebView.setVisibility(View.GONE);
			return;
		}
		mWebView.setVisibility(View.VISIBLE);

		urlParameter= "userId="+ SharedPrefUtil.getInstance().getUid()+"&"+"token="+SharedPrefUtil.getInstance().getToken()
				+"&"+"mobile_phone="+SharedPrefUtil.getInstance().getMobile();
		Log.d("===","urlParamerter"+urlParameter);
		if(TextUtil.isNotNull(getIntent().getStringExtra(Const.KEY_FROM))){
			mWebView.loadUrl(mReqUrl);
		}else{
			mWebView.postUrl(mReqUrl, EncodingUtils.getBytes(urlParameter, "base64"));
		}

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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null &&  mWebView.canGoBack()){
            // 返回键退回
        	mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
    
	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

}
