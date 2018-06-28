package com.lzk.moushimouke.Model;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.View.Interface.IHomePageActivityPresenterCallBack;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by huqun on 2018/5/28.
 */

public class HomePageActivityModel {
    private IHomePageActivityPresenterCallBack mPresenterCallBack;
    private List<Post> mPosts;

    public void requestPostData(final MyUser user, IHomePageActivityPresenterCallBack presenterCallBack){
        mPresenterCallBack=presenterCallBack;
        BmobQuery<Post> query=new BmobQuery<>();
        query.addWhereEqualTo("mUser",user);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    mPosts=list;
                    BmobQuery<Follow> followBmobQuery=new BmobQuery<>();
                    followBmobQuery.addWhereEqualTo("mFollowUser",user);
                    followBmobQuery.count(Follow.class, new CountListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e==null) {
                                mPresenterCallBack.getRequestDataCallBack(mPosts, integer);
                            }
                        }
                    });
                }
            }
        });

    }

    public void requestCurUserPostData(MyUser user,IHomePageActivityPresenterCallBack presenterCallBack){
        mPresenterCallBack=presenterCallBack;
        BmobQuery<Post> query=new BmobQuery<>();
        query.addWhereEqualTo("mUser",user);
        query.include("mUser,oldUser");
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    mPresenterCallBack.getCurUserPostListResult(list);
                }
            }
        });
    }

    public void requestCurUserImageData(MyUser user,IHomePageActivityPresenterCallBack presenterCallBack){
        mPresenterCallBack=presenterCallBack;
        BmobQuery<Post> query0=new BmobQuery<>();
        query0.addWhereEqualTo("mUser",user);

        BmobQuery<Post> query1=new BmobQuery<>();
        query1.addWhereEqualTo("type",1);

        List<BmobQuery<Post>> queries=new ArrayList<>();
        queries.add(query0);
        queries.add(query1);
        BmobQuery<Post> query=new BmobQuery<>();
        query.and(queries);
        query.include("mUser");
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    mPresenterCallBack.getCurUserImageResult(list);

                }
            }
        });
    }
}
