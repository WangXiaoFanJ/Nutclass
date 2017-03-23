package com.dev.nutclass.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.FQEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FQView extends LinearLayout {

	private Context context;

	// radioGroup1
	private RadioGroup rg1;
	private RadioGroup rg2;
	private LinearLayout payLayout;
	private LinearLayout creditLayout;
	private LinearLayout okLayout;
	private DrawableCenterTextView pay1Tv;
	private DrawableCenterTextView pay2Tv;
	private DrawableCenterTextView pay3Tv;

	private Button okBtn;

	private Button cancelBtn;

	private int fq1 = 0;
	private int fq2 = 0;
	private List<FQEntity> entity;

	public FQView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.view_fq, this);
		rg1 = (RadioGroup) this.findViewById(R.id.radioGroup1);
		rg2 = (RadioGroup) this.findViewById(R.id.radioGroup2);
		payLayout = (LinearLayout) this.findViewById(R.id.ll_pay_type);
		okBtn = (Button) this.findViewById(R.id.btn_ok);
		cancelBtn = (Button) this.findViewById(R.id.btn_cancel);
		
		creditLayout=(LinearLayout)this.findViewById(R.id.ll_pay_credit);
        
        okLayout=(LinearLayout)this.findViewById(R.id.ll_ok);
        
        
        pay1Tv=(DrawableCenterTextView)this.findViewById(R.id.tv_pay1);
        pay2Tv=(DrawableCenterTextView)this.findViewById(R.id.tv_pay2);
        pay3Tv=(DrawableCenterTextView)this.findViewById(R.id.tv_pay3);
        
	}

	// type=0 详情页
	// tyoe=1 支付页
	@SuppressLint("NewApi")
	public void updateView(List<FQEntity> entity, int type,final DialogItemClick listener) {
		rg1.removeAllViews();

		this.entity = entity;
		if (entity == null) {
			return;
		}
		
		if(type==0){
			payLayout.setVisibility(View.GONE);
		}else if(type==1){
			creditLayout.setVisibility(View.GONE);
	        okLayout.setVisibility(View.GONE);
		}
		
		pay1Tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.ok("3", 0, 0);
				}
			}
		});
        
        pay2Tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.ok("5", 0, 0);
				}
			}
		});
        pay3Tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				creditLayout.setVisibility(View.VISIBLE);
				okLayout.setVisibility(View.VISIBLE);
			}
		});

		for (int i = 0; i < entity.size(); i++) {
			RadioButton rb = new RadioButton(context);
			rb.setText(entity.get(i).getName());
			rb.setTag(i);
			rg1.addView(rb);
			if (fq1 == i) {
			
				rb.setChecked(true);
			}else{
				rb.setChecked(false);
			}
			
		}
		 
		
		updateFQ();
		rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton rb=(RadioButton) FQView.this.findViewById(group.getCheckedRadioButtonId());
				
				fq1 = Integer.parseInt(rb.getTag().toString());
				fq2 = 0;
				updateFQ();
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.d("fgview", fq1
						+ ":"+ fq2);
				if (listener!=null) {
					listener.ok(FQView.this.entity.get(fq1).getId(),fq1,fq2);
				}
				
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (listener!=null) {
					listener.cancel();
				}
				
			}
		});

	}

	private void updateFQ() {
		rg2.removeAllViews();
		for (int i = 0; i < entity.get(fq1).getList().size(); i++) {
			RadioButton rb = new RadioButton(context);
			rb.setText(entity.get(fq1).getList().get(i).getName());
			rb.setTag(i);
			
			rg2.addView(rb);
			if (fq2 == i) {
				rb.setChecked(true);
			}else{
				rb.setChecked(false);
			}
		}
		rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton rb=(RadioButton) FQView.this.findViewById(group.getCheckedRadioButtonId());
				
				fq2 = Integer.parseInt(rb.getTag().toString());
				

			}
		});
	}
	
	public interface DialogItemClick{
		public void ok(String type,int fq1,int fq2);
		public void cancel();
		
	}
	
	

}
