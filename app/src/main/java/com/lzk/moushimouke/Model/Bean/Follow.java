package com.lzk.moushimouke.Model.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by huqun on 2018/5/5.
 */

public class Follow extends BmobObject {
    private MyUser mUser;
    private MyUser mFollowUser;

    public MyUser getUser() {
        return mUser;
    }

    public void setUser(MyUser user) {
        mUser = user;
    }

    public MyUser getFollowUser() {
        return mFollowUser;
    }

    public void setFollowUser(MyUser followUser) {
        mFollowUser = followUser;
    }
}
