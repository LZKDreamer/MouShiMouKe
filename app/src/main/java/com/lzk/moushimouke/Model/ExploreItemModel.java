package com.lzk.moushimouke.Model;


import android.util.Log;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.Like;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.EventBus.GetFollowDataEB;
import com.lzk.moushimouke.Presenter.ExploreItemPresenter;
import com.lzk.moushimouke.View.Fragment.ExploreFragment;
import com.lzk.moushimouke.View.Interface.IExploreItemModel;

import org.greenrobot.eventbus.EventBus;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by huqun on 2018/5/5.
 */

public class ExploreItemModel implements IExploreItemModel {

    /*从服务器中获取Post表的内容，每次请求10条数据*/
    @Override
    public void getPostData(final int page, int count, int type) {
        BmobQuery<Post> postQuery = new BmobQuery<>();
        postQuery.order("-updatedAt");
        postQuery.setLimit(10);
        postQuery.addWhereEqualTo("type",1);//查询原创帖子，即不查询转发的帖子
        postQuery.include("mUser,oldUser");
        if (type == ExploreFragment.TYPE_FIRST_REFRESH||type==ExploreFragment.TYPE_PULL_REFRESH) {
            postQuery.findObjects(new FindListener<Post>() {
                @Override
                public void done(final List<Post> list, BmobException e) {
                    if (e == null) {
                        /*查询follow表，获取当前用户关注的人*/
                        queryFollowTable(list);

                    }
                }
            });
        }else if (type==ExploreFragment.TYPE_LOAD_MORE){
            postQuery.setSkip(page*count);//用了addWhereLessThanOrEqualTo来实现分页查询就不需要setSkip来跳过了。
            postQuery.findObjects(new FindListener<Post>() {
                @Override
                public void done(final List<Post> list, BmobException e) {
                    if (e == null&&list.size()!=0) {
                        /*lastTime=list.get(list.size()-1).getCreatedAt();*/
                        /*查询follow表，获取当前用户关注的人*/
                        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                        BmobQuery<Follow> query = new BmobQuery<>();
                        query.addWhereEqualTo("mUser", new BmobPointer(myUser));
                        query.findObjects(new FindListener<Follow>() {
                            @Override
                            public void done(List<Follow> followList, BmobException e) {
                                if (e == null) {
                                    EventBus.getDefault().post(new GetFollowDataEB(followList));
                                }
                                ExploreItemPresenter.returnPostData(list, ExploreFragment.TYPE_LOAD_MORE, true);
                            }
                        });
                    }else {
                        ExploreItemPresenter.returnPostData(list,ExploreFragment.TYPE_LOAD_MORE,false);
                    }
                }
            });
        }
    }

    private void queryFollowTable(final List<Post> postList){
        MyUser myUser= BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Follow> query=new BmobQuery<>();
        query.addWhereEqualTo("mUser",new BmobPointer(myUser));
        query.include("mFollowUser");
        query.findObjects(new FindListener<Follow>() {
            @Override
            public void done(List<Follow> followList, BmobException e) {
                if (e==null){
                    EventBus.getDefault().post(new GetFollowDataEB(followList));
                }
                ExploreItemPresenter.returnPostData(postList,ExploreFragment.TYPE_PULL_REFRESH,true);
            }
        });
    }

    @Override
    public void updateFollow(boolean isFollowed, MyUser postUser) {//true为关注，false为取消关注。
        MyUser curUser=BmobUser.getCurrentUser(MyUser.class);
        final Follow follow=new Follow();
        if (isFollowed){//向Follow表添加数据
            follow.setUser(curUser);
            follow.setFollowUser(postUser);
            follow.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e==null){
                        return;
                    }else {
                        return;
                    }
                }
            });
        }else {//向Follow表删除数据
            BmobQuery<Follow> q1=new BmobQuery<>();
            q1.addWhereEqualTo("mUser",curUser);
            BmobQuery<Follow> q2=new BmobQuery<>();
            q2.addWhereEqualTo("mFollowUser",postUser);
            List<BmobQuery<Follow>> andQuery=new ArrayList<>();
            andQuery.add(q1);
            andQuery.add(q2);
            BmobQuery<Follow> query=new BmobQuery<>();
            query.and(andQuery);
            query.findObjects(new FindListener<Follow>() {
                @Override
                public void done(List<Follow> list, BmobException e) {
                    if (e==null){
                        if (list!=null){
                            for (Follow followData:list){
                                String id=followData.getObjectId();
                                Follow followTable=new Follow();
                                followTable.setObjectId(id);
                                followTable.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null){
                                            return;
                                        }else {
                                            return;
                                        }
                                    }
                                });

                            }
                        }else {
                            return;
                        }
                    }else {
                    }
                }
            });
        }
    }

    @Override
    public void updateLikeNum(int num,String postId,int tag) {
        final Post post=new Post();
        post.setObjectId(postId);
        MyUser user=BmobUser.getCurrentUser(MyUser.class);
        Like like=new Like();
        if (tag==1){
            post.increment("like");
            like.setPost(post);
            like.setUser(user);
            like.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    return;
                }
            });

        }else {
            post.increment("like",-1);
            BmobQuery<Like> q1=new BmobQuery<>();
            q1.addWhereEqualTo("post",postId);
            BmobQuery<Like> q2=new BmobQuery<>();
            q2.addWhereEqualTo("user",user.getObjectId());
            List<BmobQuery<Like>> queryList=new ArrayList<>();
            queryList.add(q1);
            queryList.add(q2);
            BmobQuery<Like> query=new BmobQuery<>();
            query.and(queryList);
            query.findObjects(new FindListener<Like>() {
                @Override
                public void done(List<Like> list, BmobException e) {
                    for (Like mLike:list){
                        String likeId=mLike.getObjectId();
                        Like deleteLike=new Like();
                        deleteLike.setObjectId(likeId);
                        deleteLike.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                return;
                            }
                        });
                    }
                }
            });

        }
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                return;
            }
        });

    }

    @Override
    public void deletePost(String objectId) {
        Post post=new Post();
        post.setObjectId(objectId);
        post.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                return;
            }
        });
    }


}
