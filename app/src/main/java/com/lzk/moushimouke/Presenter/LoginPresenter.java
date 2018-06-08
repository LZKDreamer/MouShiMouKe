package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.LoginModel;
import com.lzk.moushimouke.View.Activity.LoginActivity;
import com.lzk.moushimouke.View.Interface.ILogin;
import com.lzk.moushimouke.View.Interface.ILoginModel;

/**
 * Created by huqun on 2018/4/22.
 */

public class LoginPresenter {
    private LoginModel mILoginModel;

    public LoginPresenter(){
        mILoginModel=new LoginModel();
    }

    public void requestCheckPhoneIsVerified(String phoneNumber){
        mILoginModel.requestCheckPhoneVerified(phoneNumber);
    }

    public void requestLogin(String phoneNumber,String password){
        mILoginModel.requestLogin(phoneNumber,password);
    }

    public static void showPhoneNumVerifiedResult(boolean result){
        LoginActivity.sLoginActivity.getLoginPhoneNumIsVerified(result);
    }

    public static void showLoginResult(boolean result){
        LoginActivity.sLoginActivity.getLoginResult(result);
    }
}
