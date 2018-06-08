package com.lzk.moushimouke.Model;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.RegisterPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IRegisterModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by huqun on 2018/4/20.
 */

public class RegisterModel implements IRegisterModel {
    private List<String> nameList;
    private List<String> phoneNumList;
    @Override
    public void checkUserNameExits(final String userName) {
        nameList=new ArrayList<>();
        nameList.clear();
        BmobQuery<MyUser> query=new BmobQuery<>();
        query.addQueryKeys("username");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                for (MyUser myUser :list){
                    nameList.add(myUser.getUsername());
                }
                if (nameList.contains(userName)){
                    RegisterPresenter.userNameExistResult(true);
                }else {
                    RegisterPresenter.userNameExistResult(false);
                }
            }
        });
    }

    @Override
    public void sendVerificationCode(String phoneNumber) {
        String smsModel=MyApplication.getContext().getResources().getString(R.string.sms_model);
        BmobSMS.requestSMSCode(phoneNumber, smsModel, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e==null){
                    RegisterPresenter.sendVerificationCodeResult(true);
                }else {
                    RegisterPresenter.sendVerificationCodeResult(false);
                }
            }
        });
    }

    @Override
    public void register(String userName, String phoneNumber, String code, String password) {
        MyUser myUser =new MyUser();
        myUser.setMobilePhoneNumber(phoneNumber);
        myUser.setUsername(userName);
        myUser.setPassword(password);
        myUser.signOrLogin(code, new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e==null){
                    RegisterPresenter.registerResult(true);
                }else {
                    RegisterPresenter.registerResult(false);
                }
            }
        });
    }

    @Override
    public void checkPhoneNumVerified(final String phoneNumber) {
        phoneNumList=new ArrayList<>();
        phoneNumList.clear();
        BmobQuery<MyUser> query=new BmobQuery<>();
        query.addQueryKeys("mobilePhoneNumber");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                for (MyUser myUser :list){
                    phoneNumList.add(myUser.getMobilePhoneNumber());
                }
                if (phoneNumList.contains(phoneNumber)){
                    RegisterPresenter.showPhoneNumResul(true);
                }else {
                    RegisterPresenter.showPhoneNumResul(false);
                }
            }
        });
    }
}