package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.HomeFragmentModel;
import com.lzk.moushimouke.View.Interface.IHomeFragmentCallBack;
import com.lzk.moushimouke.View.Interface.IHomeFragmentPresenterCallBack;

import java.util.List;

/**
 * Created by huqun on 2018/5/30.
 */

public class HomeFragmentPresenter implements IHomeFragmentPresenterCallBack{
    private HomeFragmentModel mModel;
    private IHomeFragmentCallBack mIHomeFragmentCallBack;

    public void requestInitOrRefreshData(int page,int limit,int type,IHomeFragmentCallBack mIHomeFragmentCallBack){
        this.mIHomeFragmentCallBack=mIHomeFragmentCallBack;
        mModel=new HomeFragmentModel();
        mModel.requestRefreshData(page,limit,type,this);
    }



    @Override
    public void getInitOrRefreshResult(List<Post> postList, boolean result) {
        mIHomeFragmentCallBack.getInitOrRefreshDataResult(postList,result);

    }

    @Override
    public void getLoadMoreResult(List<Post> postList, boolean result) {
        mIHomeFragmentCallBack.getLoadMoreDataResult(postList,result);
    }

    @Override
    public void getFollowNumEmptyResult(boolean result) {
        mIHomeFragmentCallBack.getFollowNumEmptyResult(false);
    }

    @Override
    public void getDataErrorResult(boolean result) {
        mIHomeFragmentCallBack.getDataErrorResult(false);
    }
}
