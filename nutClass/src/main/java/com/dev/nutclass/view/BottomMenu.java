package com.dev.nutclass.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dev.nutclass.R;

public class BottomMenu extends PopupWindow implements OnClickListener {
	private OnItemClickedListener mListener;

	/**
	 * 用于默认的图片选择
	 * 
	 * */
	public BottomMenu(Context context) {
		super(context);
		String[] salaryArray = context.getResources().getStringArray(R.array.set_photo_array);
		init(context, salaryArray, null);
	}
	public BottomMenu(Context context, OnItemClickedListener listener) {
		super(context);
		String[] salaryArray = context.getResources().getStringArray(R.array.set_photo_array);
		init(context, salaryArray, listener);
	}

	public BottomMenu(Context context, String[] items, OnItemClickedListener listener) {
		super(context);
		init(context, items, listener);
	}
	
	private void init(Context context, String[] items, OnItemClickedListener listener){
		mListener = listener;
		LayoutInflater mInflater = LayoutInflater.from(context);
		View convertView = mInflater.inflate(R.layout.bottom_menu, null);
		View gap = convertView.findViewById(R.id.gap);
//		gap.getBackground().setAlpha(40);  
		gap.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				dismiss();
				return false;
			}
		});
		LinearLayout container = (LinearLayout)convertView.findViewById(R.id.container);
//		LinearLayout root = new LinearLayout(context);
//		root.setLayoutParams(new LinearLayout.LayoutParams(
//				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		container.setOrientation(LinearLayout.VERTICAL);
		container.setBackgroundColor(Color.parseColor("#f5f7fa"));
		int paddingH=context.getResources().getDimensionPixelSize(R.dimen.common_12);
		int paddingV=context.getResources().getDimensionPixelSize(R.dimen.common_8);
		container.setPadding(paddingH, paddingV, paddingH, paddingV);
		
		setContentView(convertView);
		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		setAnimationStyle(R.style.market_popup_menu_animation);

//		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.menu_background));
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bottom_menu_bg));
		for (int i = 0; i < items.length; i++) {
			String item = items[i];
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

			View view = mInflater.inflate(R.layout.bottom_menu_item, null);
			TextView txt = (TextView) view.findViewById(R.id.bottom_menu_name);
//			if(i==items.length-1){
//				txt.setBackgroundResource(R.drawable.selector_corner_round_btn_noboard_bg);
//			}else{
//				txt.setBackgroundResource(R.drawable.selector_corner_round_btn_bg);
//			}
			txt.setBackgroundResource(R.drawable.selector_button_with_board);
			txt.setText(item);
			txt.setOnClickListener(this);
			txt.setTag(i);

			container.addView(view, params);

//			if (i != items.length - 1&&false) {
//				LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
//						LayoutParams.FILL_PARENT, 1);
//				LinearLayout divider = new LinearLayout(context);
//				divider.setBackgroundResource(R.color.bottom_popup_divider);
//				root.addView(divider, dividerParams);
//			}
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (mListener != null) {
			mListener.onItemClicked((Integer) v.getTag());
		}
	}

	public OnItemClickedListener getOnItemClickedListener() {
		return mListener;
	}
	public void setOnItemClickedListener(OnItemClickedListener mListener) {
		this.mListener = mListener;
	}

	public interface OnItemClickedListener {
		void onItemClicked(int which);
	}
}
