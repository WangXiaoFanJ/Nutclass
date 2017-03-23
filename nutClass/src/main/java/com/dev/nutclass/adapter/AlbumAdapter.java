package com.dev.nutclass.adapter;

import java.util.List;

import com.dev.nutclass.R;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.LogUtil;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlbumAdapter extends
		RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
	private static final String TAG = "AlbumAdapter";
	private List<String> list;
	private Context context;
	private RecyclerItemClickListener itemClickListener;
	private MyViewHolder selectedHolder = null;
	private int selectedIndex = 1;

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public AlbumAdapter(Context context, List<String> list,
			RecyclerItemClickListener listener) {
		this.list = list;
		this.context = context;
		itemClickListener = listener;
		selectedHolder = null;
	}

	public AlbumAdapter(Context context, List<String> list) {
		this.list = list;
		this.context = context;
		selectedHolder = null;
	}

	public void setItemClickListener(RecyclerItemClickListener listener) {
		itemClickListener = listener;
	}

	public class MyViewHolder extends ViewHolder {
		private ImageView albumIv;
		private ImageView albumFlagIv;
		private RelativeLayout rootLayout;
		private RecyclerItemClickListener itemClickListener;

		public MyViewHolder(View arg0, RecyclerItemClickListener listener) {
			super(arg0);
			// TODO Auto-generated constructor stub
			albumIv = (ImageView) arg0.findViewById(R.id.iv_album);
			albumFlagIv = (ImageView) arg0.findViewById(R.id.iv_album_flag);
			rootLayout = (RelativeLayout) arg0.findViewById(R.id.rl_root);
			itemClickListener = listener;
			rootLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (itemClickListener != null) {
						itemClickListener.onItemClick(view,
								getAdapterPosition());
						if (getAdapterPosition() != 0) {
							notifyItemChanged(selectedIndex);
							notifyItemChanged(getAdapterPosition());
							selectedIndex = getAdapterPosition();
						}
						// albumFlagIv.setSelected(true);
						// selectedIndex=getAdapterPosition();
						// selectedHolder.albumFlagIv.setSelected(false);
						// selectedHolder=MyViewHolder.this;
					}
				}
			});

		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		if (arg1 == 0) {
			LogUtil.d(TAG, "drawable");
			arg0.albumIv.setImageResource(Integer.parseInt(list.get(arg1)));
			arg0.albumFlagIv.setVisibility(View.GONE);
		} else {
			ImageLoader.getInstance().displayImage("file://" + list.get(arg1),
					arg0.albumIv, ImgConfig.getAlbumImgOption());
			arg0.albumFlagIv.setVisibility(View.VISIBLE);
			if (selectedIndex == arg1) {
				arg0.albumFlagIv.setSelected(true);
				selectedHolder = arg0;
			} else {
				arg0.albumFlagIv.setSelected(false);
			}
		}

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.item_album, null);
		return new MyViewHolder(contentView, itemClickListener);
	}

}
