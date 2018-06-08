package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.Bean.Follow;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.MeFragmentModel;
import com.lzk.moushimouke.View.Interface.IMeFragmentCallBack;
import com.lzk.moushimouke.View.Interface.IMeFragmentPresenterCallBack;

import java.util.List;

/**
 * Created by huqun on 2018/5/31.
 */

public class MeFragmentPresenter implements IMeFragmentPresenterCallBack{
    private IMeFragmentCallBack mMeFragmentCallBack;
    private MeFragmentModel mMeFragmentModel;

    public void requestMeUserInfo(MyUser user,IMeFragmentCallBack meFragmentCallBack){
        mMeFragmentCallBack=meFragmentCallBack;
        mMeFragmentModel=new MeFragmentModel();
        mMeFragmentModel.requestUserInfo(user,this);

    }

    @Override
    public void getUserInfoResult(List<Post> postList, List<Follow> followList, List<Follow> followerList, boolean result,List<MyUser> userList) {
        mMeFragmentCallBack.getRequestUserInfoResult(postList,followList,followerList,result,userList);
    }
}
