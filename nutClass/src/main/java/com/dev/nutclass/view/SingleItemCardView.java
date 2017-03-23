package com.dev.nutclass.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.entity.SingleItemCardEntity;

public class SingleItemCardView extends LinearLayout implements OnClickListener {

	private Context context;
	private TextView itemTv;
	private TextView flagTv;
	private SingleItemCardEntity entity;
	private RecyclerItemClickListener itemClickListener;
	private LinearLayout containerLayout;

	public SingleItemCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}
	public SingleItemCardView(Context context,SingleItemCardEntity entity) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_single_item, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		itemTv=(TextView)this.findViewById(R.id.tv_item);
		flagTv=(TextView)this.findViewById(R.id.tv_tag);
		containerLayout = (LinearLayout) this.findViewById(R.id.ll_container);
	}
	private void initListener(){
		setOnClickListener(this);
	}
	public void updateView(SingleItemCardEntity entity,RecyclerItemClickListener itemClickListener){
		this.entity=entity;
		this.itemClickListener=itemClickListener;
		if(entity==null){
			setVisibility(View.GONE);
		}
		flagTv.setVisibility(View.GONE);
		if(entity.isRecommend()){
			itemTv.setText(entity.getInputDesc()+entity.getDesc());
		}else if(entity.isShowTag()){
			flagTv.setVisibility(View.VISIBLE);
			itemTv.setText(entity.getDesc());
		}else{
			itemTv.setText(entity.getDesc());
		}
		flagTv.setVisibility(View.GONE);
		setBackgroundColor(getResources().getColor(R.color.color_0));
		if(entity.isParent()){
			if(entity.isSelected()){
				setBackgroundColor(getResources().getColor(R.color.color_0));
//				containerLayout.setBackgroundColor(getResources().getColor(R.color.color_0));
			}else{
				setBackgroundColor(getResources().getColor(R.color.color_10));
//				containerLayout.setBackgroundColor(getResources().getColor(R.color.color_0));
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(entity==null||TextUtils.isEmpty(entity.getDesc())){
			return;
		}
		if(itemClickListener!=null){
			itemClickListener.onItemClick(v, entity);
		}
	}

}
