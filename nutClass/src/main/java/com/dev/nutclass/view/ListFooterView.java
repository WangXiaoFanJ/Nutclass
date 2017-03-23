package com.dev.nutclass.view;

import com.dev.nutclass.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ListFooterView extends LinearLayout {
	public static final int TYPE_STATUS_LOADING = 1;
	public static final int TYPE_STATUS_FAILURE = 2;
	public static final int TYPE_STATUS_MORE = 3;
	private int type = 1;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private View loadingLayout;
	private View loadFailureLayout;
	private View clickMoreLayout;

	public ListFooterView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ListFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_listview_footer,
				this);
		loadingLayout = this.findViewById(R.id.ll_more_loading);
		loadFailureLayout = this.findViewById(R.id.tv_more_failure);
		clickMoreLayout = this.findViewById(R.id.tv_more_see);
	}

	public void updateView(int type) {
		this.type = type;
		setVisibility(View.VISIBLE);
		loadingLayout.setVisibility(View.GONE);
		loadFailureLayout.setVisibility(View.GONE);
		clickMoreLayout.setVisibility(View.GONE);
		switch (type) {
		case TYPE_STATUS_LOADING:
			loadingLayout.setVisibility(View.VISIBLE);
			break;
		case TYPE_STATUS_FAILURE:
			loadFailureLayout.setVisibility(View.VISIBLE);
			break;
		case TYPE_STATUS_MORE:
			clickMoreLayout.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
