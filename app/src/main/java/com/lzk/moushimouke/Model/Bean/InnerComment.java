package com.lzk.moushimouke.Model.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by huqun on 2018/5/22.
 */

public class InnerComment extends BmobObject implements Serializable{
    private MyUser user;
    private Comment comment;
    private String content;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
