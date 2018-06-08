package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.ResetPasswordModel;
import com.lzk.moushimouke.View.Activity.ResetPasswordActivity;

/**
 * Created by huqun on 2018/4/22.
 */

public class ResetPasswordPresenter {
    private ResetPasswordModel mResetPasswordModel;

    public ResetPasswordPresenter(){
        mResetPasswordModel=new ResetPasswordModel();
    }

    public void requestPhoneNumVerified(String phoneNumber){
        mResetPasswordModel.requestCheckPhoneNumVerified(phoneNumber);
    }

    public void commitResetPassword(String code,String password){
        mResetPasswordModel.requestResetPassword(code,password);
    }

    public void requestGetVerifyCode(String phoneNumber){
        mResetPasswordModel.requestVerificationCode(phoneNumber);
    }

    public static void checkPhoneResult(boolean result){
        ResetPasswordActivity.sPasswordActivity.showCheckPhoneNumResult(result);
    }

    public static void resetPasswordResult(boolean result){
        ResetPasswordActivity.sPasswordActivity.showResetPasswordResult(result);
    }

    public static void sendVerificationCodeResult(boolean result){
        ResetPasswordActivity.sPasswordActivity.showSendVerificationCodeResult(result);
    }
}
