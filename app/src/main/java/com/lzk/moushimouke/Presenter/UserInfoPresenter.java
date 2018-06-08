package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.UserInfoModel;
import com.lzk.moushimouke.View.Interface.IUserInfoActivityCallBack;
import com.lzk.moushimouke.View.Interface.IUserInfoPresenterCallBack;

import java.util.Calendar;

/**
 * Created by huqun on 2018/6/2.
 */

public class UserInfoPresenter implements IUserInfoPresenterCallBack {
    private IUserInfoActivityCallBack mActivityCallBack;
    private UserInfoModel mModel;

    public void requestUpdateNickName(String nickname,IUserInfoActivityCallBack callBack){
        mActivityCallBack= callBack;
        mModel=new UserInfoModel();
        mModel.updateNickName(nickname,this);
    }

    public void requestUpdateProfile(String profile,IUserInfoActivityCallBack callBack){
        mActivityCallBack=callBack;
        mModel=new UserInfoModel();
        mModel.updateProfile(profile,this);
    }

    public void requestUpdatePortrait(String filePath,IUserInfoActivityCallBack callBack){
        mActivityCallBack=callBack;
        mModel=new UserInfoModel();
        mModel.updatePortrait(filePath,this);

    }

    @Override
    public void getUpdateNickNameResult(boolean result) {
        mActivityCallBack.getUpdateNickNameResult(result);
    }

    @Override
    public void getUpdateProfileResult(boolean result) {
        mActivityCallBack.getUpdateProfileResult(result);
    }

    @Override
    public void getUpdatePortraitResult(boolean result) {
        mActivityCallBack.getUpdatePortraitResult(result);
    }
}
