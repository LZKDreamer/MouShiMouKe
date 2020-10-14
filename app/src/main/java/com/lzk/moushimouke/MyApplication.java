package com.lzk.moushimouke;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import cn.bmob.v3.Bmob;

/**
 * Created by huqun on 2018/4/21.
 */

public class MyApplication extends Application {
    private static Context sContext;
    private static final String applicationId = "c1a39592447a430217b2cb3124065ee2";


    @Override
    public void onCreate() {
        super.onCreate();
        sContext=getApplicationContext();
        Bmob.initialize(this, applicationId);
    }

    public static Context getContext(){
        return sContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
