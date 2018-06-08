package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;

import java.util.List;

/**
 * Created by huqun on 2018/5/31.
 */

public interface IMeFragmentPresenterCallBack {
    void getUserInfoResult(List<Post> postList, List<Follow> followList
            ,List<Follow> followerList,boolean result,List<MyUser> userList);


}
