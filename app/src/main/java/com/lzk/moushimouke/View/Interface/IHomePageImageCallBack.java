package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;

import java.util.List;

/**
 * Created by huqun on 2018/5/29.
 */

public interface IHomePageImageCallBack {
    void getHomePageImageResult(List<Post> posts);
}
