package com.lzk.moushimouke;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by huqun on 2018/4/21.
 */

public class MyApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext=getApplicationContext();
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
