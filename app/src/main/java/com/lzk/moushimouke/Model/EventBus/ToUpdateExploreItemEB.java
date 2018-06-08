package com.lzk.moushimouke.Model.EventBus;

/**
 * Created by huqun on 2018/6/7.
 */

public class ToUpdateExploreItemEB {
    private int position;

    public ToUpdateExploreItemEB(int position){
        this.position=position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
