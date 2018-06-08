package com.lzk.moushimouke.Model.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by huqun on 2018/6/2.
 */

public class RememberAccountUtils {
    public void rememberAccountPreferences(Context context,String phoneNumber, String password){
        SharedPreferences.Editor editor=context.getSharedPreferences("account",MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("phoneNumber",phoneNumber);
        editor.putString("password",password);
        editor.apply();
    }
}
