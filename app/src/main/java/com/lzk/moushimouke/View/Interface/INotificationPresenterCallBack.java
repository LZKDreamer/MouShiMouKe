package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Notification;

import java.util.List;

/**
 * Created by huqun on 2018/6/5.
 */

public interface INotificationPresenterCallBack {
    void getRequestNotificationDataResult(List<Notification> notificationList,boolean result);
}
