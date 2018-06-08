package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Comment;
import com.lzk.moushimouke.Model.Bean.InnerComment;

import java.util.List;

/**
 * Created by huqun on 2018/5/22.
 */

public interface ICommentPresenterCallBack {
    public abstract void getCommentSendResult(boolean result);
    public abstract void getCommentData(String postId);
    public abstract void getCommentDataResult(boolean result, List<Comment> commentList);
}
