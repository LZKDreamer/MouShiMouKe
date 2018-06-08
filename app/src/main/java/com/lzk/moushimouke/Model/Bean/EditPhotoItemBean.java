package com.lzk.moushimouke.Model.Bean;

import java.io.Serializable;

/**
 * Created by huqun on 2018/4/27.
 */

public class EditPhotoItemBean implements Serializable{
    private String imagePath;

    public EditPhotoItemBean(String imagePath){
        this.imagePath=imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

}
