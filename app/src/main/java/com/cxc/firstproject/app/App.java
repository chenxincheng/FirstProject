package com.cxc.firstproject.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.cxc.firstproject.utils.DebugUtil;
import com.example.http.HttpUtils;

import cn.bmob.sms.BmobSMS;


/**
 * APPLICATION
 */
public class App extends Application {

    private static App baseApplication;
    public static String APPID = "4d0b207b9731b474694cbfdc2bf5fbd4";
    public static App getInstance() {
        return baseApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        HttpUtils.getInstance().init(this, DebugUtil.DEBUG);
        initBmob();
    }
    private void initBmob() {
        BmobSMS.initialize(this, APPID);
    }
    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
