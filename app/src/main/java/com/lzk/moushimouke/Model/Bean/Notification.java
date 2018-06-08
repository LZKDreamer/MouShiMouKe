package com.lzk.moushimouke.Model.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by huqun on 2018/6/5.
 */

public class Notification extends BmobObject {
    private MyUser user,otherUser;
    private Post post;
    private Comment comment;
    private String content;
    private Integer type;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public MyUser getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(MyUser otherUser) {
        this.otherUser = otherUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
