package com.lzk.moushimouke.Model.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by huqun on 2018/5/17.
 */

public class Like extends BmobObject{
    private Post post;
    private MyUser user;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
