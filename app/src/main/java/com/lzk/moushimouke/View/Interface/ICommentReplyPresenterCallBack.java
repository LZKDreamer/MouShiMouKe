package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.InnerComment;

import java.util.List;

/**
 * Created by huqun on 2018/5/24.
 */

public interface ICommentReplyPresenterCallBack {
    public abstract void getCommentReplySendResult(boolean result);
    public abstract void getRequestCommentReplyResult(boolean result,List<InnerComment> innerComments);
}
