package com.dev.nutclass.view.pager;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dev.nutclass.fragment.BaseFragment;


/**
 * 简单的ViewPager的adapter，支持加入标题栏
 * 
 * @author Mr.Huang
 * 
 */
public class FragmentPagerAdapter extends CustomFragmentPagerAdapter {
	private static final String TAG="FragmentPagerAdapter";
    private ArrayList<BaseFragment> mFragments;
    private ArrayList<String> mTitles;

    public FragmentPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> pageFragments, ArrayList<String> titles) {
        this(fm, pageFragments);
        this.mTitles = titles;
    }

    public FragmentPagerAdapter(FragmentManager fm,ArrayList<BaseFragment> fragments) {
        super(fm);
        this.mFragments = restore(fm, fragments);
    }

    /**
     * 如果能从FragmentManager中找到，直接取用
     * 
     * @param fm
     * @param fragments
     * @return
     */
    private ArrayList<BaseFragment> restore(FragmentManager fm,ArrayList<BaseFragment> fragments) {
        int count = fragments.size();
        ArrayList<BaseFragment> restoreFragments = new ArrayList<BaseFragment>(fragments);
        for (int i = 0; i < count; i++) {
            Fragment f = fm.findFragmentByTag(makeFragmentName(i));
            if (f != null) {
                restoreFragments.set(i,(BaseFragment) f);
            }
        }
        return restoreFragments;
    }
    
    public void onSelected(int position) {
        if (mFragments != null) {
        	mFragments.get(position).onSelected();
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int postion) {
        return mFragments.get(postion);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
