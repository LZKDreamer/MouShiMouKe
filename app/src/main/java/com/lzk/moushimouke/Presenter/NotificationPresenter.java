package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.Bean.Notification;
import com.lzk.moushimouke.Model.NotificationFragmentModel;
import com.lzk.moushimouke.View.Interface.INotificationFragmentCallBack;
import com.lzk.moushimouke.View.Interface.INotificationPresenterCallBack;

import java.util.List;

/**
 * Created by huqun on 2018/6/5.
 */

public class NotificationPresenter implements INotificationPresenterCallBack{
    private INotificationFragmentCallBack mFragmentCallBack;
    private NotificationFragmentModel mFragmentModel;

    public void requestNotificationData(INotificationFragmentCallBack callBack){
        mFragmentCallBack=callBack;
        mFragmentModel=new NotificationFragmentModel();
        mFragmentModel.requestNotificationData(this);
    }

    @Override
    public void getRequestNotificationDataResult(List<Notification> notificationList, boolean result) {
        mFragmentCallBack.getNotificationDataResult(notificationList,result);
    }
}
