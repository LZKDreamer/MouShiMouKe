package com.lzk.moushimouke.Model;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.Bean.Repost;
import com.lzk.moushimouke.Presenter.RepostPresenter;
import com.lzk.moushimouke.View.Interface.IRepostModel;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by huqun on 2018/5/18.
 */

public class RepostModel implements IRepostModel {
    @Override
    public void repost(MyUser user, final Post post, String description) {
        Post newPost=new Post();
        newPost.setUser(user);//设置post的user为转发的人
        newPost.setMedia(post.getMedia());
        newPost.setThumbnail(post.getThumbnail());
        newPost.setDescription(post.getDescription());
        newPost.setLike(0);
        newPost.setForward(0);
        newPost.setComment(0);
        newPost.setOldUser(post.getUser());//设置转发的post的原主人
        newPost.setRepostText(description);
        newPost.setType(2);//2表示转发
        newPost.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Post oldPost=new Post();
                    oldPost.setObjectId(post.getObjectId());
                    oldPost.increment("forward",1);
                    oldPost.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            return;
                        }
                    });
                    RepostPresenter.showRepostResult(true);
                }else {
                    RepostPresenter.showRepostResult(false);
                }
            }
        });

    }
}
