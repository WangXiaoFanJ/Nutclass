package com.dev.nutclass.activity;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CourseIntroduceActivity extends BaseActivity {

	private LinearLayout imgLayout;
	private TextView descTv;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.activity_course_introduce);
	    imgLayout=(LinearLayout)this.findViewById(R.id.ll_desc_container);
		descTv=(TextView)this.findViewById(R.id.tv_desc);
		if(getIntent().hasExtra(Const.KEY_ENTITY)){
			CourseCardEntity entity=(CourseCardEntity) getIntent().getSerializableExtra(Const.KEY_ENTITY);
			if (entity == null) {
				finish();
			}
			if(entity.getDescImgList()!=null&&entity.getDescImgList().size()>0){
				for(int i=0;i<entity.getDescImgList().size();i++){
					ImageView img=new ImageView(CourseIntroduceActivity.this);
					img.setScaleType(ImageView.ScaleType.FIT_XY);
					LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.height_banner));
					params.bottomMargin=10;
					ImageLoader.getInstance().displayImage(entity.getDescImgList().get(i), img);
					imgLayout.addView(img,params);
				}
			}
			if(TextUtil.isNotNull(entity.getDesc())){
				descTv.setText(entity.getDesc());
				descTv.setVisibility(View.VISIBLE);
			}else{
				descTv.setVisibility(View.GONE);
			}
		}else{
			finish();
		}
		
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub
		
	}

}
