package com.dev.nutclass.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.activity.BigImgActivity;
import com.dev.nutclass.activity.CircleInfoActivity;
import com.dev.nutclass.activity.CircleReleaseActivity;
import com.dev.nutclass.activity.FeedUserPageActivity;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.CommentCardEntity;
import com.dev.nutclass.entity.FeedCardEntity;
import com.dev.nutclass.entity.ShareEntity;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

public class FeedCardView extends LinearLayout implements OnClickListener {
	private static final String TAG = "FeedCardView";
	private Context context;

	private RoundedImageView iconIv;
	private TextView nameTv;
	private TextView descTv;
	private TextView timeTv;
	private AblumsRectView albumsView;
	private LinearLayout commentLayout;

	private ImageView likeIv;
	private ImageView commentIv;
	private ImageView shareIv;
	
	private LinearLayout likeLayout;
	private TextView likeTv;
	
	private TextView delTv;

	private LinearLayout contentLayout;
	private ImageView cameraIv;
	
	private FeedCardEntity entity;

	public FeedCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_feed, this);

		iconIv = (RoundedImageView) this.findViewById(R.id.iv_icon);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		timeTv = (TextView) this.findViewById(R.id.tv_time);
		descTv = (TextView) this.findViewById(R.id.tv_desc);
		delTv = (TextView) this.findViewById(R.id.tv_del);
		commentLayout = (LinearLayout) this.findViewById(R.id.ll_comment);
		likeIv = (ImageView) this.findViewById(R.id.iv_like);
		commentIv = (ImageView) this.findViewById(R.id.iv_comment);
		shareIv = (ImageView) this.findViewById(R.id.iv_share);
		albumsView=(AblumsRectView)this.findViewById(R.id.album_view);
		contentLayout = (LinearLayout) this.findViewById(R.id.ll_container);
		cameraIv = (ImageView) this.findViewById(R.id.iv_camera);
		
		likeLayout=(LinearLayout) this.findViewById(R.id.ll_like);
		likeTv = (TextView) this.findViewById(R.id.tv_like);
	}

	private void initListener() {
		iconIv.setOnClickListener(this);
		nameTv.setOnClickListener(this);
		likeIv.setOnClickListener(this);
		commentIv.setOnClickListener(this);
		shareIv.setOnClickListener(this);
		cameraIv.setOnClickListener(this);
		delTv.setOnClickListener(this);
		setOnClickListener(this);
	}

	// 完善信息
	public void updateView(final FeedCardEntity tmpEntity,RecyclerItemClickListener listener) {
		this.entity = tmpEntity;

		this.listener=listener;
		// Author
		ImageLoader.getInstance().displayImage(entity.getHeadImgUrl(), iconIv);
		nameTv.setText(entity.getName());

		// 描述
		if (TextUtils.isEmpty(entity.getContent())) {
			descTv.setVisibility(View.GONE);
		} else {
			descTv.setVisibility(View.VISIBLE);
			descTv.setText(entity.getContent());
		}
		// 图片
		if (entity.getImgList() != null) {
			albumsView.setShowAddView(false);
			albumsView.updateUI(entity.getImgList());
			albumsView.setVisibility(View.VISIBLE);
		}else{
			albumsView.setVisibility(View.GONE);
		}
		timeTv.setText(entity.getTime());
		
		albumsView
		.setOnItemClickListener(new AblumsRectView.OnItemClickListener() {

			@Override
			public void deleteItemClicked(int pos) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void albumItemClicked(int pos) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, BigImgActivity.class);
				intent.putExtra(Const.KEY_ENTITY_LIST, entity.getImgList());
				intent.putExtra(Const.KEY_POSITION, pos);
				context.startActivity(intent);
				// DialogUtils.showToast(context, "等确定需求");
			}

			@Override
			public void addItemClicked() {
				// TODO Auto-generated method stub
			}
		});
		//赞
		updateLike(true);
		// 评论
		updateComment();
		if(entity.isSelfProfile()){
			contentLayout.setVisibility(View.GONE);
			cameraIv.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(UrlConst.BASE_NET_HOST+entity.getHeadImgUrl(), iconIv);
		}else{
			contentLayout.setVisibility(View.VISIBLE);
			cameraIv.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(entity.getHeadImgUrl(), iconIv);
		}
		if(context instanceof CircleInfoActivity){
			if(entity.getUid()!=null&&entity.getUid().equals(SharedPrefUtil.getInstance().getUid())){
				delTv.setVisibility(View.VISIBLE);
			}else{
				delTv.setVisibility(View.GONE);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == iconIv || v == nameTv) {
			Intent intent = new Intent(context, FeedUserPageActivity.class);
			intent.putExtra(Const.KEY_TITLE, this.entity.getName());
			intent.putExtra(Const.KEY_UID, this.entity.getUid());
			context.startActivity(intent);
		} else if (v == likeIv) {
			if (Util.checkLogin(context)) {
				entity.setIslike(!entity.isIslike());
				reqLike();
				updateLike(false);
				if(context instanceof CircleInfoActivity){
					Intent intent = new Intent();
					intent.setAction(Const.ACTION_BROADCAST_FEED_CHANGED);
					intent.putExtra(Const.KEY_ENTITY,entity);
					intent.putExtra(Const.KEY_TYPE, FeedCardEntity.TYPE_LIKE);
					intent.setPackage(ApplicationConfig.getInstance()
							.getPackageName());
					context.sendBroadcast(intent);
				}
				
			}
		} else if (v == shareIv) {
			if(Util.checkLogin(context)){
				ShareEntity entity=new ShareEntity();
				entity.setDesc(this.entity.getContent());
				entity.setTitle("课比课");
				
				if(this.entity.getImgList()!=null&&this.entity.getImgList().size()>0){
					entity.setImg(this.entity.getImgList().get(0).getSmallPath());
				}else{
					entity.setImg("http://new.kobiko.cn/Public/img/logo.png");
				}
				
				entity.setUrl("http://new.kobiko.cn/Html5/Index");
				SnsUtil.getInstance(context).openShare((Activity) context,entity);
			}
			
		}else if(v==cameraIv){
			if(Util.checkLogin(context)){
				Intent intent = new Intent(context, CircleReleaseActivity.class);
				context.startActivity(intent);
			}
			
		}else if(v==commentIv){
			if(Util.checkLogin(context)){
				Intent intent = new Intent(context, CircleInfoActivity.class);
				intent.putExtra(Const.KEY_ID, this.entity.getId());
				context.startActivity(intent);
//				if(listener!=null){
//					listener.onItemClick(this, entity);
//				}
			}
			
			
		}else if(v==delTv){
			if(Util.checkLogin(context)){
				reqDel();
			}
			
		} else {
			if(context instanceof CircleInfoActivity){
				
			}else{
				Intent intent = new Intent(context, CircleInfoActivity.class);
				intent.putExtra(Const.KEY_ID, this.entity.getId());
				context.startActivity(intent);
			}
			
		}
	}

	private void updateLike(boolean isInit) {
		if(isInit==false){
			if(entity.isIslike()){
				entity.addLiker();
				
			}else{
				entity.removeLiker();
			}
		}
		if(entity.isIslike()){
			likeIv.setImageResource(R.drawable.icon_liked);
			
		}else{
			likeIv.setImageResource(R.drawable.icon_like);
		}
		if(entity.getUserList()==null||entity.getUserList().size()==0){
			likeLayout.setVisibility(View.GONE);
		}else{
			likeLayout.setVisibility(View.VISIBLE);
			String str="";
			for(int i=0;i<entity.getUserList().size();i++){
				str+=entity.getUserList().get(i).getName();
				str+=" ";
			}
			likeTv.setText(str);
		}

		
	}

	private void updateComment() {
		commentLayout.removeAllViews();
		List<CommentCardEntity> commentList=entity.getCommentList();
		if(commentList!=null&&commentList.size()>0){
			for (int i = 0; i < commentList.size(); i++) {
				addComment(commentList.get(i));
			}
		}
	}
	private void addComment(CommentCardEntity commentEntity){
		TextView tv=new TextView(context);
		tv.setTextColor(context.getResources().getColor(R.color.color_40));
		Spannable str=TextUtil.highLightText(context, commentEntity.getName()+":"+commentEntity.getDesc(), commentEntity.getName(), R.color.color_blue_light);
		tv.setText(str);
		
		LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		params.leftMargin=5;
		params.rightMargin=5;
		commentLayout.addView(tv,params);
	}

	/**
	 * @param isLike
	 *            请求赞
	 * */
	private void reqLike() {
		String url = "";
		url = String.format(HttpUtil.addBaseGetParams(UrlConst.GET_ARTICAL_LIKE),entity.getId());

		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						 
					}
				});
	}
	/**
	 * 添加评论
	 *          
	 * */
	private void reqComment() {
		String url = "";
		url = String.format(HttpUtil.addBaseGetParams(UrlConst.GET_ARTICAL_COMMENT), entity.getId(),"I am comment");

		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
					}
				});
	}
	/**
	 * 添加评论
	 *          
	 * */
	private void reqDel() {
		
		String url = "";
		url = String.format(HttpUtil.addBaseGetParams(UrlConst.GET_ARTICAL_DELETE), entity.getId());

		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
						DialogUtils.showToast(context, "删除失败");
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						DialogUtils.showToast(context, "删除成功");
						Intent intent = new Intent();
						intent.setAction(Const.ACTION_BROADCAST_FEED_CHANGED);
						intent.putExtra(Const.KEY_ENTITY, entity);
						intent.putExtra(Const.KEY_TYPE, FeedCardEntity.TYPE_DEL);
						intent.setPackage(ApplicationConfig.getInstance()
								.getPackageName());
						context.sendBroadcast(intent);
						((Activity)context).finish();
					}
				});
	}
	public RecyclerItemClickListener listener;
//	
//	public interface FeedClickListener{
//		public void comment(String feedId);
//	}
//	public void setFeedClickListener(FeedClickListener listener){
//		this.listener=listener;
//	}
	
}
