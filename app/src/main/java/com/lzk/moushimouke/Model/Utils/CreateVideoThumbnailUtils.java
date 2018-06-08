package com.lzk.moushimouke.Model.Utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.lzk.moushimouke.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by huqun on 2018/5/14.
 */

public class CreateVideoThumbnailUtils {
    public File createVideoThumbnail(String uri){
        FileOutputStream outputStream;
        Bitmap bitmap=ThumbnailUtils.createVideoThumbnail(uri, MediaStore.Video.Thumbnails.MINI_KIND);
        File file=new File(MyApplication.getContext().getCacheDir(),"thumbnail.jpeg");
        if (file.exists()){
            file.delete();
        }
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
