package com.dev.nutclass.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;

import android.os.AsyncTask;

public class DownloadTask extends AsyncTask<String, String, String> {
	private final static String TAG="DownloadTask";
	//多线程下载的起始位置
	private long startPos=0;
	//断点下载的偏移位置
	private int relativeOffset=0;
	//需要下载的长度
	private long length;
	private String urlStr="";
	private RandomAccessFile destFile;
	private String destFilePath;
	public DownloadTask(String urlStr,String destFilePath,int start,int relativeOffset,int length){
		this.urlStr=urlStr;
		this.startPos=start;
		this.relativeOffset=relativeOffset;
		this.length=length;
		this.destFilePath=destFilePath;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(6*1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept","*/*");
			conn.setRequestProperty("Accept-Language", "zh_CN");
			conn.setRequestProperty("Charset", "utf-8");
			InputStream is=conn.getInputStream();
			long lengthAll=0;
			if(this.length>0){
				lengthAll=this.length;
			}else{
				lengthAll=conn.getContentLength();
			}
			byte[] buffer=new byte[1024];
			int length=0;
			long time=System.currentTimeMillis();
			int k=relativeOffset;
			File tmpFile=new File(destFilePath+".tmp");
			if(!tmpFile.exists()){
				tmpFile.createNewFile();
				relativeOffset=0;
			}
			long offset=startPos+relativeOffset;
			System.out.println("the offset is "+offset);
			destFile=new RandomAccessFile(tmpFile, "rw");
			is=skip(is, offset);
			destFile.seek(offset);
			while((length=is.read(buffer))!=-1&&!isCancelled()&&(k<lengthAll)){
				destFile.write(buffer, 0, length);
				k+=length;
				if(System.currentTimeMillis()-time>100){
//					System.out.println(" the loading progress "+k+"/"+lengthAll);
					publishProgress(k+"/"+lengthAll);
					time=System.currentTimeMillis();
				}
			}
			System.out.println("k==="+k);
			if(lengthAll==k){
				tmpFile.renameTo(new File(destFilePath));
				System.out.println("download complete");
			}else{
				SharedPrefUtil.getInstance().setInt("position", k);
				System.out.println("download failure");
			}
			destFile.close();
			is.close();
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		LogUtil.d(TAG, "start loading");
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		LogUtil.d(TAG, "end loading");
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		LogUtil.d(TAG, "the progress is "+values[0]);
	}

	@Override
	protected void onCancelled(String result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
		LogUtil.d(TAG, "cancel loading");
	}
	private InputStream skip(InputStream is,long offset) throws IOException{
		while(offset>0){
			long k=is.skip(offset);
			if(k<0){
				throw new RuntimeException( "File : unexpected EOF"); 
			}
			offset-=k;
		}
		return is;
	}

}
