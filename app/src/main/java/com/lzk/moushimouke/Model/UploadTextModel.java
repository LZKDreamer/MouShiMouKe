package com.lzk.moushimouke.Model;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.EditTextPresenter;
import com.lzk.moushimouke.View.Interface.IUploadTextModel;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by huqun on 2018/5/14.
 */

public class UploadTextModel implements IUploadTextModel{
    @Override
    public void requestUploadText(String text) {
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        Post post=new Post();
        post.setUser(user);
        post.setDescription(text);
        post.setForward(0);
        post.setLike(0);
        post.setComment(0);
        post.setType(1);
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    EditTextPresenter.showUploadTextResult(true);
                }else {
                    EditTextPresenter.showUploadTextResult(false);
                }
            }
        });
    }
}
