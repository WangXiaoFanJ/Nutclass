package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CooponCardEntity;
import com.dev.nutclass.entity.CooponNumEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.fragment.BaseFragment;
import com.dev.nutclass.fragment.BrandFragment;
import com.dev.nutclass.fragment.CircleFragment;
import com.dev.nutclass.fragment.CommunityFragment;
import com.dev.nutclass.fragment.CooponListFragment;
import com.dev.nutclass.fragment.HomeFragmentNew;
import com.dev.nutclass.fragment.MeFragment;
import com.dev.nutclass.fragment.ShoppingFragment;
import com.dev.nutclass.parser.CardListParser;
import com.dev.nutclass.parser.HomeListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.HomeNavView;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

public class CooponListActivity extends BaseActivity implements
		View.OnClickListener {
	private Context context;
	private final static String TAG = "CooponListActivity";

	private FrameLayout container;
	private CooponListFragment fragment1;// 未使用
	private CooponListFragment fragment2;// 已过期
	private CooponListFragment fragment3;// 已使用

	private HomeNavView navView;

	private BaseFragment curFragment;

	private TitleBar titleBar;

	private TextView leftTv;
	private View leftLine;
	private TextView rightTv;
	private View rightLine;
	private TextView middleTv;
	private View middleLine;

	private View filterLayout;

	private CooponNumEntity numEntity;
	private List<BaseCardEntity> cooponList;
	private List<BaseCardEntity> cooponList1 = new ArrayList<BaseCardEntity>();
	private List<BaseCardEntity> cooponList2 = new ArrayList<BaseCardEntity>();
	private List<BaseCardEntity> cooponList3 = new ArrayList<BaseCardEntity>();

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coopon);
		context = this;
		initView();
		initData();
		initListener();
		Util.initAppData(context);
		setOnGestureBackEnable(false);
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		navView = (HomeNavView) findViewById(R.id.view_nav);
		container = (FrameLayout) findViewById(R.id.container);

		leftTv = (TextView) findViewById(R.id.tv_left);
		leftLine = (View) findViewById(R.id.view_left);
		middleTv = (TextView) findViewById(R.id.tv_center);
		middleLine = (View) findViewById(R.id.view_center);
		rightTv = (TextView) findViewById(R.id.tv_right);
		rightLine = (View) findViewById(R.id.view_right);
	}

	private void initListener() {

		leftTv.setOnClickListener(this);
		middleTv.setOnClickListener(this);
		rightTv.setOnClickListener(this);
	}

	private void initData() {

		container.removeAllViews();
		reqData();

	}

	private void reqData() {
		String url = UrlConst.GET_COOPON_LIST;

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
						CardListParser parser = new CardListParser();
						JsonDataList<BaseCardEntity> result = (JsonDataList<BaseCardEntity>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							cooponList = result.getList();
							numEntity = (CooponNumEntity) result.getObj();
							leftTv.setText("未使用(" + numEntity.getNotUseNum()
									+ ")");
							middleTv.setText("已使用(" + numEntity.getUsedNum()
									+ ")");
							rightTv.setText("已过期(" + numEntity.getExpiredNum()
									+ ")");
							if (cooponList != null && cooponList.size() > 0) {
								for (int i = 0; i < cooponList.size(); i++) {
									int type = ((CooponCardEntity) cooponList
											.get(i)).getType();
									if (type == 1) {
										cooponList2.add(cooponList.get(i));
									} else if (type == 2) {
										cooponList1.add(cooponList.get(i));
									} else if (type == 3) {
										cooponList3.add(cooponList.get(i));
									}
								}
							}
							updateTab(0);
						}
					}
				});
	}

	public void switchFragment(int to) {
		LogUtil.i(TAG, "switchFragment to:" + to);

		switch (to) {
		case 0:
			if (fragment1 == null) {
//				fragment1 = new CooponListFragment(cooponList1);
				fragment1 = CooponListFragment.newInstance();
				fragment1.setCooponList(cooponList1);
			}
			switchContent(fragment1);
			break;
		// case 1: // 动态
		// if (categoryFragment == null) {
		// categoryFragment = new CategoryFragment();
		// }
		//
		// switchContent(categoryFragment);
		// break;

		case 1:
			if (fragment2 == null) {
//				fragment2 = new CooponListFragment(cooponList2);
				fragment2 = CooponListFragment.newInstance();
				fragment2.setCooponList(cooponList2);
			}
			switchContent(fragment2);
			break;
		case 2:
			if (fragment3 == null) {
//				fragment3 = new CooponListFragment(cooponList3);
				fragment3 = CooponListFragment.newInstance();
				fragment3.setCooponList(cooponList3);
			}
			switchContent(fragment3);
			break;

		default:
			break;
		}
	}

	/**
	 * fragment 切换
	 * **/
	public void switchContent(BaseFragment to) {
		if (curFragment != to) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			if (!to.isAdded()) { //
				if (curFragment == null) {

					transaction.add(R.id.container, to).commit(); //
				} else {
					transaction.hide(curFragment).add(R.id.container, to)
							.commit(); //

				}
				// if(to==communityFragment){
				// communityFragment.onRefresh(false);
				// }
			} else {
				if (curFragment == null) {
					transaction.show(to).commit(); //
				} else {
					transaction.hide(curFragment).show(to).commit(); //
				}

				// if(to==homeFragment){
				// homeFragment.updateLocation();
				// }
				// else if(to==categoryFragment){
				// categoryFragment.updateLocation();
				// }else if(to==shoppingFragment){
				// curFragment.onRefresh(false);
				// }
			}
			curFragment = to;
		} else {
			// 双击同一个Tab，刷新Fragment数据
			if (curFragment != null) {
				curFragment.onRefresh(false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == leftTv) {
			updateTab(0);
		} else if (v == middleTv) {
			updateTab(1);
		} else if (v == rightTv) {
			updateTab(2);
		}

	}

	/**
	 * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// // Check if the key event was the Back button and if there's history
		// if ((keyCode == KeyEvent.KEYCODE_BACK)
		// &&curFragment==communityFragment){
		// // 返回键退回
		// if(communityFragment.back()){
		// return true;
		// }
		// }
		// If it wasn't the Back key or there's no web page history, bubble up
		// to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}

	public void updateTab(int type) {
		leftLine.setVisibility(View.INVISIBLE);
		middleLine.setVisibility(View.INVISIBLE);
		rightLine.setVisibility(View.INVISIBLE);
		leftTv.setTextColor(getResources().getColor(R.color.color_60));
		middleTv.setTextColor(getResources().getColor(R.color.color_60));
		rightTv.setTextColor(getResources().getColor(R.color.color_60));
		if (type == 0) {
			leftTv.setTextColor(getResources().getColor(R.color.color_orange));
			leftLine.setVisibility(View.VISIBLE);
		} else if (type == 1) {
			middleTv.setTextColor(getResources().getColor(R.color.color_orange));
			middleLine.setVisibility(View.VISIBLE);
		} else if (type == 2) {
			rightTv.setTextColor(getResources().getColor(R.color.color_orange));
			rightLine.setVisibility(View.VISIBLE);
		}
		switchFragment(type);
	}
}
