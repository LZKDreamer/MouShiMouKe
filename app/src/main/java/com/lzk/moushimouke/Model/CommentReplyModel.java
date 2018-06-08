package com.lzk.moushimouke.Model;

import android.util.Log;

import com.lzk.moushimouke.Model.Bean.Comment;
import com.lzk.moushimouke.Model.Bean.InnerComment;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Notification;
import com.lzk.moushimouke.Presenter.CommentReplyPresenter;
import com.lzk.moushimouke.View.Activity.CommentReplyActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by huqun on 2018/5/24.
 */

public class CommentReplyModel {
    private CommentReplyPresenter mCommentReplyPresenter;

    public void sendCommentReply(final String commentId, final MyUser user, final String content, final MyUser commentUser){
        mCommentReplyPresenter=new CommentReplyPresenter();
        InnerComment innerComment=new InnerComment();
        final Comment comment=new Comment();
        comment.setObjectId(commentId);
        innerComment.setComment(comment);
        innerComment.setUser(user);
        innerComment.setContent(content);
        innerComment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                  Comment commentTable=new Comment();
                  commentTable.setObjectId(commentId);
                  commentTable.increment("commentNum",1);
                  commentTable.update(new UpdateListener() {
                      @Override
                      public void done(BmobException e) {
                          if (e==null){
                              mCommentReplyPresenter.getCommentReplySendResult(true);
                              Notification notification=new Notification();
                              notification.setUser(user);
                              notification.setOtherUser(commentUser);
                              notification.setComment(comment);
                              notification.setContent(content);
                              notification.setType(2);
                              notification.save(new SaveListener<String>() {
                                  @Override
                                  public void done(String s, BmobException e) {
                                      return;
                                  }
                              });
                          }
                      }
                  });
                }else {
                    mCommentReplyPresenter.getCommentReplySendResult(false);
                }
            }
        });
    }

    public void requestCommentReplyData(String commentId){
        mCommentReplyPresenter=new CommentReplyPresenter();
        BmobQuery<InnerComment> query=new BmobQuery<>();
        Comment comment=new Comment();
        comment.setObjectId(commentId);
        query.addWhereEqualTo("comment",commentId);
        query.include("user");
        query.order("-updatedAt");
        query.findObjects(new FindListener<InnerComment>() {
            @Override
            public void done(List<InnerComment> list, BmobException e) {
                if (e==null){
                    mCommentReplyPresenter.getRequestCommentReplyResult(true,list);
                }else {
                    mCommentReplyPresenter.getRequestCommentReplyResult(false,list);
                }
            }
        });
    }
}
