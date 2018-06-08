package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.RepostModel;
import com.lzk.moushimouke.View.Activity.RepostActivity;

/**
 * Created by huqun on 2018/5/18.
 */

public class RepostPresenter {
    private RepostModel mRepostModel;

    public RepostPresenter(){
        mRepostModel=new RepostModel();
    }

    public void requestRepost(MyUser user,Post post,String description){
        mRepostModel.repost(user,post,description);
    }

    public static void showRepostResult(boolean result){
        RepostActivity.sRepostActivity.showRepostResult(result);
    }
}
