package com.lzk.moushimouke.View.Interface;

/**
 * Created by huqun on 2018/4/20.
 */

public interface ILoginModel {
    public abstract void requestLogin(String phoneNumber,String password);
    public abstract void requestCheckPhoneVerified(String phoneNumber);
}
