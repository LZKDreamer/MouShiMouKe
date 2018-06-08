package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.ExploreItemModel;
import com.lzk.moushimouke.Model.FollowerActivityModel;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.View.Interface.IFollowerActivityCallBack;
import com.lzk.moushimouke.View.Interface.IFollowerActivityPresenterCallBack;

import java.util.List;

/**
 * Created by huqun on 2018/6/4.
 */

public class FollowerActivityPresenter implements IFollowerActivityPresenterCallBack{
    private IFollowerActivityCallBack mActivityCallBack;
    private FollowerActivityModel mActivityModel;

    public void requestFollowerData(IFollowerActivityCallBack callBack){
        mActivityCallBack=callBack;
        mActivityModel=new FollowerActivityModel();
        mActivityModel.requestFollowerData(this);
    }

    @Override
    public void getFollowerDataResult(List<Follow> followerList, List<Post> postList,List<Follow> followList, boolean result) {
        mActivityCallBack.getFollowerDataResult(followerList,postList,followList,result);
    }

    public void requestUpdateFollow(boolean state, MyUser followUser){
        ExploreItemModel model=new ExploreItemModel();
        model.updateFollow(state,followUser);
    }
}
