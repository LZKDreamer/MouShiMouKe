package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.ExploreItemModel;
import com.lzk.moushimouke.Model.FollowActivityModel;
import com.lzk.moushimouke.View.Activity.MainActivity;
import com.lzk.moushimouke.View.Interface.IFollowActivityCallBack;
import com.lzk.moushimouke.View.Interface.IFollowActivityPresenterCallBack;

import java.util.List;

/**
 * Created by huqun on 2018/6/3.
 */

public class FollowActivityPresenter implements IFollowActivityPresenterCallBack{
    private IFollowActivityCallBack mActivityCallBack;
    private FollowActivityModel mActivityModel;

    public void requestFollowData(IFollowActivityCallBack callBack){
        mActivityCallBack=callBack;
        mActivityModel=new FollowActivityModel();
        mActivityModel.requestFollowData(this);
    }

    @Override
    public void getFollowDataResult(List<Follow> followList, List<Post> postList, boolean result) {
        mActivityCallBack.getFollowDataResult(followList,postList,result);
    }

    public void requestUpdateFollowState(boolean followState, MyUser followUser){
        ExploreItemModel model=new ExploreItemModel();
        model.updateFollow(followState,followUser);
    }
}
