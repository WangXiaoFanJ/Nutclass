package com.dev.nutclass.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dev.nutclass.constants.Const;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.TextUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Fragment基类 用于基本的广播的接收与数据的统计
 * 
 * @author LJ
 * 
 */
public abstract class BaseFragment extends Fragment {
	private static final String TAG = "BaseFragment";
	public static final int TYPE_STATUS_NORMAL = 1;
	public static final int TYPE_STATUS_LOADING = 2;
	public static final int TYPE_STATUS_FAILURE = 3;
	public static final int TYPE_STATUS_EMPTY = 4;
	private BroadcastReceiver mReceiver;

	public void registerReceiver(String[] filers) {
		unregisterReceiver();
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction()
						.equals(Const.ACTION_BROADCAST_LOGIN_SUCC)) {
					updateUserInfo(true);
					LogUtil.i(TAG, "send success log");
				} else if (intent.getAction().equals(
						Const.ACTION_BROADCAST_USERINFO_CHANGE)) {
					updateUserInfo(true);

				} else if (intent.getAction().equals(
						Const.ACTION_BROADCAST_RECEIVE_MESSAGE)) {// 收到消息
					String content = "";
					messageRec(content);
				} else if (intent.getAction().equals(
						Const.ACTION_BROADCAST_FEED_CHANGED)) {
					Object obj = intent.getSerializableExtra(Const.KEY_ENTITY);
					
					String type = intent.getIntExtra(Const.KEY_TYPE,0)+"";
					LogUtil.i(TAG, "type="+type);
					//type=0删除，type=1：赞，type=2：评论，type=3，关注，
					if (obj!=null) {
						refreshUI(obj,type);
					}
					// 依据data里面的state来判断是何种状态
					// refreshReleaseUI(data);
				} else if (intent.getAction().equals(
						Const.ACTION_BROADCAST_FEED_RELEASE_CHANGED)) {
					 
				}else if (intent.getAction().equals(
						Const.ACTION_BROADCAST_FEED_CREATE)) {
					feedCreatedSucc();
					 
				}
			}
		};

		if (filers != null && filers.length > 0) {
			IntentFilter filter = new IntentFilter();
			for (int i = 0; i < filers.length; i++) {
				String action = filers[i];
				LogUtil.i(TAG, "registerReceiver:action" + action);
				filter.addAction(action);
			}

			getActivity().registerReceiver(mReceiver, filter);
		}

	}

	public void unregisterReceiver() {
		if (mReceiver != null) {
			getActivity().unregisterReceiver(mReceiver);
		}
		mReceiver = null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();MobclickAgent.onResume(getActivity());

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(getActivity());

	}

	// 多Fragment切换的时候调用
	public void onSelected() {
	}

	// 更新用户信息
	public void updateUserInfo(boolean isBackGround) {
	}

	// 子类可以重载这个方法，刷新Feed状态
	public void refreshUI(Object obj, String type){
		
	}

	// 重新刷新页面
	public void refreshPage(Object obj) {

	}

	// 处理接收消息
	public void messageRec(String text) {
	}

	// 子类可以重载这个方法，处理Feed状态
	// public void refreshReleaseUI(FailureBoxEntity entity){}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onRefresh(boolean b) {
		// TODO Auto-generated method stub

	}
	public void feedCreatedSucc(){}
	 
}
