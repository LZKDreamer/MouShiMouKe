package com.lzk.moushimouke.Model;

import android.util.Log;
import android.widget.Toast;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.HomeFragmentPresenter;
import com.lzk.moushimouke.View.Fragment.HomeFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by huqun on 2018/5/30.
 */

public class HomeFragmentModel {
    private HomeFragmentPresenter mPresenter;
    private String[] userArray;

    public void requestRefreshData(final int page, final int limit, final int type, HomeFragmentPresenter homeFragmentPresenter){
        mPresenter=homeFragmentPresenter;
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        final BmobQuery<Follow> query=new BmobQuery<>();
        query.addWhereEqualTo("mUser",user);
        query.include("mFollowUser");
        query.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> list, BmobException e) {
                if (e==null){
                   userArray=getUserIdArray(list);
                    if (list.size()!=0) {
                        if (type == HomeFragment.REQUEST_TYPE_REFRESH) {
                            BmobQuery<MyUser> innerQuery=new BmobQuery<>();
                            innerQuery.addWhereContainedIn("objectId",Arrays.asList(userArray));
                            BmobQuery<Post> postQuery=new BmobQuery<>();
                            postQuery.addWhereMatchesQuery("mUser","_User",innerQuery);
                            postQuery.include("mUser,oldUser");
                            postQuery.setLimit(limit);
                            postQuery.order("-createdAt");
                            postQuery.findObjects(new FindListener<Post>() {
                                @Override
                                public void done(List<Post> list, BmobException e) {
                                    if (e==null){
                                        mPresenter.getInitOrRefreshResult(list,true);
                                    }else {
                                        mPresenter.getInitOrRefreshResult(list,false);
                                    }
                                }
                            });

                        } else if (type == HomeFragment.REQUEST_TYPE_LOAD_MORE) {
                            BmobQuery<MyUser> innerQuery=new BmobQuery<>();
                            innerQuery.addWhereContainedIn("objectId",Arrays.asList(userArray));
                            BmobQuery<Post> postQuery=new BmobQuery<>();
                            postQuery.addWhereMatchesQuery("mUser","_User",innerQuery);
                            postQuery.include("mUser,oldUser");
                            postQuery.setLimit(limit);
                            postQuery.setSkip(page*limit);
                            postQuery.order("-createdAt");
                            postQuery.findObjects(new FindListener<Post>() {
                                @Override
                                public void done(List<Post> list, BmobException e) {
                                    if (e==null){
                                        mPresenter.getLoadMoreResult(list,true);
                                    }else {
                                        mPresenter.getLoadMoreResult(list,false);
                                    }
                                }
                            });
                        }
                    }else {
                        mPresenter.getFollowNumEmptyResult(false);
                    }
                }else {
                    mPresenter.getDataErrorResult(false);
                }
            }
        });
    }


    private String[] getUserIdArray(List<Follow> followList){
        userArray=null;
        userArray=new String[followList.size()];
       for (int i=0;i<followList.size();i++){
           userArray[i]=followList.get(i).getFollowUser().getObjectId();
       }
       return userArray;
    }
}
