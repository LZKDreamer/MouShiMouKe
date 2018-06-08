package com.lzk.moushimouke.Model;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.ResetPasswordPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IResetPasswordModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by huqun on 2018/4/20.
 */

public class ResetPasswordModel implements IResetPasswordModel{
    private List<String> phoneList;

    @Override
    public void requestVerificationCode(String phoneNumber) {
        String smsModel= MyApplication.getContext().getResources().getString(R.string.sms_model);
        BmobSMS.requestSMSCode(phoneNumber, smsModel, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e==null){
                    ResetPasswordPresenter.sendVerificationCodeResult(true);
                }else {
                    ResetPasswordPresenter.sendVerificationCodeResult(false);
                }
            }
        });
    }

    @Override
    public void requestResetPassword(String code, String password) {
        MyUser.resetPasswordBySMSCode(code, password, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    ResetPasswordPresenter.resetPasswordResult(true);
                }else {
                    ResetPasswordPresenter.resetPasswordResult(false);
                }
            }
        });
    }

    @Override
    public void requestCheckPhoneNumVerified(final String phoneNumber) {
        phoneList=new ArrayList<>();
        phoneList.clear();
        BmobQuery<MyUser> query=new BmobQuery<>();
        query.addQueryKeys("mobilePhoneNumber");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                for (MyUser user:list){
                    phoneList.add(user.getMobilePhoneNumber());
                }
                if (phoneList.contains(phoneNumber)){
                    ResetPasswordPresenter.checkPhoneResult(true);
                }else {
                    ResetPasswordPresenter.checkPhoneResult(false);
                }
            }
        });
    }
}
