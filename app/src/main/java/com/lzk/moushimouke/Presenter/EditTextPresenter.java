package com.lzk.moushimouke.Presenter;

import com.lzk.moushimouke.Model.UploadTextModel;
import com.lzk.moushimouke.View.Activity.EditTextActivity;

/**
 * Created by huqun on 2018/5/14.
 */

public class EditTextPresenter {
    private UploadTextModel mUploadTextModel;

    public EditTextPresenter(){
        mUploadTextModel=new UploadTextModel();
    }

    public void requestUploadText(String text){
        mUploadTextModel.requestUploadText(text);
    }

    public static void showUploadTextResult(boolean result){
        EditTextActivity.sEditTextActivity.showUploadTextResult(result);
    }
}
