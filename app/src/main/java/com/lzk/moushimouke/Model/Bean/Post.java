package com.lzk.moushimouke.Model.Bean;


import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by huqun on 2018/5/2.
 */

public class Post extends BmobObject{
    private String description,thumbnail,repostText;
    private MyUser mUser,oldUser;
    private List<BmobFile> media;
    private Integer forward,like,comment,type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyUser getUser() {
        return mUser;
    }

    public void setUser(MyUser user) {
        mUser = user;
    }

    public List<BmobFile> getMedia() {
        return media;
    }

    public void setMedia(List<BmobFile> media) {
        this.media = media;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getForward() {
        return forward;
    }

    public void setForward(Integer forward) {
        this.forward = forward;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getRepostText() {
        return repostText;
    }

    public void setRepostText(String repostText) {
        this.repostText = repostText;
    }

    public MyUser getOldUser() {
        return oldUser;
    }

    public void setOldUser(MyUser oldUser) {
        this.oldUser = oldUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
