package com.lzk.moushimouke.Presenter;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.ExploreItemModel;
import com.lzk.moushimouke.View.Fragment.ExploreFragment;
import com.lzk.moushimouke.View.Interface.IExploreItem;
import com.lzk.moushimouke.View.Interface.IExploreItemModel;

import java.util.List;

/**
 * Created by huqun on 2018/5/5.
 */

public class ExploreItemPresenter {
    private IExploreItemModel mItemModel;
    private IExploreItem mExploreItem;

    public ExploreItemPresenter(IExploreItem exploreItem){
        mExploreItem=exploreItem;
        mItemModel=new ExploreItemModel();
    }

    public void requestPostData(int page,int count,int type){
        mItemModel.getPostData(page,count,type);
    }

    public void requestUpdateFollow(boolean isFollowed, MyUser postUser){//true为关注，false为取消关注。
        mItemModel.updateFollow(isFollowed,postUser);
    }

    public void requestUpdateLikeNum(int likeNum,String postId,int tag){//1为递增，-1为递减
        mItemModel.updateLikeNum(likeNum,postId,tag);

    }

    public static void returnPostData(List<Post> postList,int type,boolean result){
        ExploreFragment.sExploreFragment.setPostData(postList,type,result);
    }

    public void requestDeletePost(String objectId){
        mItemModel=new ExploreItemModel();
        mItemModel.deletePost(objectId);
    }
}
