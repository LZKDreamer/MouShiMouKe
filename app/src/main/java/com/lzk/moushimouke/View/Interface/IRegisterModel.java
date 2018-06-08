package com.lzk.moushimouke.View.Interface;

/**
 * Created by huqun on 2018/4/20.
 */

public interface IRegisterModel {
    public abstract void checkUserNameExits(String userName);
    public abstract void sendVerificationCode(String phoneNumber);
    public abstract void register(String userName,String phoneNumber,String code,String password);
    public abstract void checkPhoneNumVerified(String phoneNumber);
}
