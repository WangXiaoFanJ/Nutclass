package com.dev.nutclass.activity;

import java.util.ArrayList;

import com.dev.nutclass.R;
import com.dev.nutclass.R.layout;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.view.BannerCardView;

import android.content.Context;
import android.os.Bundle;

public class TestActivity extends BaseActivity {
	private Context context;
	private static final String TAG="TestActivity";
	private BannerCardView view;
	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_test);
	    // TODO Auto-generated method stub
	    this.context=TestActivity.this;
	    view=(BannerCardView)findViewById(R.id.view_loop);
	    ArrayList<ImageEntity> list = new ArrayList<ImageEntity>();
		ImageEntity e1 = new ImageEntity();
		e1.setBigPath("http://netdemo-pic.stor.sinaapp.com/%E8%AF%BE%E7%A8%8B%E6%A0%BC%E5%AD%90.png");
		ImageEntity e2 = new ImageEntity();
		e2.setBigPath("http://netdemo-pic.stor.sinaapp.com/%E8%AF%BE%E7%A8%8B%E6%A0%BC%E5%AD%90%E2%80%94%E2%80%94%E6%89%8B%E5%8A%A8%E6%B7%BB%E5%8A%A0%E8%AF%BE%E7%A8%8B.png");
		ImageEntity e3 = new ImageEntity();
		e3.setBigPath("http://netdemo-pic.stor.sinaapp.com/%E8%AF%BE%E7%A8%8B%E6%A0%BC%E5%AD%90.png");
		ImageEntity e4 = new ImageEntity();
		e4.setBigPath("http://netdemo-pic.stor.sinaapp.com/%E8%AF%BE%E7%A8%8B%E6%A0%BC%E5%AD%90%E2%80%94%E2%80%94%E6%89%8B%E5%8A%A8%E6%B7%BB%E5%8A%A0%E8%AF%BE%E7%A8%8B.png");
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
//		view.updateView(list);
	}

}
