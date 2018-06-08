package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.Comment;
import com.lzk.moushimouke.Model.Bean.InnerComment;

import java.util.List;

/**
 * Created by huqun on 2018/5/21.
 */

public interface ICommentActivityDataCallBack {
    public abstract String getActivityComment();
    public abstract void getDialogFragmentComment(String comment);
    public abstract void getRequestCommentResult(boolean result, List<Comment> commentList);
}
