package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.Bean.InnerComment;
import com.lzk.moushimouke.Model.CommentReplyModel;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.View.Activity.CommentReplyActivity;
import com.lzk.moushimouke.View.Fragment.CommentReplyDialogFragment;
import com.lzk.moushimouke.View.Interface.ICommentReplyDataCallback;
import com.lzk.moushimouke.View.Interface.ICommentReplyPresenterCallBack;
import com.lzk.moushimouke.View.Interface.ICommentReplyResultCallBack;

import java.util.List;

/**
 * Created by huqun on 2018/5/24.
 */

public class CommentReplyPresenter implements ICommentReplyPresenterCallBack {
    private CommentReplyModel mCommentReplyModel;
    private ICommentReplyDataCallback mReplyDataCallback;
    private ICommentReplyResultCallBack mReplyResultCallBack;

    /*提交回复*/
    public CommentReplyPresenter(ICommentReplyResultCallBack mReplyResultCallBack){
        this.mReplyResultCallBack=mReplyResultCallBack;
        mCommentReplyModel=new CommentReplyModel();
    }

    /*Model返回回复结果*/
    public CommentReplyPresenter(){
       mReplyResultCallBack= CommentReplyDialogFragment.sCommentReplyDialogFragment;
       mReplyDataCallback= CommentReplyActivity.sCommentReplyActivity;
    }


    public void requestSendCommentReply(String commentId, MyUser user,String content,MyUser commentUser){
        mCommentReplyModel.sendCommentReply(commentId,user,content,commentUser);
    }


    @Override
    public void getCommentReplySendResult(boolean result) {
        mReplyResultCallBack.getCommentReplyResult(result);
    }

    @Override
    public void getRequestCommentReplyResult(boolean result,List<InnerComment> innerComments) {
        mReplyDataCallback.getCommentReplySendResult(result,innerComments);
    }

    public void requestCommentReplyData(String commentId){
        mCommentReplyModel.requestCommentReplyData(commentId);
    }
}
