package com.dev.nutclass.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.FeedCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.TagEntity;
import com.dev.nutclass.image.control.ImageConst;
import com.dev.nutclass.image.control.ImageUploadManager;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.request.OkHttpClientManager.Param;
import com.dev.nutclass.utils.BitmapUtil;
import com.dev.nutclass.utils.CommonUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.DialogUtils.ItemSelectedListener;
import com.dev.nutclass.view.AblumsRectView;
import com.dev.nutclass.view.TitleBar;
import com.squareup.okhttp.Request;

import com.umeng.socialize.SocializeException;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class CircleReleaseActivity extends BaseActivity implements
		OnClickListener {
	private Context context;
	private final static String TAG = "CircleReleaseActivity";

	private final int IMAGE_PHOTO_CODE = 11; // 从相册返回图片
	private final int IMAGE_CAMERA_CODE = 12; // 从相册返回图片
	private final int IMAGE_DEAL = 22; // 从图片裁切返回图片
	private String mLocalCameraPath = "";

	
	private TitleBar titleBar;
	private AblumsRectView albumsView;
	
	private View loading=null;
	
	private EditText descEdit;
	private boolean running=false;
	

	private RelativeLayout rootLayout;

	// 图片
	private List<ImageEntity> imgList = new ArrayList<ImageEntity>();
	
	
	 
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			DialogUtils.showToast(context, "发布成功");
		}
	};
	private boolean isRunning = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_release);
		// TODO Auto-generated method stub
		context=this;
		initView();
		initIntent();
		initData();
		initListener();
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub
		finish();
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		titleBar.setTitleRight1(TitleBar.TYPE_RELEASE_TXT);
		albumsView = (AblumsRectView) findViewById(R.id.album_view);
		descEdit = (EditText) findViewById(R.id.edit_desc);
		rootLayout = (RelativeLayout) findViewById(R.id.ll_root);
		loading=(View)findViewById(R.id.view_loading);
	}

	// 第一次进入
	private void initIntent() {
		
	}

	// 第一次进入
	private void initData() {
		albumsView.updateUI(imgList);
	}

	private void initListener() {
		
		loading.setOnClickListener(this);
		descEdit.setOnClickListener(this);
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {

			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
				if(imgList!=null&&imgList.size()>0){
					execute();
				}else{
					DialogUtils.showToast(context, "请至少选择一张图片");
				}
				return true;
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
		albumsView.setShowDel(true);
		albumsView
				.setOnItemClickListener(new AblumsRectView.OnItemClickListener() {

					@Override
					public void deleteItemClicked(int pos) {
						// TODO Auto-generated method stub
						imgList.remove(pos);
						albumsView.updateUI(imgList);
					}

					@Override
					public void albumItemClicked(int pos) {
						// TODO Auto-generated method stub
//						Intent intent = new Intent();
//						intent.setClass(context, AddTagActivity.class);
//						intent.putExtra(Const.KEY_TYPE, AddTagActivity.type);
//						intent.putExtra(Const.KEY_FROM, 1);
//						intent.putExtra(Const.KEY_POSITION, pos);
//						intent.putExtra(Const.KEY_ENTITY, imgList.get(pos));
//						startActivityForResult(intent, REQ_IMG);
						// DialogUtils.showToast(context, "等确定需求");
					}

					@Override
					public void addItemClicked() {
						// TODO Auto-generated method stub
//						Intent intent = new Intent();
//						intent.setClass(context, AddImgActivity.class);
//						intent.putExtra(Const.KEY_TYPE, AddTagActivity.type);
//						startActivity(intent);
						DialogUtils.showItemsDialog(context,
								new String[] { "拍照", "从相册中选择" },
								new ItemSelectedListener() {
									@Override
									public void onItemSelected(DialogInterface dialog,
											String text, int which) {
										// TODO Auto-generated method stub
										switch (which) {
										case 0:
											mLocalCameraPath = ImageUploadManager
													.startImageCapture((Activity) context,
															IMAGE_CAMERA_CODE,
															ImageUploadManager
																	.getCameraFileName());
											break;
										case 1:
											ImageUploadManager.gotoSysPic(
													(Activity) context, IMAGE_PHOTO_CODE);
											break;
										case 2:
											break;
										default:
											break;
										}

									}
								});
					}
				});
		 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	// 发布（先上传图片，然后一起发布）
	private void execute() {
		if(running){
			DialogUtils.showToast(context, "正在发布，请稍后");
			return;
		}
		String desc = descEdit.getText().toString();
		if (TextUtils.isEmpty(desc)) {
			DialogUtils.showToast(context, "请输入内容");
			return;
		}
		if(imgList==null||imgList.size()==0){
			DialogUtils.showToast(context, "至少选择一张图片");
			return;
		}
		String url = UrlConst.GET_ARTICAL_RELEASE;
		List<File> list=new ArrayList<File>();
		for(int i=0;i<imgList.size();i++){
			String img=imgList.get(i).getImgPath();
			File file = null;
			if (TextUtils.isEmpty(img)) {
				file = null;
			} else {// 第一次进入不选择图片
				file = new File(img);
				if (!file.exists()) {
					file= null;
				}
			}
			if(file!=null){
				list.add(file);
			}
		}
		File[] fileArray=new File[imgList.size()];
		String[] fileNameArray=new String[imgList.size()];
		for(int i=0;i<imgList.size();i++){
			fileArray[i]=list.get(i);
			fileNameArray[i]="article_img"+(i+1);
		}
		
		
		
		
		Param[] params = new Param[] {
				new Param("userId", SharedPrefUtil.getInstance().getUid()),
				new Param("token", SharedPrefUtil.getInstance().getToken()),
				new Param("content", desc)
				
				};

		try {
			running=true;
			loading.setVisibility(View.VISIBLE);
			OkHttpClientManager.postAsyn(url,
					new OkHttpClientManager.ResultCallback<String>() {

						@Override
						public void onError(Request request, Exception e) {
							// TODO Auto-generated method stub
							LogUtil.d(TAG, "error e=" + e.getMessage());
							loading.setVisibility(View.GONE);
							running=false;
						}

						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							LogUtil.d(TAG, "response=" + response);
							loading.setVisibility(View.GONE);
							running=false;
							SimpleInfoParser parser = new SimpleInfoParser();
							JsonResult<String> result = (JsonResult<String>) parser
									.parse(response);
							if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
								DialogUtils.showToast(context, "发布成功");
								Intent intent = new Intent();
								intent.setAction(Const.ACTION_BROADCAST_FEED_CREATE);
								intent.setPackage(ApplicationConfig.getInstance()
										.getPackageName());
								context.sendBroadcast(intent);
								finish();
							} else {
								DialogUtils.showToast(context, "发布失败");
							}
						}
					}, fileArray, fileNameArray, params);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updateUI() {
		albumsView.updateUI(imgList);
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == IMAGE_PHOTO_CODE && resultCode == Activity.RESULT_OK) { // 相册

			Uri contentUri = data.getData();
//			if (!TextUtils.isEmpty(contentUri.getAuthority())) {
				// Cursor cursor = null;
				try {
					String filePath = CommonUtil.getPath(context, contentUri);

					LogUtil.d(TAG, "local path=" + filePath);
					if (!TextUtils.isEmpty(filePath)) {// 目前本地存的图片路径
						updateImg(filePath);
						// 生成上传文件，或者压缩
//						Intent intent = new Intent(this,
//								CropImageActivity.class);
//						intent.putExtra(ImageConst.KEY_PATH, filePath);
//						intent.putExtra(ImageConst.KEY_SCALE, 1.0f);
//						startActivityForResult(intent, IMAGE_DEAL);
						
					}
				} finally {
					// if (cursor != null) {
					// cursor.close();
					// }
				}
//			}

		} else if (requestCode == IMAGE_CAMERA_CODE
				&& resultCode == Activity.RESULT_OK) {// 相机
			updateImg(mLocalCameraPath);
//			File f = new File(mLocalCameraPath);
//			Intent intent = new Intent(this, CropImageActivity.class);
//			intent.putExtra(ImageConst.KEY_PATH, f.getAbsolutePath());
//			intent.putExtra(ImageConst.KEY_SCALE, 1.0f);
//			startActivityForResult(intent, IMAGE_DEAL);
		} else if (requestCode == IMAGE_DEAL) {// 裁剪图片完成后
			// 获取图片路径(对图片进行显示)
//			if (data == null) {
//				return;
//			}
//			final String path = data.getStringExtra(ImageConst.KEY_PATH);
//			updateImg(path);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void updateImg(String path){
		if (!TextUtils.isEmpty(path)) {// 目前本地存的图片路径
			ImageEntity entity=new ImageEntity();
			entity.setImgPath(path);
			imgList.add(entity);
			albumsView.updateUI(imgList);
		}
	}

	

	@Override
	public void loginSucc() {
		// TODO Auto-generated method stub
		
	}
}
