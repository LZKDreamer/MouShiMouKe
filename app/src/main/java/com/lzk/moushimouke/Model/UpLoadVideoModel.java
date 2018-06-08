package com.lzk.moushimouke.Model;

import android.graphics.Bitmap;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Presenter.EditVideoPresenter;
import com.lzk.moushimouke.View.Interface.IUploadVideoModel;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by huqun on 2018/5/4.
 */

public class UpLoadVideoModel implements IUploadVideoModel{
    private String thumbnailUri;
    @Override
    public void uploadVideo(final String description, List<String> list, File thumbnail) {
        final MyUser myUser= BmobUser.getCurrentUser(MyUser.class);
        final Post post=new Post();
        final String[] videoString=new String[list.size()];
        for (int i=0;i<list.size();i++){
            videoString[i]=list.get(i);
        }
        uploadVideoThumbnail(thumbnail);
        BmobFile.uploadBatch(videoString, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == videoString.length) {
                    EditVideoPresenter.showUpLoadVideoResult(true);
                }

                post.setDescription(description);
                post.setUser(myUser);
                post.setMedia(list);
                post.setThumbnail(thumbnailUri);
                post.setLike(0);
                post.setForward(0);
                post.setComment(0);
                post.setType(1);
                post.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e != null) {
                            EditVideoPresenter.showUpLoadVideoResult(false);
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                EditVideoPresenter.showUpLoadVideoResult(false);
            }
        });
    }

    private void uploadVideoThumbnail(File thumbnailFile){
        final BmobFile bmobFile=new BmobFile(thumbnailFile);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    thumbnailUri=bmobFile.getFileUrl();
                }else {
                    EditVideoPresenter.showUpLoadVideoResult(false);
                }
            }
        });
    }
}
