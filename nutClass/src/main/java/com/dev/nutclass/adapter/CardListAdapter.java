package com.dev.nutclass.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.activity.FeedUserPageActivity;
import com.dev.nutclass.activity.HomeActivity;
import com.dev.nutclass.entity.ActionCardEntity;
import com.dev.nutclass.entity.ActionInfoCardEntity;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.AgeCardEntity;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CommentCardEntity;
import com.dev.nutclass.entity.CooponCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.FeedCardEntity;
import com.dev.nutclass.entity.HeadCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.JDCatCardEntity;
import com.dev.nutclass.entity.JDItemCardEntity;
import com.dev.nutclass.entity.MarketCardEntity;
import com.dev.nutclass.entity.MarketInfoCardEntity;
import com.dev.nutclass.entity.MerchantCardEntity;
import com.dev.nutclass.entity.SchoolCardEntity;
import com.dev.nutclass.entity.SimpleTextEntity;
import com.dev.nutclass.entity.SingleItemCardEntity;
import com.dev.nutclass.entity.ZeroCardEntity;
import com.dev.nutclass.view.ActionCardView;
import com.dev.nutclass.view.ActionInfoCardView;
import com.dev.nutclass.view.AdCard001View;
import com.dev.nutclass.view.AdCard002View;
import com.dev.nutclass.view.AdCard003View;
import com.dev.nutclass.view.AdCard004View;
import com.dev.nutclass.view.AdCardView;
import com.dev.nutclass.view.AgeCardView;
import com.dev.nutclass.view.BannerCardView;
import com.dev.nutclass.view.BrandCardView;
import com.dev.nutclass.view.CommentCardView;
import com.dev.nutclass.view.CooponCardView;
import com.dev.nutclass.view.CourseCardView;
import com.dev.nutclass.view.CourseInfoCardView;
import com.dev.nutclass.view.CoursePayCardView;
import com.dev.nutclass.view.FeedCardView;
import com.dev.nutclass.view.FeedUserCardView;
import com.dev.nutclass.view.HeadCardView;
import com.dev.nutclass.view.JDCardView;
import com.dev.nutclass.view.JDCatCardView;
import com.dev.nutclass.view.MarketCardView;
import com.dev.nutclass.view.MarketInfoCardView;
import com.dev.nutclass.view.MerchantAppMView;
import com.dev.nutclass.view.MerchantCardView;
import com.dev.nutclass.view.SchoolCardView;
import com.dev.nutclass.view.SimpleTextCardView;
import com.dev.nutclass.view.SingleItemCardView;
import com.dev.nutclass.view.WaitingPayView;
import com.dev.nutclass.view.ZeroCardView;

public class CardListAdapter extends
		RecyclerView.Adapter<CardListAdapter.MyViewHolder> {
	private List<BaseCardEntity> list;
	private List<String> plusPriceList;
	private CourseInfoCardView courseInfoCardView;
	private Context context;
	private RecyclerItemClickListener itemClickListener;

	public CardListAdapter(Context context, List<BaseCardEntity> list,
			RecyclerItemClickListener listener) {
		this.list = list;
		this.context = context;
		itemClickListener = listener;
	}

	public CardListAdapter(Context context, List<BaseCardEntity> list) {
		this.list = list;
		this.context = context;
		plusPriceList = new ArrayList<>();
	}
	public void addList(List<BaseCardEntity> tmplist){
		if(list==null){
			list=new ArrayList<BaseCardEntity>();
		}
		int start=list.size();
		list.addAll(tmplist);
		notifyItemRangeChanged(start, tmplist.size());
	}
	public List<BaseCardEntity> getList(){
		return list;
	}
	public void addItem(BaseCardEntity entity,int pos){
		list.add(pos, entity);
		notifyItemInserted(pos);
	}
	public void removeItem(int pos){
		list.remove(pos);
		notifyItemRemoved(pos);
	}
	public void setItemClickListener(RecyclerItemClickListener listener) {
		itemClickListener = listener;
	}

	public class MyViewHolder extends ViewHolder {
		View view = null;

		public MyViewHolder(View arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
			view = arg0;
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

	public List<String> getPlusPriceList(){
		plusPriceList.clear();
		plusPriceList.addAll(courseInfoCardView.getPlusGoodsIdList());
		Log.d("===","plusGoodsIdListsss:"+plusPriceList.size());
		return plusPriceList;
	}
	public String getSchoolHour(){
		return courseInfoCardView.getSchoolHour();
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (list == null || position < 0 || position >= list.size()) {
			if (position == 0) {
				return position;
			} else {
				return -1;
			}
		}
		int type = list.get(position).getCardType();
		return type;
	}

	@Override
	public void onBindViewHolder(MyViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		int type = list.get(arg1).getCardType();
		if (type == BaseCardEntity.CARD_TYPE_BANNER||type==BaseCardEntity.CARD_TYPE_JD_BANNER) {
			BannerCardEntity entity = (BannerCardEntity) list.get(arg1);
			((BannerCardView) (arg0.view)).updateView(entity,1);
		} else if (type == BaseCardEntity.CARD_TYPE_AD1) {
//			((AdCardView) (arg0.view)).updateView();
		}else if(type==BaseCardEntity.CARD_TYPE_AD2){
//			((AdCardView2) (arg0.view)).updateView();
		}else if (type == BaseCardEntity.CARD_TYPE_COURSE) {
			CourseCardEntity entity = (CourseCardEntity) list.get(arg1);
			if(context instanceof CourseInfoActivity){
				((CourseInfoCardView) (arg0.view)).updateView(entity);
				courseInfoCardView = ((CourseInfoCardView) (arg0.view));
			}else{
				((CourseCardView) (arg0.view)).updateView(entity,itemClickListener);
			}
		}else if (type == BaseCardEntity.CARD_TYPE_SIMPLE_TEXT) {
			SimpleTextEntity entity = (SimpleTextEntity) list.get(arg1);
			((SimpleTextCardView) (arg0.view)).updateView(entity,itemClickListener);
		}else if (type == BaseCardEntity.CARD_TYPE_BRAND) {
			ImageEntity entity = (ImageEntity) list.get(arg1);
			((BrandCardView) (arg0.view)).updateView(entity);
		}else if (type == BaseCardEntity.CARD_TYPE_ACTION) {
			ActionCardEntity entity = (ActionCardEntity) list.get(arg1);
			((ActionCardView) (arg0.view)).updateView(entity);
		}else if (type == BaseCardEntity.CARD_TYPE_SINGLE_ITEM) {
			SingleItemCardEntity entity = (SingleItemCardEntity) list.get(arg1);
			((SingleItemCardView) (arg0.view)).updateView(entity,itemClickListener);
		}else if (type == BaseCardEntity.CARD_TYPE_COMMENT) {
			CommentCardEntity entity = (CommentCardEntity) list.get(arg1);
			((CommentCardView) (arg0.view)).updateView(entity);
		}else if (type == BaseCardEntity.CARD_TYPE_ACTION_INFO) {
			ActionInfoCardEntity entity = (ActionInfoCardEntity) list.get(arg1);
			((ActionInfoCardView) (arg0.view)).updateView(entity);
		}else if (type == BaseCardEntity.CARD_TYPE_ZERO) {
			ZeroCardEntity entity = (ZeroCardEntity) list.get(arg1);
			((ZeroCardView) (arg0.view)).updateView(entity);
		}else if (type == BaseCardEntity.CARD_TYPE_MARKET) {
			MarketCardEntity entity = (MarketCardEntity) list.get(arg1);
			((MarketCardView) (arg0.view)).updateView(entity);
		}else if (type == BaseCardEntity.CARD_TYPE_MARKET_INFO) {
			MarketInfoCardEntity entity = (MarketInfoCardEntity) list.get(arg1);
			((MarketInfoCardView) (arg0.view)).updateView(entity);
		}else if (type == BaseCardEntity.CARD_TYPE_AD) {
			AdCardEntity entity = (AdCardEntity) list.get(arg1);
			((AdCardView) (arg0.view)).updateView(entity);
		}else if(type==BaseCardEntity.CARD_TYPE_SCHOOL||type==BaseCardEntity.CARD_TYPE_SCHOOL_LIST){
			SchoolCardEntity entity=(SchoolCardEntity)list.get(arg1);
			((SchoolCardView) (arg0.view)).updateView(entity);
			
		}else if(type==BaseCardEntity.CARD_TYPE_HEAD){
			HeadCardEntity entity=(HeadCardEntity)list.get(arg1);
			((HeadCardView) (arg0.view)).updateView(entity);
			
		}else if(type==BaseCardEntity.CARD_TYPE_FEED){
			FeedCardEntity entity=(FeedCardEntity)list.get(arg1);
			if(context instanceof FeedUserPageActivity){
				((FeedUserCardView) (arg0.view)).updateView(entity);
			}else{
				((FeedCardView) (arg0.view)).updateView(entity,itemClickListener);
			//	((FeedCardView) (arg0.view)).setFeedClickListener(listener);
			}
			
			
		}else if(type==BaseCardEntity.CARD_TYPE_COOPON){
			CooponCardEntity entity=(CooponCardEntity)list.get(arg1);
			((CooponCardView) (arg0.view)).updateView(entity);
			
		}else if(type==BaseCardEntity.CARD_TYPE_AGE){
			AgeCardEntity entity=(AgeCardEntity)list.get(arg1);
			((AgeCardView) (arg0.view)).updateView(entity);
		}else if(type==BaseCardEntity.CARD_TYPE_AD_001){
			AdCardEntity entity = (AdCardEntity) list.get(arg1);
			((AdCard001View) (arg0.view)).updateView(entity);
		}else if(type==BaseCardEntity.CARD_TYPE_AD_002){
			AdCardEntity entity = (AdCardEntity) list.get(arg1);
			((AdCard002View) (arg0.view)).updateView(entity);
		}else if(type==BaseCardEntity.CARD_TYPE_AD_003){
			AdCardEntity entity = (AdCardEntity) list.get(arg1);
			((AdCard003View) (arg0.view)).updateView(entity);
		}else if(type==BaseCardEntity.CARD_TYPE_AD_004){
			AdCardEntity entity = (AdCardEntity) list.get(arg1);
			((AdCard004View) (arg0.view)).updateView(entity);
		}else if(type==BaseCardEntity.CARD_TYPE_JD_CAT_CARD){
			JDCatCardEntity entity = (JDCatCardEntity) list.get(arg1);
			((JDCatCardView) (arg0.view)).updateView(entity,itemClickListener);
		}else if(type==BaseCardEntity.CARD_TYPE_JD_DOUBLE_CARD){
			JDItemCardEntity entity = (JDItemCardEntity) list.get(arg1);
			((JDCardView) (arg0.view)).updateView(entity);
		}else if(type==BaseCardEntity.CARD_TYPE_COURSE_PAY){
			CourseCardEntity entity = (CourseCardEntity) list.get(arg1);
			((CoursePayCardView) (arg0.view)).updateView(entity,arg1,itemClickListener);
		}else if(type ==BaseCardEntity.CARD_TYPE_WAITING_PAY){
			CourseCardEntity entity = (CourseCardEntity) list.get(arg1);
			((WaitingPayView)(arg0.view)).updateView(entity);
		}
		else if(type==BaseCardEntity.CARD_TYPE_MERCHANT_ORDER){
			MerchantCardEntity entity = (MerchantCardEntity) list.get(arg1);
			((MerchantCardView)(arg0.view)).updateView(entity);
		}else if (type == BaseCardEntity.CARD_TYPE_MERCHANT_APPOITNMENT){
			MerchantCardEntity entity = (MerchantCardEntity) list.get(arg1);
			((MerchantAppMView)(arg0.view)).updataView(entity);
		}
	}
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View view = null;
		if (arg1== BaseCardEntity.CARD_TYPE_BANNER||arg1==BaseCardEntity.CARD_TYPE_JD_BANNER) {
			if(context instanceof HomeActivity&&arg1== BaseCardEntity.CARD_TYPE_BANNER){
				view = new BannerCardView(context,1);
			}else if(arg1== BaseCardEntity.CARD_TYPE_JD_BANNER){
				view = new BannerCardView(context,2);
			}else{
				view = new BannerCardView(context,3);
			}
			
		} else if (arg1 == BaseCardEntity.CARD_TYPE_AD1) {
			view = new AdCardView(context);
		} else if (arg1 == BaseCardEntity.CARD_TYPE_AD2) {
//			 view=new AdCardView2(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_COURSE) {
			if(context instanceof CourseInfoActivity){
				view = new CourseInfoCardView(context);
			}else{
				view = new CourseCardView(context);
			}
		}else if (arg1 == BaseCardEntity.CARD_TYPE_SIMPLE_TEXT) {
			view = new SimpleTextCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_BRAND) {
			view = new BrandCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_ACTION) {
			view = new ActionCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_SINGLE_ITEM) {
			view = new SingleItemCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_COMMENT) {
			view = new CommentCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_ACTION_INFO) {
			view = new ActionInfoCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_ZERO) {
			view = new ZeroCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_MARKET) {
			view = new MarketCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_MARKET_INFO) {
			view = new MarketInfoCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_AD) {
			view = new AdCardView(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_SCHOOL||arg1==BaseCardEntity.CARD_TYPE_SCHOOL_LIST){
			view=new SchoolCardView(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_HEAD){
			view=new HeadCardView(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_FEED){
			if(context instanceof FeedUserPageActivity){
				view=new FeedUserCardView(context);
			}else{
				view=new FeedCardView(context);
			}
			
		}else if(arg1==BaseCardEntity.CARD_TYPE_COOPON){
			view=new CooponCardView(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_AGE){
			view=new AgeCardView(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_AD_001){
			view=new AdCard001View(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_AD_002){
			view=new AdCard002View(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_AD_003){
			view=new AdCard003View(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_AD_004){
			view=new AdCard004View(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_JD_CAT_CARD){
			view=new JDCatCardView(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_JD_DOUBLE_CARD){
			view=new JDCardView(context);
		}else if(arg1==BaseCardEntity.CARD_TYPE_COURSE_PAY){
			view=new CoursePayCardView(context);
		}else if(arg1 ==BaseCardEntity.CARD_TYPE_WAITING_PAY){
			view = new WaitingPayView(context);
		}
		else if(arg1 == BaseCardEntity.CARD_TYPE_MERCHANT_ORDER){
			view = new MerchantCardView(context);
		}else if (arg1 == BaseCardEntity.CARD_TYPE_MERCHANT_APPOITNMENT){
			view = new MerchantAppMView(context);
		}
		return new MyViewHolder(view);
	}

}
