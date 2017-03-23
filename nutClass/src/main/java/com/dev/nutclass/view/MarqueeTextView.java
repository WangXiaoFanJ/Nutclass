package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 跑马灯的效果默认需要获得焦点
 * 
 * @author Mr.Huang
 * 
 */
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context) {
		super(context);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}
