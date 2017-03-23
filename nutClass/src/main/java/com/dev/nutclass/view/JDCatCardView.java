package com.dev.nutclass.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.BrandListActivity;
import com.dev.nutclass.activity.ZeroListActivity;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JDCatCardEntity;
import com.dev.nutclass.entity.JDItemCardEntity;
import com.dev.nutclass.entity.SingleItemCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JDCatCardView extends LinearLayout implements OnClickListener{

	private Context context;
	private LinearLayout pLayout;
	private LinearLayout cLayout;
	
	
	private JDCatCardEntity entity=null;
	
	 
	
	private boolean show=false;
//	private static String selectedP="";
//	private static String selectedC="";
	
	private RecyclerItemClickListener listener;

	public JDCatCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_jd_cat, this);
		pLayout=(LinearLayout) this.findViewById(R.id.ll_p_cat);
		cLayout=(LinearLayout) this.findViewById(R.id.ll_c_cat);
	
		
	}

	public void updateView(final JDCatCardEntity entity, RecyclerItemClickListener listener) {
		this.entity=entity;
		this.listener=listener;
		if(entity==null||entity.getPlist()==null||entity.getPlist().size()<=0){
			setVisibility(View.GONE);
		}
		 
		pLayout.removeAllViews();
		cLayout.removeAllViews();
		
		List<BaseCardEntity> plist=entity.getPlist();
		for (int i = 0; i < plist.size(); i++) {
			final SingleItemCardEntity sEntity=((SingleItemCardEntity)(plist.get(i)));
			View view=LayoutInflater.from(context).inflate(R.layout.view_jd_filter, null);
			LinearLayout.LayoutParams filterParam = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
			filterParam.weight=1;
			view.setOnClickListener(new OnClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(show&&!sEntity.getId().equals(JDCatCardView.this.entity.getSelectedP())){
						cLayout.setVisibility(View.GONE);
						show=false;
					}else{
						show=true;
						cLayout.removeAllViews();
						cLayout.setVisibility(View.VISIBLE);
						List<BaseCardEntity> tmpList=JDCatCardView.this.entity.getClist(sEntity.getId());
						LinearLayout rowLayout=null;
						LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
						for (int i = 0; i < tmpList.size(); i++) {
							LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(DensityUtil.getDisplayWidth(context)/4,LayoutParams.WRAP_CONTENT);
							
							if(i%4==0){
								rowLayout=new LinearLayout(context);
								rowLayout.setLayoutDirection(HORIZONTAL);
							}
							View item=LayoutInflater.from(context).inflate(R.layout.view_text_item,null);
							TextView itemTv=(TextView)item.findViewById(R.id.tv_item);
							itemTv.setText(((SingleItemCardEntity)(tmpList.get(i))).getDesc());
							final SingleItemCardEntity tmpEntity=(SingleItemCardEntity)(tmpList.get(i));
							if(JDCatCardView.this.entity.getSelectedC().equals(tmpEntity.getId())){
								itemTv.setTextColor(Color.RED);
							}else{
								itemTv.setTextColor(getResources().getColor(R.color.color_100));

							}
							item.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (JDCatCardView.this.listener!=null) {
										JDCatCardView.this.listener.onItemClick(v, tmpEntity);
										JDCatCardView.this.entity.setSelectedP(tmpEntity.getTag());
										JDCatCardView.this.entity.setSelectedC(tmpEntity.getId());
									}
								}
							});
							rowLayout.addView(item,cellParams);
							if(i%4==3||i==tmpList.size()-1){
								cLayout.addView(rowLayout,rowParams);
							}
							
						}
					}
				}
			});
			
			
			TextView tv=(TextView)view.findViewById(R.id.tv_name);
			ImageView iv=(ImageView)view.findViewById(R.id.iv_arraw);
			tv.setText(sEntity.getDesc());
			if (this.entity.getSelectedP()!=null&&this.entity.getSelectedP().equals(sEntity.getId())) {
				//iv.setImageResource(R.drawable.tab_arrow_up);
				tv.setTextColor(Color.RED);
			}else{
				tv.setTextColor(getResources().getColor(R.color.color_100));
			//	iv.setImageResource(R.drawable.tab_arrow_down);
			}
			pLayout.addView(view,filterParam);
			
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
}
