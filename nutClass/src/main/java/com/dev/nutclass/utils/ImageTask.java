package com.dev.nutclass.utils;



import android.graphics.Bitmap;
import android.os.AsyncTask;

public class ImageTask extends AsyncTask<String, Void, Bitmap> {
	private OnImageSuccessListener listener;
	
	
	public ImageTask(OnImageSuccessListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		String url = params[0];
		Bitmap bitmap = MyHttpUtils.getBitmapFromUrl(url);
		return bitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		listener.onImageSuccess(result);
	}

}
