package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.UpLoadVideoModel;
import com.lzk.moushimouke.View.Activity.EditVideoActivity;

import java.io.File;
import java.util.List;

/**
 * Created by huqun on 2018/5/4.
 */

public class EditVideoPresenter {
    private UpLoadVideoModel mVideoModel;

    public EditVideoPresenter(){
        mVideoModel=new UpLoadVideoModel();
    }

    public void requestUpLoadVideo(String description, List<String> videoList,File thumbnail){
        mVideoModel.uploadVideo(description,videoList,thumbnail);
    }

    public static void showUpLoadVideoResult(boolean result){
        EditVideoActivity.sVideoActivity.showUploadVideoResult(result);
    }
}
