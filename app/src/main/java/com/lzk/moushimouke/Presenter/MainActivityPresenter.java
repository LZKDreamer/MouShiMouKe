package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.MainActivityModel;
import com.lzk.moushimouke.View.Interface.IMainActivityCallBack;
import com.lzk.moushimouke.View.Interface.IMainActivityPresenterCallBack;

/**
 * Created by huqun on 2018/6/2.
 */

public class MainActivityPresenter implements IMainActivityPresenterCallBack {
    private IMainActivityCallBack mActivityCallBack;
    private MainActivityModel mActivityModel;

    public void login(String phoneNumber, String password, IMainActivityCallBack callBack){
        mActivityCallBack=callBack;
        mActivityModel=new MainActivityModel();
        mActivityModel.login(phoneNumber,password,this);
    }

    @Override
    public void getLoginResult(boolean result) {
        mActivityCallBack.getLoginResult(result);
    }
}
