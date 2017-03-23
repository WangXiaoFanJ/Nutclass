package com.dev.nutclass.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.net.HttpURLConnection;

import com.dev.nutclass.R;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;

public class UploadActivity extends Activity implements OnClickListener {

	private String wordUrlStr = "http://1.netdemo.sinaapp.com/get_test.php?type=2";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.activity_upload);
		findViewById(R.id.btn_upload_word).setOnClickListener(this);
		findViewById(R.id.btn_upload_pic).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btn_upload_word:
			Map<String, String> baseParams = new HashMap<String, String>();
			baseParams.put("type", "2");
			Map<String, String> postParams = new HashMap<String, String>();
			postParams.put("username", "LJName");
			postParams.put("userage", "LJAge");
			sendByPost(wordUrlStr, postParams, baseParams);
			break;
		case R.id.btn_upload_pic:
			new Thread(){
				public void run() {
					Map<String, String> dd = new HashMap<String, String>();
					String path=Environment.getExternalStorageDirectory()+File.separator+"test.jpg";
					dd.put("uploadImage",path);
					String urldd="http://1.fanscircle.sinaapp.com/interface/upload_pic.php?";
					uploadFile(dd, urldd);
				};
			}.start();
			
			break;
		default:
			break;
		}
	}

	private void sendByGet(final String url, final Map<String, String> params) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					StringBuilder urlSb = new StringBuilder();
					urlSb.append(url).append("?");
					for (String key : params.keySet()) {
						urlSb.append(key)
								.append("=")
								.append(URLEncoder.encode(params.get(key),
										"utf-8"));
						urlSb.append("&");
					}
					urlSb.deleteCharAt(urlSb.length() - 1);
					HttpURLConnection conn;
					System.out.println(urlSb.toString());
					conn = (HttpURLConnection) new URL(urlSb.toString())
							.openConnection();
					conn.setConnectTimeout(6 * 1000);
					conn.setRequestMethod("GET");
					if (conn.getResponseCode() == 200) {
						System.out.println("get upload success");
						System.out.println(HttpUtil.inputStream2String(conn
								.getInputStream()));
					} else {
						System.out.println("get upload failure");
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}.start();

	}

	private void sendByPost(final String url,
			final Map<String, String> postparams,
			final Map<String, String> baseparams) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					StringBuilder urlSb = new StringBuilder();
					urlSb.append(url).append("?");
					if (baseparams != null && baseparams.size() > 0) {
						for (String key : baseparams.keySet()) {
							urlSb.append(key)
									.append("=")
									.append(URLEncoder.encode(
											baseparams.get(key), "utf-8"));
							urlSb.append("&");
						}
						urlSb.deleteCharAt(urlSb.length() - 1);
					}
					StringBuilder paramsSb = new StringBuilder();
					if (postparams != null && postparams.size() > 0) {
						for (String key : postparams.keySet()) {
							paramsSb.append(key)
									.append("=")
									.append(URLEncoder.encode(
											postparams.get(key), "utf-8"));
							paramsSb.append("&");
						}
						paramsSb.deleteCharAt(paramsSb.length() - 1);
					}
					byte[] postData = paramsSb.toString().getBytes();
					HttpURLConnection conn;
					System.out.println(urlSb.toString());
					conn = (HttpURLConnection) new URL(urlSb.toString())
							.openConnection();
					conn.setConnectTimeout(6 * 1000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setRequestProperty("Content-Type", "*/*");
					conn.setRequestProperty("Content-Length", postData.length
							+ "");
					OutputStream os = conn.getOutputStream();
					os.write(postData);
					os.flush();
					os.close();
					if (conn.getResponseCode() == 200) {
						System.out.println("get upload success");
						System.out.println(HttpUtil.inputStream2String(conn
								.getInputStream()));
					} else {
						System.out.println("get upload failure");
					}
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}.start();
	}
 

	private static final int TIME_OUT = 10 * 10000000; // 超时时间
	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";

	/**
	 * * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的url
	 * @return 返回响应的内容
	 */
	public static String uploadFile(final Map<String, String> params, String RequestURL) {
		
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", "utf-8");
			// 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			
			if(params!=null&&params.size()>0){
				OutputStream os=conn.getOutputStream();
				for (String key : params.keySet()) {
					if(key.equals("uploadImage")){//添加文件
						StringBuffer sb = new StringBuffer();
						//添加RFC1867协议的头
						sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
						sb.append("Content-Disposition:form-date;name=\""+key+"\";filename=\""+params.get(key)+"\""+LINE_END);
						sb.append(LINE_END);
						os.write(sb.toString().getBytes());
						InputStream is=new FileInputStream(params.get(key));
						byte[] bytes = new byte[1024];  
		                int len = 0;  
		                while ((len = is.read(bytes)) != -1) {  
		                    os.write(bytes, 0, len);  
		                }  
		                is.close();  
		                os.write(LINE_END.getBytes());  
		                os.flush();  
					}else{
						StringBuffer sb = new StringBuffer();
						//添加RFC1867协议的头
						sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
						sb.append("Content-Disposition:form-date;name=\""+key+"\""+LINE_END);
						sb.append(LINE_END);
						sb.append(params.get(key)).append(LINE_END);
						os.write(sb.toString().getBytes());
						os.flush();
					}
					
				}
				String end=PREFIX+BOUNDARY+PREFIX+LINE_END;
				os.write(end.getBytes());
				os.flush();
				os.close();
			}
			if (conn.getResponseCode() == 200) {
				System.out.println("get upload success");
				System.out.println(HttpUtil.inputStream2String(conn
						.getInputStream()));
			} else {
				System.out.println("get upload failure");
			}
		 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FAILURE;
	}

}
