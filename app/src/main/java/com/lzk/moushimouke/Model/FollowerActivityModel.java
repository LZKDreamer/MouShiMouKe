package com.lzk.moushimouke.Model;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.FollowerActivityPresenter;
import com.lzk.moushimouke.View.Interface.IFollowerActivityPresenterCallBack;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by huqun on 2018/6/4.
 */

public class FollowerActivityModel {
    private IFollowerActivityPresenterCallBack mPresenterCallBack;

    public void requestFollowerData(IFollowerActivityPresenterCallBack callBack){
        mPresenterCallBack=callBack;
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Follow> query=new BmobQuery<>();
        query.addWhereEqualTo("mFollowUser",user);
        query.include("mUser,mFollowUser");
        query.order("-createdAt");
        query.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> list, BmobException e) {
                if(e==null){
                    requestFollowData(list);
                }else {
                    mPresenterCallBack.getFollowerDataResult(list,null,null,false);
                }

            }
        });
    }

    private void requestFollowData(final List followerList){
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Follow> query=new BmobQuery<>();
        query.addWhereEqualTo("mUser",user);
        query.include("mFollowUser");
        query.order("-createdAt");
        query.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> list, BmobException e) {
                if (e==null){
                    requestFollowerPostData(followerList,list);
                }else {
                    mPresenterCallBack.getFollowerDataResult(list,null,null,false);
                }
            }
        });
    }

    private void requestFollowerPostData(final List<Follow> followerList, final List<Follow> followList){
        BmobQuery<MyUser> innerQuery=new BmobQuery<>();
        String[] id=new String[followerList.size()];
        for (int i=0;i<followerList.size();i++){
            id[i]=followerList.get(i).getUser().getObjectId();
        }
        innerQuery.addWhereContainedIn("objectId", Arrays.asList(id));
        BmobQuery<Post> query=new BmobQuery<>();
        query.addWhereMatchesQuery("mUser","_User",innerQuery);
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    mPresenterCallBack.getFollowerDataResult(followerList,list,followList,true);
                }else {
                    mPresenterCallBack.getFollowerDataResult(followerList,null,null,false);
                }
            }
        });
    }
}
