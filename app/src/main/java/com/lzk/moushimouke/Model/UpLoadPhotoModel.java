package com.lzk.moushimouke.Model;

import android.os.Looper;
import android.util.Log;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.EditPhotoPresenter;
import com.lzk.moushimouke.View.Interface.IUploadPhotoModel;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by huqun on 2018/5/2.
 */

public class UpLoadPhotoModel implements IUploadPhotoModel {

    @Override
    public void upLoadPhoto(final String description, List<String> photoList) {
        final MyUser user= BmobUser.getCurrentUser(MyUser.class);
        final Post post=new Post();
        final String[] photoPath=new String[photoList.size()];
        for (int i=0;i<photoList.size();i++){
            photoPath[i]=photoList.get(i);
        }

        BmobFile.uploadBatch(photoPath, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size()==photoPath.length){
                    EditPhotoPresenter.showUpLoadPhotoResult(true);

                    post.setDescription(description);
                    post.setUser(user);
                    post.setMedia(list);
                    post.setLike(0);
                    post.setForward(0);
                    post.setComment(0);
                    post.setType(1);
                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                EditPhotoPresenter.showUpLoadPhotoResult(true);
                            }else {
                                EditPhotoPresenter.showUpLoadPhotoResult(false);
                            }
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                EditPhotoPresenter.showUpLoadPhotoResult(false);
            }
        });

    }
}
