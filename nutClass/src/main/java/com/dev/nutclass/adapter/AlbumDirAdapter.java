package com.dev.nutclass.adapter;

import java.util.List;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.ImageEntity;
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

public class AlbumDirAdapter extends RecyclerView.Adapter<AlbumDirAdapter.MyViewHolder>{
	private List<ImageEntity> list;
	private Context context;
	private RecyclerItemClickListener itemClickListener;
	public AlbumDirAdapter(Context context,List<ImageEntity> list,RecyclerItemClickListener listener){
		this.list=list;
		this.context=context;
		itemClickListener=listener;
	}
	public AlbumDirAdapter(Context context,List<ImageEntity> list){
		this.list=list;
		this.context=context;
	}
	public void setItemClickListener(RecyclerItemClickListener listener){
		itemClickListener=listener;
	}
	public class MyViewHolder extends ViewHolder {
		private ImageView dirIv;
		private TextView nameTv;
		private TextView numTv;
		private LinearLayout rootLayout;
		private RecyclerItemClickListener itemClickListener;
		public MyViewHolder(View arg0,RecyclerItemClickListener listener) {
			super(arg0);
			// TODO Auto-generated constructor stub
			dirIv=(ImageView)arg0.findViewById(R.id.iv_dir);
			nameTv=(TextView)arg0.findViewById(R.id.tv_name);
			numTv=(TextView)arg0.findViewById(R.id.tv_num);
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
		arg0.numTv.setText(list.get(arg1).getCount()+"");
		ImageLoader.getInstance().displayImage("file://"+list.get(arg1).getImgPath(), arg0.dirIv);
	}
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View contentView=LayoutInflater.from(context).inflate(R.layout.item_album_dir, null);
		return new MyViewHolder(contentView,itemClickListener);
	}

}
