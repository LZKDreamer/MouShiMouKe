package com.lzk.moushimouke.Model.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by huqun on 2018/5/4.
 */

public class IsNetWorkConnectedUtils {

    public  boolean IsNetWorkConnected(Context context){
        if (context!=null){
            ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if (networkInfo!=null){
                return networkInfo.isAvailable();
            }else {
                return false;
            }
        }
        return false;
    }

    public boolean isWifiConnected(Context context){
        if (context!=null){
            ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if (networkInfo!=null&&networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}
