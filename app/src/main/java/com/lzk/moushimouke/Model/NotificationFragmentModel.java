package com.lzk.moushimouke.Model;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Notification;
import com.lzk.moushimouke.View.Interface.INotificationPresenterCallBack;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by huqun on 2018/6/5.
 */

public class NotificationFragmentModel {
    private INotificationPresenterCallBack mPresenterCallBack;

    public void requestNotificationData(INotificationPresenterCallBack callBack){
        mPresenterCallBack=callBack;
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Notification> query=new BmobQuery<>();
        query.addWhereEqualTo("otherUser",user);
        query.include("user,otherUser,comment,post");
        query.order("-createdAt");
        query.findObjects(new FindListener<Notification>() {
            @Override
            public void done(List<Notification> list, BmobException e) {
                if (e==null){
                    mPresenterCallBack.getRequestNotificationDataResult(list,true);
                }else {
                    mPresenterCallBack.getRequestNotificationDataResult(null,false);
                }
            }
        });
    }
}
