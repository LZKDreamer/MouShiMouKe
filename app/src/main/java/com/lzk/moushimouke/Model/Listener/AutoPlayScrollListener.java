package com.lzk.moushimouke.Model.Listener;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.R;

import cn.jzvd.JZVideoPlayerStandard;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by huqun on 2018/5/12.
 */

public class AutoPlayScrollListener extends RecyclerView.OnScrollListener {
    private int firstVisibleItem=0;
    private int lastVisibleItem=0;
    private int visibleCount=0;
    private final int AUTO_PLAY_VIDEO=1;
    private final int PAUSE_VIDEO=2;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState){
            case SCROLL_STATE_IDLE:
                IsNetWorkConnectedUtils connectedUtils=new IsNetWorkConnectedUtils();
                if (connectedUtils.isWifiConnected(MyApplication.getContext())){
                    autoPlayVideo(recyclerView,AUTO_PLAY_VIDEO);
                }else {
                    break;
                }
                break;
               /* default:
                    autoPlayVideo(recyclerView,PAUSE_VIDEO);
                    break;*/
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager manager= (LinearLayoutManager) layoutManager;
            firstVisibleItem=manager.findFirstVisibleItemPosition();
            lastVisibleItem=manager.findLastVisibleItemPosition();
            visibleCount=lastVisibleItem-firstVisibleItem;
        }
    }

    private void autoPlayVideo(RecyclerView recyclerView, int videoState){
        for (int i=0;i<visibleCount;i++){
            if (recyclerView != null && recyclerView.getChildAt(i) != null
                    && recyclerView.getChildAt(i).findViewById(R.id.explore_video) != null
                    &&recyclerView.getChildAt(i).findViewById(R.id.explore_video).getVisibility()== View.VISIBLE) {
                JZVideoPlayerStandard videoPlayerStandard=recyclerView.getChildAt(i).findViewById(R.id.explore_video);
                Rect rect=new Rect();
                videoPlayerStandard.getLocalVisibleRect(rect);
                int videoHeight=videoPlayerStandard.getHeight();
                if (rect.top==0&&rect.bottom==videoHeight){
                    handleVideo(videoState,videoPlayerStandard);
                    break;
                }
            }
        }
    }

    private void handleVideo(int videoState,JZVideoPlayerStandard jzVideoPlayerStandard){
        switch (videoState){
            case AUTO_PLAY_VIDEO:
                if (jzVideoPlayerStandard.currentState!=JZVideoPlayerStandard.CURRENT_STATE_PLAYING&&jzVideoPlayerStandard.currentState!=JZVideoPlayerStandard.CURRENT_STATE_PREPARING){
                    jzVideoPlayerStandard.startVideo();
                }
                break;
            case PAUSE_VIDEO:
                if (jzVideoPlayerStandard.currentState!=JZVideoPlayerStandard.CURRENT_STATE_PAUSE){
                    jzVideoPlayerStandard.startButton.performClick();
                }
                break;
            default:
                break;
        }
    }
}
