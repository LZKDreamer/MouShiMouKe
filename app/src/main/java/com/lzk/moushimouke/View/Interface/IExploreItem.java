package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Post;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by huqun on 2018/5/5.
 */

public interface IExploreItem {
    public abstract void setPostData(List<Post> postList,int type,boolean result);//result判断是否有数据，true为有。
}
