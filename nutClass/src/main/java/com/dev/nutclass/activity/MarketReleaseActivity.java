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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.SingleItemCardEntity;
import com.dev.nutclass.image.control.ImageConst;
import com.dev.nutclass.image.control.ImageUploadManager;
import com.dev.nutclass.image.crop.CropImageActivity;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.request.OkHttpClientManager.Param;
import com.dev.nutclass.utils.BitmapUtil;
import com.dev.nutclass.utils.CommonUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.utils.DialogUtils.ItemSelectedListener;
import com.squareup.okhttp.Request;

public class MarketReleaseActivity extends BaseActivity implements
		OnClickListener {
	private Context context;
	private final static String TAG = "MarketReleaseActivity";

	private final int IMAGE_PHOTO_CODE = 11; // 从相册返回图片
	private final int IMAGE_CAMERA_CODE = 12; // 从相册返回图片
	private final int IMAGE_DEAL = 22; // 从图片裁切返回图片
	private String mLocalCameraPath = "";

	private EditText titleEdit;
	private EditText priceEdit;
	private EditText descEdit;
	private TextView categoryTv;
	private TextView addressTv;
	private TextView useTv;
	private TextView ageTv;
	private ImageView img1Tv;
	private ImageView img2Tv;
	private ImageView img3Tv;

	private TextView releaseTv;

	private String category = "";
	private String address = "";
	private String use = "";
	private String age = "";
	private String img1 = "";
	private String img2 = "";
	private String img3 = "";
	private int position = -1;

	private RecyclerView filterListView;
	private RecyclerView filter2ListView;
	private LinearLayout filterListLayout;

	private CardListAdapter filterAdapter;
	private CardListAdapter filter2Adapter;

	private RecyclerItemClickListener itemClickListener1;
	private RecyclerItemClickListener itemClickListener2;

	private List<SingleItemCardEntity> categoryList = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = MarketReleaseActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_market_release);
		initView();
		initIntent();
		initData();
		initListener();
	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

	private void initView() {

		titleEdit = (EditText) findViewById(R.id.edit_title);
		priceEdit = (EditText) findViewById(R.id.edit_price);
		descEdit = (EditText) findViewById(R.id.edit_desc);
		categoryTv = (TextView) findViewById(R.id.tv_category);
		addressTv = (TextView) findViewById(R.id.tv_address);
		useTv = (TextView) findViewById(R.id.tv_use);
		ageTv = (TextView) findViewById(R.id.tv_age);
		img1Tv = (ImageView) findViewById(R.id.iv_img1);
		img2Tv = (ImageView) findViewById(R.id.iv_img2);
		img3Tv = (ImageView) findViewById(R.id.iv_img3);
		releaseTv = (TextView) findViewById(R.id.tv_release);

		filterListView = (RecyclerView) findViewById(R.id.filter_list);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		filterListView.setLayoutManager(mLayoutManager);

		LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(context);
		mLayoutManager2.setOrientation(LinearLayout.VERTICAL);
		filter2ListView = (RecyclerView) findViewById(R.id.filter_list2);
		filter2ListView.setLayoutManager(mLayoutManager2);

		filterListLayout = (LinearLayout) findViewById(R.id.ll_filter_list);
	}

	private void initIntent() {

	}

	private void initData() {

	}

	private void initListener() {
		categoryTv.setOnClickListener(this);
		addressTv.setOnClickListener(this);
		useTv.setOnClickListener(this);
		ageTv.setOnClickListener(this);
		img1Tv.setOnClickListener(this);
		img2Tv.setOnClickListener(this);
		img3Tv.setOnClickListener(this);
		releaseTv.setOnClickListener(this);

		itemClickListener1 = new RecyclerItemClickListener() {

			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
				DialogUtils.showToast(context, entity.getDesc());
				// 此处点击重新请求
				updateSecondFilter(entity.getId());
			}

			
		};
		itemClickListener2 = new RecyclerItemClickListener() {

			@Override
			public void onItemClick(View view, Object obj) {
				// TODO Auto-generated method stub
				SingleItemCardEntity entity = (SingleItemCardEntity) obj;
//				DialogUtils.showToast(context, "重新请求" + entity.getDesc());
				// 此处点击重新请求
				address=entity.getId();
				addressTv.setText(entity.getDesc());
				updateFilter(false);

			}

			
		};
	}

	private void updateFilter(boolean show) {
		if (!show) {
			filterListLayout.setVisibility(View.GONE);
		} else {
			filterListLayout.setVisibility(View.VISIBLE);
			filter2ListView.setVisibility(View.VISIBLE);
			filterAdapter = new CardListAdapter(context, Util.regionList,
					itemClickListener1);
			filterListView.setAdapter(filterAdapter);

			filter2Adapter = new CardListAdapter(
					context,
					Util.regionSecondList
							.get(((SingleItemCardEntity) Util.regionList.get(0))
									.getId()), itemClickListener2);
			filter2ListView.setAdapter(filter2Adapter);
		}
	}

	private void updateSecondFilter(String pid) {
		filter2Adapter = new CardListAdapter(context,
				Util.regionSecondList.get(pid), itemClickListener2);
		filter2ListView.setAdapter(filter2Adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == categoryTv) {
			// DialogUtils.showToast(context, "弹出分享框");
			final String[] strArray = new String[Util.getCategoryList().size()];
			for (int i = 0; i < Util.getCategoryList().size(); i++) {
				strArray[i] = ((SingleItemCardEntity) Util.getCategoryList()
						.get(i)).getDesc();
			}
			// 分类数据解析
			DialogUtils.showItemsDialog(context, strArray,
					new ItemSelectedListener() {

						@Override
						public void onItemSelected(DialogInterface dialog,
								String text, int which) {
							// TODO Auto-generated method stub
							categoryTv.setText(strArray[which]);
							category = ((SingleItemCardEntity) Util
									.getCategoryList().get(which)).getId();
						}
					});
		} else if (v == addressTv) {
			updateFilter(true);
		} else if (v == useTv) {
			// 分类数据解析
			DialogUtils.showItemsDialog(context,
					getResources().getStringArray(R.array.use_filter),
					new ItemSelectedListener() {

						@Override
						public void onItemSelected(DialogInterface dialog,
								String text, int which) {
							// TODO Auto-generated method stub
							use = which + 1 + "";
							useTv.setText(text);
							LogUtil.d(TAG, "use=" + use);
						}
					});
		} else if (v == ageTv) {
			// 分类数据解析
			DialogUtils.showItemsDialog(context,
					getResources().getStringArray(R.array.age_filter),
					new ItemSelectedListener() {

						@Override
						public void onItemSelected(DialogInterface dialog,
								String text, int which) {
							// TODO Auto-generated method stub
							age = which + 1 + "";
							ageTv.setText(text);
							LogUtil.d(TAG, "age=" + age);
						}
					});
		} else if (v == img1Tv || v == img2Tv || v == img3Tv) {
			//DialogUtils.showToast(context, "图片选择器");
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
			if (v == img1Tv) {
				position = 1;
			} else if (v == img2Tv) {
				position = 2;
			} else if (v == img3Tv) {
				position = 3;
			}
		} else if (v == releaseTv) {
			release();
		}
	}

	private void release() {
		String title = titleEdit.getText().toString();
		if (TextUtils.isEmpty(title)) {
			DialogUtils.showToast(context, "请输入标题");
			return;
		}
		if (TextUtils.isEmpty(category)) {
			DialogUtils.showToast(context, "请选择分类");
			return;
		}
		if (TextUtils.isEmpty(address)) {
			DialogUtils.showToast(context, "请选择商圈");
			return;
		}
		String price = priceEdit.getText().toString();
		if (TextUtils.isEmpty(price)) {
			DialogUtils.showToast(context, "请输入价格");
			return;
		}
		if (TextUtils.isEmpty(use)) {
			DialogUtils.showToast(context, "请选择成色");
			return;
		}
		if (TextUtils.isEmpty(age)) {
			DialogUtils.showToast(context, "请选择使用年龄");
			return;
		}
		String desc = descEdit.getText().toString();
		if (TextUtils.isEmpty(desc)) {
			DialogUtils.showToast(context, "请输入描述内容");
			return;
		}
		if (TextUtils.isEmpty(img1) && TextUtils.isEmpty(img2)
				&& TextUtils.isEmpty(img3)) {
			DialogUtils.showToast(context, "请至少输入一张图片");
			return;
		}
		String url = UrlConst.MARKET_RELEASE_URL;
		List<File> list=new ArrayList<File>();
		for(int i=0;i<3;i++){
			String img="";
			if(i==0){
				img=img1;
			}else if(i==1){
				img=img2;
			}else if(i==2){
				img=img3;
			}
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
		File[] fileArray=new File[list.size()];
		String[] fileNameArray=new String[list.size()];
		for(int i=0;i<list.size();i++){
			fileArray[i]=list.get(i);
			fileNameArray[i]="image"+(i+1);
		}
		
		
		
		/**
		 * Image1 至 Image6 上行参数： 图片key Image1 至 Image6 单文件最大 2m Title str 标题
		 * Type int 类型（ 类型 path get_maket_cat_list） district_id int 商圈ID （path
		 * get_district_list ） Price int 产品价格 Old_lv int 成色
		 * 1（10成新）2（9成新）3（8成新）1（8成新以下） Age_lv int 适用年龄 1（0-6） 2（6-18）3（18以上）
		 * Description str 描述 userId int 用户ID
		 * */
		Param[] params = new Param[] {
				new Param("userId", SharedPrefUtil.getInstance().getUid()),
				new Param("token", SharedPrefUtil.getInstance().getToken()),
				new Param("title", title),
				new Param("type", category),
				new Param("district_id",address),
				new Param("price", price),
				new Param("old_lv", use),
				new Param("age_lv",age),
				new Param("description", desc),
				};

		try {
			OkHttpClientManager.postAsyn(url,
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
							SimpleInfoParser parser = new SimpleInfoParser();
							JsonResult<String> result = (JsonResult<String>) parser
									.parse(response);
							if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
								DialogUtils.showToast(context, "发布成功");
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		LogUtil.i(TAG, "requestCode=" + requestCode + ",resultCode= "
				+ resultCode);
		if (requestCode == IMAGE_PHOTO_CODE && resultCode == Activity.RESULT_OK) { // 相册

			Uri contentUri = data.getData();
			if (!TextUtils.isEmpty(contentUri.getAuthority())) {
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
			}

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
			if (data == null) {
				return;
			}
			final String path = data.getStringExtra(ImageConst.KEY_PATH);
			updateImg(path);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void updateImg(String path){
		if (!TextUtils.isEmpty(path)) {// 目前本地存的图片路径
			switch (position) {
			case 1:
				img1 = path;
				img1Tv.setImageBitmap(BitmapFactory.decodeFile(path));
				img1Tv.setImageBitmap(BitmapUtil.getSquareBitmap(context, path, getResources().getDisplayMetrics().widthPixels/3));
				break;
			case 2:
				img2 = path;
				img2Tv.setImageBitmap(BitmapFactory.decodeFile(path));
				img2Tv.setImageBitmap(BitmapUtil.getSquareBitmap(context, path, getResources().getDisplayMetrics().widthPixels/3));
				break;
			case 3:
				img3 = path;
				img3Tv.setImageBitmap(BitmapFactory.decodeFile(path));
				img3Tv.setImageBitmap(BitmapUtil.getSquareBitmap(context, path, getResources().getDisplayMetrics().widthPixels/3));
				break;

			default:
				break;
			}
		}
	}
}
