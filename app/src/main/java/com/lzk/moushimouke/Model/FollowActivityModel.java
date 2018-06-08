package com.lzk.moushimouke.Model;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.FollowActivityPresenter;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by huqun on 2018/6/3.
 */

public class FollowActivityModel {
    private FollowActivityPresenter mPresenter;

    public void requestFollowData(FollowActivityPresenter presenter){
        mPresenter=presenter;
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Follow> query=new BmobQuery<>();
        query.addWhereEqualTo("mUser",user);
        query.include("mUser,mFollowUser");
        query.order("-createdAt");
        query.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> list, BmobException e) {
                if (e==null){
                    requestFollowUserPost(list);

                }else {
                    mPresenter.getFollowDataResult(list,null,false);
                }
            }
        });
    }

    private void requestFollowUserPost(final List<Follow> followList){
        BmobQuery<MyUser> innerQuery=new BmobQuery<>();
        String[] id=new String[followList.size()];
        for (int i=0;i<followList.size();i++){
            id[i]=followList.get(i).getFollowUser().getObjectId();
        }
        innerQuery.addWhereContainedIn("objectId", Arrays.asList(id));
        BmobQuery<Post> query=new BmobQuery<>();
        query.addWhereMatchesQuery("mUser","_User",innerQuery);
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e==null){
                    mPresenter.getFollowDataResult(followList,list,true);
                }else {
                    mPresenter.getFollowDataResult(followList,null,false);
                }
            }
        });
    }
}
