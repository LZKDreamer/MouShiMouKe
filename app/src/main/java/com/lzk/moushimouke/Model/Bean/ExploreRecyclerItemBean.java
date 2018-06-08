package com.lzk.moushimouke.Model.Bean;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by huqun on 2018/5/5.
 */

public class ExploreRecyclerItemBean {
    private String portrait;
    private String userName;
    private String postTime;
    private List<BmobFile> mediaList;
    private int forwardNum;
    private int likeNum;
    private int commentNum;
    private String description;
    private String thumbnailUri;
    private FollowStateBean isFollowed;
    private boolean isCurUser;
    private MyUser postUser;
    private Post post;
    private String postId;

    public ExploreRecyclerItemBean(String portrait,String userName,
                                   String postTime,List<BmobFile> mediaList,
                                   int forwardNum,int likeNum,
                                   String description,String thumbnailUri,
                                   FollowStateBean isFollowed,boolean isCurUser,
                                   MyUser postUser,
                                   Post post,int commentNum,String postId){
        this.portrait=portrait;
        this.userName=userName;
        this.postTime=postTime;
        this.mediaList=mediaList;
        this.forwardNum=forwardNum;
        this.likeNum=likeNum;
        this.description=description;
        this.thumbnailUri=thumbnailUri;
        this.isFollowed=isFollowed;
        this.isCurUser=isCurUser;
        this.postUser=postUser;
        this.post=post;
        this.commentNum=commentNum;
        this.postId=postId;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostTime() {
        return postTime;
    }

    public List<BmobFile> getMediaList() {
        return mediaList;
    }

    public int getForwardNum() {
        return forwardNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUri() {
        return thumbnailUri;
    }

    public FollowStateBean isFollowed() {
        return isFollowed;
    }

    public boolean isCurUser() {
        return isCurUser;
    }

    public MyUser getPostUser() {
        return postUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public void setMediaList(List<BmobFile> mediaList) {
        this.mediaList = mediaList;
    }

    public void setForwardNum(int forwardNum) {
        this.forwardNum = forwardNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setThumbnailUri(String thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public void setFollowed(FollowStateBean followed) {
        isFollowed = followed;
    }

    public void setCurUser(boolean curUser) {
        isCurUser = curUser;
    }

    public void setPostUser(MyUser postUser) {
        this.postUser = postUser;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
