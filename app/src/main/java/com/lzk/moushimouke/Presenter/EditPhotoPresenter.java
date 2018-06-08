package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.UpLoadPhotoModel;
import com.lzk.moushimouke.View.Activity.EditPhotoActivity;

import java.util.List;

/**
 * Created by huqun on 2018/5/2.
 */

public class EditPhotoPresenter {
    private UpLoadPhotoModel mPhotoModel;
    public EditPhotoPresenter(){
        mPhotoModel=new UpLoadPhotoModel();
    }

    public void requestUpLoadPhoto(String description, List<String> photoList){
        mPhotoModel.upLoadPhoto(description,photoList);
    }

    public static void showUpLoadPhotoResult(boolean result){
        EditPhotoActivity.sEditPhotoActivity.showUploadPhotoResult(result);
    }
}
