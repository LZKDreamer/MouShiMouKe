package com.lzk.moushimouke.View.Interface;

/**
 * Created by huqun on 2018/4/20.
 */

public interface IResetPasswordModel {
    public abstract void requestVerificationCode(String phoneNumber);
    public abstract void requestResetPassword(String code,String password);
    public abstract void requestCheckPhoneNumVerified(String phoneNumber);
}
