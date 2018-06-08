package com.lzk.moushimouke.Model;

import android.util.Log;
import android.widget.Toast;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.LoginPresenter;
import com.lzk.moushimouke.View.Interface.IUserInfoPresenterCallBack;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by huqun on 2018/6/2.
 */

public class UserInfoModel {
    private IUserInfoPresenterCallBack mPresenterCallBack;
    MyUser user=BmobUser.getCurrentUser(MyUser.class);

    public void updateNickName(String nickname,IUserInfoPresenterCallBack callBack){
        mPresenterCallBack=callBack;

        user.setUsername(nickname);
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    fetchUserInfo();
                    mPresenterCallBack.getUpdateNickNameResult(true);
                }else {
                    mPresenterCallBack.getUpdateNickNameResult(false);
                }
            }
        });
    }

    public void updateProfile(String profile,IUserInfoPresenterCallBack callBack){
        mPresenterCallBack=callBack;
        String id=user.getObjectId();
        MyUser newUser=new MyUser();
        newUser.setObjectId(id);
        newUser.setProfile(profile);
        newUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    fetchUserInfo();
                    mPresenterCallBack.getUpdateProfileResult(true);
                }else {
                    mPresenterCallBack.getUpdateProfileResult(false);
                }
            }
        });
    }

    private void fetchUserInfo(){
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                return;
            }
        });
    }

    public void updatePortrait(String filePath,IUserInfoPresenterCallBack callBack){
        mPresenterCallBack=callBack;
        final BmobFile file=new BmobFile(new File(filePath));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    updatePortraitUrl(file.getFileUrl());
                }else {
                    mPresenterCallBack.getUpdatePortraitResult(false);
                }
            }
        });
    }

    private void updatePortraitUrl(String path){
        String id=user.getObjectId();
        MyUser otherUser=new MyUser();
        otherUser.setPortrait(path);
        otherUser.update(id,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    fetchUserInfo();
                    mPresenterCallBack.getUpdatePortraitResult(true);
                }else {
                    mPresenterCallBack.getUpdatePortraitResult(false);
                }
            }
        });
    }
}
