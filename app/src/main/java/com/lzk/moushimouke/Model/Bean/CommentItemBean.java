package com.lzk.moushimouke.Model.Bean;

import java.util.List;

/**
 * Created by huqun on 2018/5/22.
 */

public class CommentItemBean {
    private List<Comment> mComments;

    public CommentItemBean(List<Comment> commentList){
        mComments=commentList;
    }

    public List<Comment> getComments() {
        return mComments;
    }
}
