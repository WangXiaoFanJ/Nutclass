package com.dev.nutclass.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.TextUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public abstract class BaseActivity extends FragmentActivity {
	private final String TAG = "BaseActivity";

	public String mPageTitle;//本页的Title名

	public boolean userPresent = true;
	public boolean beBackground = false;
	//手势返回
	// button event
	public final static int RIGHT_BUTTON = 0;
	public final static int LEFT_BUTTON = 1;
	private static final int FLING_MIN_VELOCITY = 1000;// 移动最小速度
    private int mTouchSlop;
    boolean isDrag = false;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;
    private MotionEvent mCurrentDownEvent;
    
    private BroadcastReceiver mReceiver;
    public void unregisterReceiver(){
    	if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
		mReceiver = null;
    }
    public void registerReceiver(){
    	String[] filters = {Const.ACTION_BROADCAST_LOGIN_SUCC,Const.ACTION_BROADCAST_USERINFO_CHANGE,Const.ACTION_BROADCAST_FEED_CHANGED,Const.ACTION_BROADCAST_FEED_CREATE};
    	registerReceiver(filters);
    }
    public void registerReceiver(String[] filers){
    	//unregisterReceiver();
		mReceiver=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals(Const.ACTION_BROADCAST_LOGIN_SUCC)) {
					loginSucc();
					LogUtil.i(TAG, "send success log");
				}else if (intent.getAction().equals(Const.ACTION_BROADCAST_USERINFO_CHANGE)) {
					userInfoChange();
				}else if (intent.getAction().equals(Const.ACTION_BROADCAST_FEED_CHANGED)) {
					String feedid = intent.getStringExtra(Const.KEY_ID);
					String type = intent.getIntExtra(Const.KEY_TYPE,0)+"";
					LogUtil.i(TAG, "feedid:id="+feedid+",type="+type);
					//type=0删除，type=1：赞，type=2：评论，type=3，关注，
					if (TextUtil.isNotNull(feedid)) {
						refreshUI(feedid,type);
					}
				}else if (intent.getAction().equals(Const.ACTION_BROADCAST_FEED_CREATE)) {
					feedCreatedSucc();
				}
			}
		};
		if (filers != null && filers.length >0) {
			IntentFilter filter=new IntentFilter();
			for (int i = 0; i < filers.length; i++) {
				String action = filers[i];
				LogUtil.i(TAG, "registerReceiver:action"+action);
				filter.addAction(action);	
			}
			
			registerReceiver(mReceiver, filter);
		}
	
    }
	//子类可以重载这个方法，刷新界面
	public void refreshUI(String id,String type){}
    
	public void feedCreatedSucc(){}
	
    public void loginSucc(){}
    
    public void userInfoChange(){}
    
	/**
	 * 从SwitchUser登录成功返回后所作的工作（第一次初始化）
	 */
	protected void onInitActivity() {

	}

	/**
	 * 从SwitchUser登录成功返回后所作的工作（已经初始化）
	 */
	protected void onUpdateActivity() {

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
MobclickAgent.onResume(getApplicationContext());
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver();
		ApplicationConfig.getInstance().removeActivity(this);
		super.onDestroy();

		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final ViewConfiguration configuration = ViewConfiguration.get(getApplicationContext());
        mTouchSlop = getResources().getDimensionPixelSize(R.dimen.dis_touch_slop);
        
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		ApplicationConfig.getInstance().addActivity(this);
//		 //透明状态栏  
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
//        //透明导航栏  
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);  
	}


    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG, "onTrackballEvent:"+event.getAction());
		return super.onTrackballEvent(event);
	}

	 

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (isOnGestureBack(event)) {
			final int action = event.getAction() & MotionEventCompat.ACTION_MASK;
			
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(event);
			
			switch (action) {
				case MotionEvent.ACTION_DOWN: {
					
					if (mCurrentDownEvent != null) {
		                mCurrentDownEvent.recycle();
		            }
		            mCurrentDownEvent = MotionEvent.obtain(event);

					break;
				}
				case MotionEvent.ACTION_MOVE: {
					if (mCurrentDownEvent == null) {
						mCurrentDownEvent = MotionEvent.obtain(event);
		            }
					
					final float x = MotionEventCompat.getX(event, 0);
					final float xDiff = x - mCurrentDownEvent.getX();
					final float y = MotionEventCompat.getY(event, 0);
					final float yDiff = Math.abs(y - mCurrentDownEvent.getY()) * 2;
					if ((xDiff > (mTouchSlop * 3)) && (xDiff > yDiff)) {
						isDrag = true;
					}
					
					break;
				}
				case MotionEvent.ACTION_UP: {
					if (isDrag) {

						final VelocityTracker velocityTracker = mVelocityTracker;
	                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
	                    final float xVelocity = (int) velocityTracker.getXVelocity();

						isDrag = false;
						
						if (mVelocityTracker != null) {
			                mVelocityTracker.recycle();
			                mVelocityTracker = null;
			            }
						
						if (xVelocity > FLING_MIN_VELOCITY) {
							onGestureBack();
							event.setAction(MotionEvent.ACTION_CANCEL);
						}
					}
					break;
				}
				
				case MotionEvent.ACTION_CANCEL: {
					mVelocityTracker.recycle();
			        mVelocityTracker = null;
			        isDrag = false;
		            break;
				}
			}
		}
    	
		return super.dispatchTouchEvent(event);
	}
    
    private boolean onGestureBackEnable = true;
   	public boolean isOnGestureBackEnable() {
   		return onGestureBackEnable;
   	}

   	public void setOnGestureBackEnable(boolean onGestureBackEnable) {
   		this.onGestureBackEnable = onGestureBackEnable;
   	}
	/**
     * 子类是否响应手势返回
     * @return 默认返回true，支持手势返回。
     */
    protected boolean isOnGestureBack(MotionEvent event) {
    	return onGestureBackEnable;
    }
    
	protected abstract void handleTitleBarEvent(int eventId);
    /**
     * 子类可以重写此方法来处理手势返回时需要进行的操作；
     * 默认与返回键一致
     */
    protected void onGestureBack() {
//    	handleTitleBarEvent(LEFT_BUTTON);
    	finish();
    }

}
