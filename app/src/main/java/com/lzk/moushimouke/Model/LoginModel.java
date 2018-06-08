package com.lzk.moushimouke.Model;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Presenter.LoginPresenter;
import com.lzk.moushimouke.View.Interface.ILoginModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by huqun on 2018/4/20.
 */

public class LoginModel implements ILoginModel{
    private List<String> phoneNumList;
    @Override
    public void requestLogin(String phoneNumber, String password) {
        MyUser.loginByAccount(phoneNumber, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (myUser!=null){
                    LoginPresenter.showLoginResult(true);
                }else {
                    LoginPresenter.showLoginResult(false);
                }
            }
        });

    }

    @Override
    public void requestCheckPhoneVerified(final String phoneNumber) {
        phoneNumList=new ArrayList<>();
        phoneNumList.clear();
        BmobQuery<MyUser> query=new BmobQuery<>();
        query.addQueryKeys("mobilePhoneNumber");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (list!=null){
                    for (MyUser user:list){
                        phoneNumList.add(user.getMobilePhoneNumber());
                    }
                    if (phoneNumList.contains(phoneNumber)){
                        LoginPresenter.showPhoneNumVerifiedResult(true);
                    }else {
                        LoginPresenter.showPhoneNumVerifiedResult(false);
                    }
                }else {
                    LoginPresenter.showPhoneNumVerifiedResult(false);
                }


            }
        });
    }
}
