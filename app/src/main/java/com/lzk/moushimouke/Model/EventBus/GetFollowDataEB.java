package com.lzk.moushimouke.Model.EventBus;

import com.lzk.moushimouke.Model.Bean.Follow;

import java.util.List;

/**
 * Created by huqun on 2018/5/15.
 */

public class GetFollowDataEB {
    private List<Follow> mFollowList;

    public GetFollowDataEB(List<Follow> mFollowList){
        this.mFollowList=mFollowList;
    }

    public List<Follow> getFollowList() {
        return mFollowList;
    }

    public void setFollowList(List<Follow> followList) {
        mFollowList = followList;
    }
}
