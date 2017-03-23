package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.CommentCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.StorageUtil;
import com.dev.nutclass.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentCardView extends LinearLayout {
	private static final String TAG = "CommentCardView";
	private Context context;

	private TextView nameTv;
	private TextView timeTv;
	private TextView descTv;
	private RoundedImageView iconIv;

	public CommentCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public CommentCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	//
	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.view_comment_card, this);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		timeTv = (TextView) this.findViewById(R.id.tv_time);
		descTv = (TextView) this.findViewById(R.id.tv_desc);
		iconIv = (RoundedImageView) this.findViewById(R.id.iv_icon);
	}

	public void updateView(CommentCardEntity entity) {
		if (entity == null) {
			return;
		}
		nameTv.setText(entity.getName());
		descTv.setText(entity.getDesc());
 		timeTv.setText(TextUtil.getStandardDate(entity.getTime()));
		ImageLoader.getInstance().displayImage(entity.getAvater(),
				iconIv, ImgConfig.getPortraitOption());
	}

}
