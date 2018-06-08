package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Post;

import java.util.List;

/**
 * Created by huqun on 2018/5/28.
 */

public interface IHomePageActivityDataCallBack {
    void getPostAndFollowerResult(List<Post> postList,int followerNum);
}
