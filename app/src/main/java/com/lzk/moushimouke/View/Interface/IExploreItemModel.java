package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;

import java.util.List;

/**
 * Created by huqun on 2018/5/5.
 */

public interface IExploreItemModel {
    public abstract void getPostData(int page,int count,int type);
    public abstract void updateFollow(boolean isFollowed,MyUser postUser);
    public abstract void updateLikeNum(int num,String postId,int tag);
    public abstract void deletePost(String objectId);

}
