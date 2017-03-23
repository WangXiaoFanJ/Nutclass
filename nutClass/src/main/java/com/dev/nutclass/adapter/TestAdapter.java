package com.dev.nutclass.adapter;

import java.util.List;

import com.dev.nutclass.R;
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

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder>{
	private List<String> list;
	private Context context;
	private RecyclerItemClickListener itemClickListener;
	public TestAdapter(Context context,List<String> list,RecyclerItemClickListener listener){
		this.list=list;
		this.context=context;
		itemClickListener=listener;
	}
	public TestAdapter(Context context,List<String> list){
		this.list=list;
		this.context=context;
	}
	public void setItemClickListener(RecyclerItemClickListener listener){
		itemClickListener=listener;
	}
	public class MyViewHolder extends ViewHolder {
		private TextView tv;
		private RecyclerItemClickListener itemClickListener;
		public MyViewHolder(View arg0,RecyclerItemClickListener listener) {
			super(arg0);
			// TODO Auto-generated constructor stub
			 tv=(TextView) arg0;
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
		arg0.tv.setText("这是啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊"+arg1);
	}
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
//		View contentView=LayoutInflater.from(context).inflate(R.layout.item_album_dir, null);
		TextView tv=new TextView(context);
		return new MyViewHolder(tv,itemClickListener);
	}

}
