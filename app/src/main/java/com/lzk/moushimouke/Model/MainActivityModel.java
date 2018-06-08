package com.lzk.moushimouke.Model;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.View.Interface.IMainActivityPresenterCallBack;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by huqun on 2018/6/2.
 */

public class MainActivityModel {
    private IMainActivityPresenterCallBack mActivityPresenter;

    public void login(String phoneNumber, String password, IMainActivityPresenterCallBack callBack){
        mActivityPresenter=callBack;
        BmobUser.loginByAccount(phoneNumber, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (user!=null){
                    mActivityPresenter.getLoginResult(true);
                }else {
                    mActivityPresenter.getLoginResult(false);
                }
            }
        });

    }
}
