package com.lzk.moushimouke.Presenter;

import android.util.Log;

import com.lzk.moushimouke.Model.RegisterModel;
import com.lzk.moushimouke.View.Activity.RegisterActivity;
import com.lzk.moushimouke.View.Interface.IRegister;

/**
 * Created by huqun on 2018/4/20.
 */

public class RegisterPresenter {
    private RegisterModel mRegisterModel;

    public RegisterPresenter(){
        mRegisterModel=new RegisterModel();
    }


    public void requestVerificationCode(String phoneNumber){

        mRegisterModel.sendVerificationCode(phoneNumber);
    }

    public void checkUserNameExist(String userName){
        mRegisterModel.checkUserNameExits(userName);
    }

    public static void userNameExistResult(boolean result){
        RegisterActivity.sRegisterActivity.showUserNameResult(result);
    }

    public void requestRegister(String userName,String phoneNumber,String code,String password){
        mRegisterModel.register(userName,phoneNumber,code,password);
    }

    public static void registerResult(boolean result){
        RegisterActivity.sRegisterActivity.showRegisterResult(result);
    }

    public static void sendVerificationCodeResult(boolean result){
        RegisterActivity.sRegisterActivity.showSendVerificationResult(result);
    }

    public void checkPhoneNumResult(String phoneNumber){
        mRegisterModel.checkPhoneNumVerified(phoneNumber);
    }

    public static void showPhoneNumResul(boolean result){
        RegisterActivity.sRegisterActivity.checkPhoneNumVerified(result);
    }
}