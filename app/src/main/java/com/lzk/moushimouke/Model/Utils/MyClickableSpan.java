package com.lzk.moushimouke.Model.Utils;

import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.View.Activity.HomePageActivity;

/**
 * Created by huqun on 2018/6/5.
 */

public class MyClickableSpan extends ClickableSpan {

    private MyUser mMyUser;

    public MyClickableSpan(MyUser user){
        mMyUser=user;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View view) {
        Intent intent= HomePageActivity.newIntent(MyApplication.getContext(),mMyUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }
}
