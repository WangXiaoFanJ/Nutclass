package com.dev.nutclass.adapter;

import java.util.List;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.UserEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
	private List<UserEntity> list;
	private Context context;
	private RecyclerItemClickListener itemClickListener;
	public UserAdapter(Context context,List<UserEntity> list,RecyclerItemClickListener listener){
		this.list=list;
		this.context=context;
		itemClickListener=listener;
	}
	public UserAdapter(Context context,List<UserEntity> list){
		this.list=list;
		this.context=context;
	}
	public void setItemClickListener(RecyclerItemClickListener listener){
		itemClickListener=listener;
	}
	public class MyViewHolder extends ViewHolder {
		private ImageView dirIv;
		private TextView nameTv;
		private TextView descTv;
		private LinearLayout rootLayout;
		private RecyclerItemClickListener itemClickListener;
		public MyViewHolder(View arg0,RecyclerItemClickListener listener) {
			super(arg0);
			// TODO Auto-generated constructor stub
			dirIv=(ImageView)arg0.findViewById(R.id.iv_icon);
			nameTv=(TextView)arg0.findViewById(R.id.tv_name);
			descTv=(TextView)arg0.findViewById(R.id.tv_address);
			rootLayout=(LinearLayout)arg0.findViewById(R.id.ll_root);
			itemClickListener=listener;
			rootLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if(itemClickListener!=null){
						itemClickListener.onItemClick(view, getAdapterPosition());
					}
				}
			});
			 
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if(list==null){
			return 0;
		}
		return list.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.nameTv.setText(list.get(arg1).getName());
		ImageLoader.getInstance().displayImage(list.get(arg1).getPortrait(), arg0.dirIv);
	}
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View contentView=LayoutInflater.from(context).inflate(R.layout.item_user, null);
		return new MyViewHolder(contentView,itemClickListener);
	}

}
