package com.dev.nutclass.activity;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.SimpleEntity;
import com.dev.nutclass.entity.SingnalEvent;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.image.control.ImageConst;
import com.dev.nutclass.image.control.ImageUploadManager;
import com.dev.nutclass.image.crop.CropImageActivity;
import com.dev.nutclass.parser.EditUserInfoParser;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.request.OkHttpClientManager.Param;
import com.dev.nutclass.utils.CommonUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.DialogUtils.ItemSelectedListener;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.view.TitleBar;
import com.mrwujay.cascade.activity.SelectProvinceActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import de.greenrobot.event.EventBus;

public class EditUserInfoActivity extends BaseActivity implements
		OnClickListener {
	private Context context;
	private final static String TAG = "EditUserInfoActivity";

	private final int IMAGE_PHOTO_CODE = 11; // 从相册返回图片
	private final int IMAGE_CAMERA_CODE = 12; // 从相册返回图片
	private String mLocalCameraPath = "";
	private final int IMAGE_DEAL = 22; // 从图片裁切返回图片
	private final int REQ_ADDRESS = 23; // 从图片裁切返回图片

	private TitleBar titleBar;
	private LinearLayout profileLayout;
	private LinearLayout nameLayout;
	private LinearLayout sexLayout;
	private LinearLayout birthdayLayout;
	private LinearLayout addressLayout;
	private LinearLayout babyLayout;
	private ImageView profileIv;
	private TextView nameTv;
	private TextView sexTv;
	private TextView birthdayTv;
	private TextView addressTv;
	private TextView babyTv;

	private UserEntity userEntity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = EditUserInfoActivity.this;
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_edit_userinfo);
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
		titleBar = (TitleBar) findViewById(R.id.tb_title);

		profileLayout = (LinearLayout) findViewById(R.id.ll_modify_profile);
		nameLayout = (LinearLayout) findViewById(R.id.ll_modify_name);
		sexLayout = (LinearLayout) findViewById(R.id.ll_modify_sex);
		birthdayLayout = (LinearLayout) findViewById(R.id.ll_modify_birthday);
		addressLayout = (LinearLayout) findViewById(R.id.ll_modify_address);
		babyLayout = (LinearLayout) findViewById(R.id.ll_modify_baby);
		profileIv = (ImageView) findViewById(R.id.iv_profile);
		nameTv = (TextView) findViewById(R.id.tv_user_name);
		sexTv = (TextView) findViewById(R.id.tv_sex);
		birthdayTv = (TextView) findViewById(R.id.tv_birthday);
		addressTv = (TextView) findViewById(R.id.tv_address);
		babyTv = (TextView) findViewById(R.id.tv_baby);
	}

	private void initIntent() {

	}

	private void initData() {
		reqData();
	}

	private void initListener() {
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {

			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
				reqModifyData();
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
		profileLayout.setOnClickListener(this);
		nameLayout.setOnClickListener(this);
		sexLayout.setOnClickListener(this);
		birthdayLayout.setOnClickListener(this);
		addressLayout.setOnClickListener(this);
		babyLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == profileLayout) {
			DialogUtils.showToast(context, "图片选择器");
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
			// finish();
		} else if (v == nameLayout) {
			final EditText et = new EditText(this);
			et.setText(userEntity.getName());
			// 获取ip而已，不用在乎
			new AlertDialog.Builder(this)
					.setTitle("请输入用户名")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(et)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// 数据获取
									userEntity.setName(et.getText().toString());
									nameTv.setText(et.getText().toString());
								}
							}).setNegativeButton("取消", null).show();

		} else if (v == sexLayout) {
			// DialogUtils.showToast(context, "弹出分享框");
			DialogUtils.showItemsDialog(context, new String[] { "男", "女" },
					new ItemSelectedListener() {

						@Override
						public void onItemSelected(DialogInterface dialog,
								String text, int which) {
							// TODO Auto-generated method stub
							if (which == 0) {
								userEntity.setSex(1);
								update();
							} else {
								userEntity.setSex(2);
								update();
							}
						}
					});
		} else if (v == birthdayLayout) {
			Calendar d = Calendar.getInstance(Locale.CHINA);
			// 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
			Date myDate = new Date();
			// 创建一个Date实例
			d.setTime(myDate);
			// 设置日历的时间，把一个新建Date实例myDate传入
			int year = d.get(Calendar.YEAR);
			int month = d.get(Calendar.MONTH);
			int day = d.get(Calendar.DAY_OF_MONTH);
			// 获得日历中的 year month day
			DatePickerDialog dlg = new DatePickerDialog(context,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							String date = year + "-" + (monthOfYear + 1) + "-"
									+ (dayOfMonth + 1);
							userEntity.setBirthday(date);
							birthdayTv.setText(date);
						}
					}, year, month, day);

			// 新建一个DatePickerDialog 构造方法中
			// （设备上下文，OnDateSetListener时间设置监听器，默认年，默认月，默认日）
			dlg.show();
		} else if (v == addressLayout) {
			Intent intent = new Intent();
			intent.setClass(context, SelectProvinceActivity.class);
			startActivityForResult(intent, REQ_ADDRESS);
		} else if (v == babyLayout) {
			// DialogUtils.showToast(context, "弹出分享框");
			DialogUtils.showItemsDialog(context, new String[] { "无宝宝", "备孕",
					"已孕", "已出生" }, new ItemSelectedListener() {

				@Override
				public void onItemSelected(DialogInterface dialog, String text,
						int which) {
					// TODO Auto-generated method stub
					userEntity.setBabayStatus(which);
					update();
				}
			});
		}
	}

	/*
	 * 网络请求数据
	 */
	private void reqData() {
		String url = HttpUtil.addBaseGetParams(UrlConst.USERINFO_URL);
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
						EditUserInfoParser parser = new EditUserInfoParser();
						JsonResult<UserEntity> retObj = (JsonResult<UserEntity>) parser
								.parse(response);
						if (retObj.getErrorCode() == UrlConst.SUCCESS_CODE) {
							userEntity = retObj.getRetObj();
							update();
						} else {
							DialogUtils.showToast(context, retObj.getErrorMsg());
						}

					}
				});

	}

	/*
	 * 网络请求数据
	 */
	private void reqModifyData() {
		String url = UrlConst.USERINFO_EDIT_URL;
		File file = null;
		if (TextUtils.isEmpty(userEntity.getProfileLocalPath())) {
			file = null;
		} else {// 第一次进入不选择图片
			file = new File(userEntity.getProfileLocalPath());
			if (!file.exists()) {
				file = null;
			}
		}
		/**
		 * image1 图片KEY nick_name 昵称 sex 性别 1 男 2女 Birthday 生日 1900-01-01
		 * District 地址 北京 Babystatus 宝宝状态 0无宝宝 1备孕 2已孕 已出生
		 * */
		Param[] params = new Param[] {
				new Param("userId", SharedPrefUtil.getInstance().getUid()),
				new Param("token", SharedPrefUtil.getInstance().getToken()),
				new Param("nick_name", userEntity.getName()),
				new Param("sex", String.valueOf(userEntity.getSex())),
				new Param("birthday", userEntity.getBirthday()),
				new Param("district", userEntity.getAddress()),
				new Param("babystatus", String.valueOf(userEntity
						.getBabayStatus())) };

		try {
			if(file==null){
				OkHttpClientManager.postAsyn(url,
						new OkHttpClientManager.ResultCallback<String>() {

							@Override
							public void onError(Request request, Exception e) {
								// TODO Auto-generated method stub
								LogUtil.d(TAG, "error e=" + e.getMessage());
								DialogUtils.showToast(context, getString(R.string.tip_net_exeception));
							}

							@Override
							public void onResponse(String response) {
								// TODO Auto-generated method stub
								LogUtil.d(TAG, "response=" + response);
								SimpleInfoParser parser = new SimpleInfoParser();
								JsonResult<String> result = (JsonResult<String>) parser
										.parse(response);
								if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
									DialogUtils.showToast(context, "修改成功");
									//修改本地缓存，并且调用通知更新
									EventBus.getDefault().post(new SingnalEvent(SingnalEvent.SINGNAL_UPDATE_USERINFO));
									finish();
								} else {
									DialogUtils.showToast(context, "修改失败");
								}
							}
						}, params);
			}else{
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
									DialogUtils.showToast(context, "修改成功");
									finish();
								} else {
									DialogUtils.showToast(context, "修改失败");
								}
							}
						}, file, "image1", params);
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update() {
		if (userEntity == null) {
			finish();
		}
		ImageLoader.getInstance().displayImage(userEntity.getPortrait(),
				profileIv);
		nameTv.setText(userEntity.getName());
		if (userEntity.getSex() == 1) {
			sexTv.setText("男");
		} else if (userEntity.getSex() == 2) {
			sexTv.setText("女");
		} else {
			sexTv.setText("无");
		}
		birthdayTv.setText(userEntity.getBirthday());
		addressTv.setText(userEntity.getAddress());
		switch (userEntity.getBabayStatus()) {
		case 0:
			babyTv.setText("无宝宝");
			break;
		case 1:
			babyTv.setText("备孕");
			break;
		case 2:
			babyTv.setText("已孕");
			break;
		case 3:
			babyTv.setText("已出生");
			break;

		default:
			break;
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
						// 生成上传文件，或者压缩
						Intent intent = new Intent(this,
								CropImageActivity.class);
						intent.putExtra(ImageConst.KEY_PATH, filePath);
						intent.putExtra(ImageConst.KEY_SCALE, 1.0f);
						startActivityForResult(intent, IMAGE_DEAL);
					}
				} finally {
					// if (cursor != null) {
					// cursor.close();
					// }
				}
			}

		} else if (requestCode == IMAGE_CAMERA_CODE
				&& resultCode == Activity.RESULT_OK) {// 相机
			File f = new File(mLocalCameraPath);
			Intent intent = new Intent(this, CropImageActivity.class);
			intent.putExtra(ImageConst.KEY_PATH, f.getAbsolutePath());
			intent.putExtra(ImageConst.KEY_SCALE, 1.0f);
			startActivityForResult(intent, IMAGE_DEAL);
		} else if (requestCode == IMAGE_DEAL) {// 裁剪图片完成后
			// 获取图片路径(对图片进行显示)
			if (data == null) {
				return;
			}
			final String path = data.getStringExtra(ImageConst.KEY_PATH);
			if (!TextUtils.isEmpty(path)) {// 目前本地存的图片路径
				userEntity.setProfileLocalPath(path);
				profileIv.setImageBitmap(BitmapFactory.decodeFile(path, null));
			}
		} else if (requestCode == REQ_ADDRESS) {
			String address = data.getStringExtra(Const.KEY_CONTENT);
			userEntity.setAddress(address);
			addressTv.setText(address);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
