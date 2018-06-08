package com.lzk.moushimouke.Presenter;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.HomePageActivityModel;
import com.lzk.moushimouke.View.Interface.IHomePageActivityDataCallBack;
import com.lzk.moushimouke.View.Interface.IHomePageActivityPresenterCallBack;
import com.lzk.moushimouke.View.Interface.IHomePageImageCallBack;
import com.lzk.moushimouke.View.Interface.IHomePagePostCallBack;

import java.util.List;

/**
 * Created by huqun on 2018/5/28.
 */

public class HomePageActivityPresenter implements IHomePageActivityPresenterCallBack {
    private HomePageActivityModel mModel;
    private IHomePageActivityDataCallBack mActivityDataCallBack;
    private IHomePagePostCallBack mPostCallBack;
    private IHomePageImageCallBack mImageCallBack;


    public void requestPostData(MyUser user,IHomePageActivityDataCallBack activityDataCallBack){
        mModel=new HomePageActivityModel();
        mActivityDataCallBack=activityDataCallBack;
        mModel.requestPostData(user,this);
    }

    @Override
    public void getRequestDataCallBack(List<Post> postList, int followerNum) {
        mActivityDataCallBack.getPostAndFollowerResult(postList,followerNum);
    }

    public void requestCurUserPostData(MyUser user,IHomePagePostCallBack postCallBack){
        mPostCallBack=postCallBack;
        mModel=new HomePageActivityModel();
        mModel.requestCurUserPostData(user,this);
    }

    @Override
    public void getCurUserPostListResult(List<Post> postList) {
        mPostCallBack.getCurUserPostList(postList);
    }



    public void requestCurUserPostImage(MyUser user, IHomePageImageCallBack callBack){
        mImageCallBack=callBack;
        mModel=new HomePageActivityModel();
        mModel.requestCurUserImageData(user,this);
    }

    @Override
    public void getCurUserImageResult(List<Post> postList) {
        mImageCallBack.getHomePageImageResult(postList);
    }
}
