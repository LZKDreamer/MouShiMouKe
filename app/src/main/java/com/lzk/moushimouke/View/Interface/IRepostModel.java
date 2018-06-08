package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;

/**
 * Created by huqun on 2018/5/18.
 */

public interface IRepostModel {
    public abstract void repost(MyUser user, Post post, String description);
}
