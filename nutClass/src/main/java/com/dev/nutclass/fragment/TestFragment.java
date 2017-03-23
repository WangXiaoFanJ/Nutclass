package com.dev.nutclass.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.request.TaskResult;

public class TestFragment extends BaseFragment {

	private TextView testTv;
	private String testStr;
//	public TestFragment(String str) {
//		// TODO Auto-generated constructor stub
//		this.testStr=str;
//	}
public static TestFragment newInstance(String str) {
	TestFragment newFragment = new TestFragment();
	Bundle bundle = new Bundle();
	bundle.putString("str", str);

	newFragment.setArguments(bundle);
	return newFragment;

}
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_test, null);
		testStr = getArguments().getString("str");
		testTv=(TextView)view.findViewById(R.id.tv_test);
		testTv.setText(testStr);
		return view;
	}

	

}
