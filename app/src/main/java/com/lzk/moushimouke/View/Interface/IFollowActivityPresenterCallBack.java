package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.Post;

import java.util.List;

/**
 * Created by huqun on 2018/6/3.
 */

public interface IFollowActivityPresenterCallBack {
    void getFollowDataResult(List<Follow> followList, List<Post> postList, boolean result);
}
