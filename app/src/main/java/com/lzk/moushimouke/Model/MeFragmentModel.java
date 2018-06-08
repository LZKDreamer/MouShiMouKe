package com.lzk.moushimouke.Model;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.View.Interface.IMeFragmentPresenterCallBack;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by huqun on 2018/5/31.
 */

public class MeFragmentModel {
    private IMeFragmentPresenterCallBack mMeFragmentPresenterCallBack;

    public void requestUserInfo(final MyUser user, IMeFragmentPresenterCallBack meFragmentPresenterCallBack){
        mMeFragmentPresenterCallBack=meFragmentPresenterCallBack;
        BmobQuery<MyUser> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("objectId",user.getObjectId());
        userBmobQuery.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e==null){
                   requestPostData(user,list);
                }
            }
        });

    }

    private void requestPostData(final MyUser user, final List<MyUser> userList){
        BmobQuery<Post> postQuery=new BmobQuery<>();
        postQuery.addWhereEqualTo("mUser",user);
        postQuery.include("mUser");
        postQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    requestFollowData(list,user,userList);
                }else {
                    mMeFragmentPresenterCallBack.getUserInfoResult(null,null,null,false,userList);
                }
            }
        });
    }

    private void requestFollowData(final List<Post> postList, final MyUser user, final List<MyUser> userList){
        BmobQuery<Follow> followQuery=new BmobQuery<>();
        followQuery.addWhereEqualTo("mUser",user);
        followQuery.include("mFollowUser");
        followQuery.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> list, BmobException e) {
                if (e==null){
                    requestFollowerData(postList,list,user,userList);
                }else {
                    mMeFragmentPresenterCallBack.getUserInfoResult(null,null,null,false,userList);
                }
            }
        });
    }

    private void requestFollowerData(final List<Post> postList, final List<Follow> followList, MyUser user, final List<MyUser> userList){
        BmobQuery<Follow> followQuery=new BmobQuery<>();
        followQuery.addWhereEqualTo("mFollowUser",user);
        followQuery.include("mUser");
        followQuery.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> list, BmobException e) {
                if (e==null){
                    mMeFragmentPresenterCallBack.getUserInfoResult(postList,followList,list,true,userList);
                }else {
                    mMeFragmentPresenterCallBack.getUserInfoResult(null,null,null,false,userList);
                }
            }
        });
    }
}
