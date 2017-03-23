package com.dev.nutclass;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.StorageUtil;
import com.dev.nutclass.utils.Util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;

public class ApplicationConfig {
    private static final String TAG = "ApplicationConfig";

    private NutClassApplication application;

    private Context context;
    private static ApplicationConfig instance = null;
    private String packageName;
    private String versionName;
    private int versionCode;
    private String imei;
    private double lng;
    private double lat;
    private String province;
    private String city;
    private String cacheDir;


    private ArrayList<Activity> mActivities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        if (!mActivities.contains(activity)) {
            mActivities.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    public void clearActivitys() {
        for (int i = 0; i < mActivities.size(); i++) {
            Activity activity = mActivities.get(i);
            LogUtil.i(TAG, "finish activity= " + activity.getClass().getSimpleName());
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivities.clear();
    }

    /**
     * 获取版本信息
     */
    public static int getVersionCode(Context context, Class cls) {

        try {
            return context.getPackageManager().getPackageInfo(
                    new ComponentName(context, cls).getPackageName(), 0).versionCode;
        } catch (Throwable e) {
            // MobclickAgent.reportError(context, new JiecaoException(e));
            return -1;

        }
    }

    /**
     * 获取版本信息
     */
    public static String getVersionName(Context context, Class cls) {

        try {
            return context.getPackageManager().getPackageInfo(
                    new ComponentName(context, cls).getPackageName(), 0).versionName;

        } catch (Throwable e) {
            // MobclickAgent.reportError(context, new JiecaoException(e));
            return "1.0";

        }
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double loc) {
        this.lng = loc;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }


    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPackageName() {
        return packageName;
    }

    private static Lock sLock = new ReentrantLock();

    public void initConfig(NutClassApplication application) {
        synchronized (sLock) {
            this.application = application;
            context = application.getBaseContext();
            packageName = context.getPackageName();
            versionCode = getVersionCode(context, NutClassApplication.class);
            versionName = getVersionName(context, NutClassApplication.class);
            imei = Util.getImei(getBaseContext());
            try {
                sLock.notifyAll();
            } catch (Exception e) {
            }
            ImgConfig.initImgConfig(application.getApplicationContext());
//			Util.location(application,null);
        }
    }

    public void updateConfig(NutClassApplication application) {
        initConfig(application);
    }

    public static ApplicationConfig getInstance() {
        if (instance == null) {
            try {
                sLock.lock();
                if (instance == null) {
                    instance = new ApplicationConfig();
                }
            } finally {
                sLock.unlock();
            }
        }
        return instance;
    }

    public NutClassApplication getApplication() {
        return application;
    }

    public Context getBaseContext() {
        return this.context;
    }


    public void clearMemoryCache(Context context) {
        StorageUtil.clearImgFileCache(context);
    }
}
