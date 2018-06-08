package com.lzk.moushimouke.View.Interface;

/**
 * Created by huqun on 2018/4/20.
 */

public interface IRegister {
    public abstract void showUserNameResult(boolean result);
    public abstract void showSendVerificationResult(boolean result);
    public abstract void showRegisterResult(boolean result);
    public abstract void checkPhoneNumVerified(boolean result);
}
