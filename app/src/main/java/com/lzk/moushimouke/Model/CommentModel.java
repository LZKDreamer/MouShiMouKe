package com.lzk.moushimouke.Model;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.Comment;
import com.lzk.moushimouke.Model.Bean.InnerComment;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Notification;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.CommentPresenter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by huqun on 2018/5/22.
 */

public class CommentModel {
    private CommentPresenter mCommentPresenter;
    private Post post;

    public void sendComment(final String comment, final String postId, MyUser user, final MyUser postUser){
        mCommentPresenter=new CommentPresenter();
        Comment commentTable=new Comment();
        post=new Post();
        post.setObjectId(postId);
        commentTable.setComment(comment);
        commentTable.setPost(post);
        commentTable.setUser(user);
        commentTable.setLike(0);
        commentTable.setCommentNum(0);
        commentTable.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    post.increment("comment",1);
                    post.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            mCommentPresenter.getCommentSendResult(true);
                            Notification notification=new Notification();
                            notification.setPost(post);
                            notification.setOtherUser(postUser);
                            MyUser curUser= BmobUser.getCurrentUser(MyUser.class);
                            notification.setUser(curUser);
                            notification.setType(1);//1表示评论
                            notification.setContent(comment);
                            notification.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    return;
                                }
                            });

                        }
                    });
                }else {
                    mCommentPresenter.getCommentSendResult(false);
                }
            }
        });
    }

    public void requestComment(String postId){
        final BmobQuery<Comment> query=new BmobQuery<>();
        query.addWhereEqualTo("post",postId);
        query.include("user");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                mCommentPresenter=new CommentPresenter();
                if (e==null){
                    mCommentPresenter.getCommentDataResult(true,list);

                }else {
                    mCommentPresenter.getCommentDataResult(false,list);
                }

            }
        });
    }

    public void sendUpdateCommentLikeNum(String commentId,int tag){
        Comment comment=new Comment();
        if (tag==1){//增加
            comment.setObjectId(commentId);
            comment.increment("like",1);
        }else {
            comment.setObjectId(commentId);
            comment.increment("like",-1);
        }
        comment.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                return;
            }
        });

    }
}
