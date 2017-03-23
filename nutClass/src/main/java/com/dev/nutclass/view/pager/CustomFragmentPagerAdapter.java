package com.dev.nutclass.view.pager;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dev.nutclass.utils.LogUtil;

/**
 * 修正adapter instantiateItem的策略，防止崩溃后再创建出现的问题
 * 
 * @author Mr.Huang
 * 
 */
public abstract class CustomFragmentPagerAdapter extends PagerAdapter {
	private static final String TAG="CustomFragmentPagerAdapter";
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    public CustomFragmentPagerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    public abstract Fragment getItem(int paramInt);

    @Override
    public void startUpdate(ViewGroup container) {}

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        long itemId = getItemId(position);

        String name = makeFragmentName(itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            mCurTransaction.attach(fragment);
            LogUtil.d(TAG, "attach"+position);
        } else {
        	LogUtil.d(TAG, "add"+position);
            fragment = getItem(position);

            mCurTransaction.add(container.getId(), fragment,makeFragmentName(itemId));
        }

        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        LogUtil.d(TAG, "destroyItem"+position);
        mCurTransaction.detach((Fragment) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            LogUtil.d(TAG, "setPrimaryItem"+position);
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
            LogUtil.d(TAG, "finishUpdate");
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
    	LogUtil.d(TAG, "isViewFromObject");
        return ((Fragment) object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public long getItemId(int position) {
        return position;
    }

    public static String makeFragmentName(long id) {
        return "android:switcher:" + id;
    }
}
