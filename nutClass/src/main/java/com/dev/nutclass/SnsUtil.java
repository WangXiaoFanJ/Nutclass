package com.dev.nutclass;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.dev.nutclass.entity.ShareEntity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;

public class SnsUtil {
	public static final String QQ_APP_ID = "1104840741";
	public static final String QQ_APP_KET = "CGxrDaUBSPrl6yLS";
	public static final String WEIXIN_APP_ID = "wxf4451af60a7f3501";
	public static final String WEIXIN_APP_KET = "ba438da1dabe212a10f41f25c26692b1";
	// public static final String DESCRIPTOR = "com.umeng.share";
	
	
	public static final String KJD_APP_KEY = "564d1437199f4468a96f25fd8e1593a6";
	public static final String KJD_APP_SECRET = "9496385b737d4806a4e889fad8efd854";

	private static SnsUtil instance = null;
	private static Context context;

	public static SnsUtil getInstance(Context c) {
		context = c;
		if (instance == null) {
			instance = new SnsUtil();
		}
		return instance;
	}

	/**
	 * type=1 课程详情 type=2
	 * */
	public void openShare(final Activity context,ShareEntity entity){
		if(entity==null||TextUtils.isEmpty(entity.getUrl())){
			return;
		}
		final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ
                };
        new ShareAction(context).setDisplayList( displaylist )
                .withText(entity.getDesc() )
                .withTitle(entity.getTitle())
                .withTargetUrl(entity.getUrl())
                .withMedia(new UMImage(context, entity.getImg()) )
                .setListenerList(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Toast.makeText(SnsUtil.context,platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(context,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(context,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                })
                .open();
	}

}
