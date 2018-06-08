package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Post;

import java.util.List;

/**
 * Created by huqun on 2018/5/30.
 */

public interface IHomeFragmentPresenterCallBack {
    void getInitOrRefreshResult(List<Post> postList,boolean result);
    void getLoadMoreResult(List<Post> postList,boolean result);
    void getFollowNumEmptyResult(boolean result);
    void getDataErrorResult(boolean result);
}
