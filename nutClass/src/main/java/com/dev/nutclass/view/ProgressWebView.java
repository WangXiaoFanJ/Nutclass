package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 * @author Mr.Huang
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
    private ProgressBar progressbar;
    public ProgressWebView(Context context) {
        super(context);
        initWebView();
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWebView();
    }
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }
    
    private void initWebView(){
        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        // 设置进度颜色
//		Drawable drawable = getResources().getDrawable(R.drawable.progressbar);
//		drawable.setBounds(progressbar.getProgressDrawable().getBounds());
//		progressbar.setProgressDrawable(drawable);
//		progressbar.setIndeterminateDrawable(drawable);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3, 0, 0));
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());	
    };

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        	setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
    
    public void setProgress(int newProgress){
    	if (newProgress >= 100) {
            progressbar.setVisibility(GONE);
        } else {
            if (progressbar.getVisibility() == GONE)
                progressbar.setVisibility(VISIBLE);
            progressbar.setProgress(newProgress);
        }
    }
}