package com.lzk.moushimouke.View.Interface;

import android.graphics.Bitmap;

import java.io.File;
import java.util.List;

/**
 * Created by huqun on 2018/5/4.
 */

public interface IUploadVideoModel {
    public abstract void uploadVideo(String description, List<String> list,File thumbnail);
}
