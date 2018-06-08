package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.Bean.Comment;
import com.lzk.moushimouke.Model.Bean.InnerComment;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.CommentModel;
import com.lzk.moushimouke.View.Activity.CommentActivity;
import com.lzk.moushimouke.View.Fragment.CommentEditDialogFragment;
import com.lzk.moushimouke.View.Interface.ICommentActivityDataCallBack;
import com.lzk.moushimouke.View.Interface.ICommentPresenterCallBack;
import com.lzk.moushimouke.View.Interface.ICommentSendResultCallBack;

import java.util.List;

/**
 * Created by huqun on 2018/5/22.
 */

public class CommentPresenter implements ICommentPresenterCallBack{
    private CommentModel mCommentModel;
    private ICommentSendResultCallBack mCallBack;
    public CommentPresenter(ICommentSendResultCallBack callBack){
        mCommentModel=new CommentModel();
        mCallBack=callBack;
    }

    public CommentPresenter(){
        mCallBack= CommentEditDialogFragment.sCommentEditDialogFragment;
    }

    public void requestSendComment(String comment, String postId, MyUser user,MyUser postUser){
        mCommentModel.sendComment(comment,postId,user,postUser);
    }

    @Override
    public void getCommentSendResult(boolean result) {
        mCallBack.getCommentSendResult(result);
    }

    @Override
    public void getCommentData(String postId) {
        mCommentModel=new CommentModel();
        mCommentModel.requestComment(postId);
    }

    @Override
    public void getCommentDataResult(boolean result, List<Comment> commentList) {
        ICommentActivityDataCallBack iCommentActivityDataCallBack=CommentActivity.sCommentActivity;
        iCommentActivityDataCallBack.getRequestCommentResult(result,commentList);
    }

    public void requestUpdateCommentLikeNum(String commentId,int tag){
        mCommentModel=new CommentModel();
        mCommentModel.sendUpdateCommentLikeNum(commentId,tag);
    }
}
