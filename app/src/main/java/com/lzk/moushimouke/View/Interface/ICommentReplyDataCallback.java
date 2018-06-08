package com.lzk.moushimouke.View.Interface;

import com.lzk.moushimouke.Model.Bean.InnerComment;

import java.util.List;

/**
 * Created by huqun on 2018/5/24.
 */

public interface ICommentReplyDataCallback {
    public abstract String getCommentReplyActivityText();
    public abstract void getCommentReplyDialogText(String replyText);
    public abstract void getCommentReplySendResult(boolean result, List<InnerComment> innerComments);
}
