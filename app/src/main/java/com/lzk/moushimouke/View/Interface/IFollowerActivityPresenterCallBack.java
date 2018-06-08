package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.Post;

import java.util.List;

/**
 * Created by huqun on 2018/6/4.
 */

public interface IFollowerActivityPresenterCallBack {
    void getFollowerDataResult(List<Follow> followerList, List<Post> postList,List<Follow> followList, boolean result);
}
