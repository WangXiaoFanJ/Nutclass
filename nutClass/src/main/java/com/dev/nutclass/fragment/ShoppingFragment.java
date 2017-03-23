package com.dev.nutclass.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CardListActivity;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.activity.PayActivityNew;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.adapter.ShoppingCarListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.db.NutClassDB;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.DBEntity;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.view.TitleBar;

public class ShoppingFragment extends BaseFragment implements OnClickListener{
	private static final String TAG = "ShoppingFragment";
	private ShoppingCarListAdapter adapter;
	private TitleBar titleBar;
	private boolean isEdit=false;
	private Context mContext;
	private ListView listView;
	private CheckBox checkBoxAll;
	private Button accountBtn;
	private TextView totalPriceTv;
	private Double totalPrice;
	private Map<Integer,Integer> map = new HashMap<>();
	List<BaseCardEntity> courseList;
	private LinearLayout shoppingCarLayout;
	private TextView lookLookTv;
	public ShoppingFragment() {
		// TODO Auto-generated constructor stub
		LogUtil.d(TAG, "ShoppingFragment");
	}
	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == 0){
				float price = (Float)msg.obj;
				if(price > 0){
					totalPriceTv.setText("￥"+price+"");
				}else{
					totalPriceTv.setText("￥0");
				}
			}else if(msg.what==1){
				shoppingCarLayout.setVisibility(View.GONE);
				lookLookTv.setVisibility(View.VISIBLE);
			}
			return false;
		}
	});
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_shopping, null);
		titleBar=(TitleBar)view.findViewById(R.id.tb_title);
		listView = (ListView) view.findViewById(R.id.list_view);
		checkBoxAll = (CheckBox) view.findViewById(R.id.checkbox_all);
		accountBtn = (Button) view.findViewById(R.id.btn_account);
		totalPriceTv = (TextView) view.findViewById(R.id.total_price_tv);
		lookLookTv = (TextView) view.findViewById(R.id.tv_look_look);
		shoppingCarLayout = (LinearLayout) view.findViewById(R.id.rl_shopping_car);


		lookLookTv.setOnClickListener(this);
		checkBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (checkBoxAll.isChecked()) {
					for (int i = 0; i < courseList.size(); i++) {
						map.put(i, i);
					}
				} else {
					map.clear();
				}
				adapter.getTotalPrice();
				adapter.notifyDataSetChanged();
			}
		});
		accountBtn.setOnClickListener(this);
//		buyTv=(TextView)view.findViewById(R.id.tv_pay);
//		buyTv.setVisibility(View.GONE);
//		buyTv.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				StringBuffer sb=new StringBuffer();
//				for(int i=0;i<adapter.getList().size();i++){
//					sb.append(((CourseCardEntity)(adapter.getList().get(i))).getRetIds());
//					if(i<(adapter.getList().size()-1)){
//						sb.append(",");
//					}
//				}
//				reqOrder(sb.toString());
//			}
//		});
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {
			
			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				isEdit=!isEdit;
				reqData(isEdit);
				return true;
			}
			
			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onClickMiddle() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onClickLeft() {
				// TODO Auto-generated method stub
				return false;
			}
		});
		String[] filters = { Const.ACTION_BROADCAST_LOGIN_SUCC,
				Const.ACTION_BROADCAST_FEED_CHANGED,
				Const.ACTION_BROADCAST_RECEIVE_MESSAGE,
				Const.ACTION_BROADCAST_FEED_RELEASE_CHANGED };
		registerReceiver(filters);
		reqData(false);
		return view;
	}

	public void update(List<BaseCardEntity> list) {
		
		if (list!=null&&list.size()>0) {
			shoppingCarLayout.setVisibility(View.VISIBLE);
			listView.setVisibility(View.VISIBLE);
			lookLookTv.setVisibility(View.GONE);
			adapter = new ShoppingCarListAdapter(map,list,mContext,checkBoxAll,handler,new RecyclerItemClickListener(){
				@Override
				public void onItemClick(View view, Object obj) {
					int position=-1;
					CourseCardEntity entity=(CourseCardEntity)obj;
					for (int i = 0; i < adapter.getList().size(); i++) {
						CourseCardEntity e=(CourseCardEntity)adapter.getList().get(i);
						if(entity.getDbId()==e.getDbId()){
							position=i;
						}
					}
					NutClassDB.getInstance(getActivity()).deleteById(entity.getDbId());
					if(position>=0){
						adapter.removeItem(position);
					}
				}
			});
//			adapter = new CardListAdapter(getActivity(), list,new RecyclerItemClickListener() {
//
//				@Override
//				public void onItemClick(View view, Object obj) {
//					// TODO Auto-generated method stub
////					NutClassDB.getInstance(getActivity()).deleteById(((CourseCardEntity)(adapter.getList().get(position))).getDbId());
//					int position=-1;
//					CourseCardEntity entity=(CourseCardEntity)obj;
//					for (int i = 0; i < adapter.getList().size(); i++) {
//						CourseCardEntity e=(CourseCardEntity)adapter.getList().get(i);
//						if(entity.getDbId()==e.getDbId()){
//							position=i;
//						}
//					}
//					NutClassDB.getInstance(getActivity()).deleteById(entity.getDbId());
//					if(position>=0){
//						adapter.removeItem(position);
//					}
//
//
//				}
//
//
//			});

//			cardListView.setAdapter(adapter);
			listView.setAdapter(adapter);
//			LinearLayoutManager mLayoutManager = new LinearLayoutManager(
//					getActivity());
//			mLayoutManager.setOrientation(LinearLayout.VERTICAL);
//			cardListView.setLayoutManager(mLayoutManager);
//			buyTv.setVisibility(View.GONE);
		} else {
//			buyTv.setVisibility(View.GONE);
		}
	}

	@Override
	public void onRefresh(boolean b) {
		// TODO Auto-generated method stub
		super.onRefresh(b);
		reqData(false);
	}

	/*
	 * 从数据库获取当前用户的购物车数据
	 */
	private void reqData(boolean isEdit) {
		this.isEdit=isEdit;
		List<DBEntity> list =NutClassDB.getInstance(getActivity()).getDBListByType(DBEntity.TYPE_COURSE_SHOPPING);
		if(list!=null&&list.size()>0){
//			cardListView.setVisibility(View.VISIBLE);
			courseList=new ArrayList<BaseCardEntity>();
			for(int i=0;i<list.size();i++){
				DBEntity entity=list.get(i);
				CourseCardEntity courseEntity=new CourseCardEntity();
				JSONObject courseObj;
				try {
					courseObj = new JSONObject(entity.getJsonStr());
					courseEntity.setFrom(CardListActivity.TYPE_FROM_SHOPPING);
					courseEntity.optJsonObj(courseObj);
					courseEntity.setEdit(isEdit);
					courseEntity.setDbId(entity.getId());
					courseList.add(courseEntity);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(courseList.size()>0){
				update(courseList);
			}
		}else{

		}
		if(isEdit){
			titleBar.setTitleRight2(3);
		}else{
			titleBar.setTitleRight2(11);
		}
	}

	@Override
	public void onClick(View v) {
		if(v == lookLookTv){
			Intent intent=new Intent(mContext,CourseListActivity.class);
			intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_SCHOOL);
			intent.putExtra(Const.KEY_ID, "0");
			intent.putExtra(Const.KEY_TITLE,"周边");
			intent.putExtra(Const.KEY_AGE, "2");
			mContext.startActivity(intent);
		}else if (v == accountBtn){
			String retIds = adapter.getPayOrderInfo();
			String plusPriceBuyInfo = adapter.getPlusPriceBuyInfo();
			if(map.size()==0){
				DialogUtils.showToast(mContext,"请选择课程");
			}else{
				Intent intent = new Intent(mContext,PayActivityNew.class);
				intent.putExtra(Const.KEY_ORDER_INFO,retIds);
				if(TextUtil.isNotNull(plusPriceBuyInfo)){
					intent.putExtra(Const.KEY_PLUS_PRICE_BUY,plusPriceBuyInfo);
				}
				startActivity(intent);
			}

		}
	}
}
