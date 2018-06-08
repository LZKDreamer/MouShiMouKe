package com.lzk.moushimouke.Model.Bean;


import cn.bmob.v3.BmobObject;

/**
 * Created by huqun on 2018/5/18.
 */

public class Repost extends BmobObject{
    private MyUser user;
    private Post post;
    private String description;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
