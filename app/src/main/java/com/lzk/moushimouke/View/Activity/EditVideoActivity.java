package com.lzk.moushimouke.View.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzk.moushimouke.Model.Utils.CreateVideoThumbnailUtils;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.Model.Utils.PromptBackDialogUtils;
import com.lzk.moushimouke.Presenter.EditVideoPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Fragment.LoadingDialogFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;

public class EditVideoActivity extends AppCompatActivity {
    @BindView(R.id.edit_action_bar_back)
    ImageView mEditActionBarBack;
    @BindView(R.id.edit_action_bar_post)
    ImageView mEditActionBarPost;
    @BindView(R.id.edit_video_introduce)
    MaterialEditText mEditVideoIntroduce;
    @BindView(R.id.edit_video)
    JZVideoPlayerStandard mEditVideo;
    @BindView(R.id.edit_video_add)
    ImageView mEditVideoAdd;
    @BindView(R.id.edit_video_delete)
    ImageView mEditVideoDelete;
    private String videoPath;
    public static final int CHOOSE_VIDEO=1;
    private EditVideoPresenter mVideoPresenter;
    private LoadingDialogFragment mLoadingDialogFragment;
    public static EditVideoActivity sVideoActivity;
    private File thumbnailFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        ButterKnife.bind(this);
        videoPath = getIntent().getStringExtra("videoPath");
        setVideo();
        sVideoActivity=this;
        mVideoPresenter=new EditVideoPresenter();
    }

    @OnClick({R.id.edit_action_bar_back, R.id.edit_action_bar_post, R.id.edit_video_add, R.id.edit_video_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_action_bar_back:
                isBack();
                break;
            case R.id.edit_action_bar_post:
                IsNetWorkConnectedUtils connectedUtils = new IsNetWorkConnectedUtils();
                boolean network = connectedUtils.IsNetWorkConnected(this);
                if (network) {
                    List<String> dataList = new ArrayList<>();
                    String videoDescription = mEditVideoIntroduce.getText().toString();
                    if (!videoPath.isEmpty()) {
                        dataList.add(videoPath);
                            CreateVideoThumbnailUtils thumbnailUtils=new CreateVideoThumbnailUtils();
                            File thumbnailFile=thumbnailUtils.createVideoThumbnail(videoPath);
                            mVideoPresenter.requestUpLoadVideo(videoDescription, dataList,thumbnailFile);
                            FragmentManager manager = getSupportFragmentManager();
                            mLoadingDialogFragment = new LoadingDialogFragment();
                            mLoadingDialogFragment.setCancelable(false);
                            mLoadingDialogFragment.show(manager, "");
                        } else {
                            createSnackBar(mEditVideo, getResources().getString(R.string.edit_media_null_prompt));
                        }
                    } else {
                        createSnackBar(mEditVideo, getResources().getString(R.string.network_error));
                    }
                    break;
                    case R.id.edit_video_add:
                        PictureSelector.create(this)
                                .openGallery(PictureMimeType.ofVideo())
                                .maxSelectNum(1)
                                .imageSpanCount(2)
                                .selectionMode(PictureConfig.SINGLE)
                                .previewVideo(true)
                                .isCamera(false)
                                .sizeMultiplier(0.5f)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.edit_video_delete:
                        mEditVideo.setVisibility(View.GONE);
                        mEditVideoDelete.setVisibility(View.GONE);
                        mEditVideo.release();
                        videoPath = "";
                        break;
                }
        }


    private void setVideo(){
            mEditVideo.setUp(videoPath, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, CHOOSE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PictureConfig.CHOOSE_REQUEST){
            if (resultCode==RESULT_OK&&data!=null) {
                List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);
                for (LocalMedia media : mediaList) {
                    videoPath = media.getPath();
                }
                setVideo();
                mEditVideo.setVisibility(View.VISIBLE);
                mEditVideoDelete.setVisibility(View.VISIBLE);
            }else {
                mEditVideoDelete.setVisibility(View.GONE);
                mEditVideo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoPath!=null){
            setVideo();
        }else {
            mEditVideoDelete.setVisibility(View.GONE);
            mEditVideo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
       isBack();
    }

    public  void showUploadVideoResult(boolean result){
        if (result){
            mLoadingDialogFragment.dismiss();
            finish();
        }else {
            mLoadingDialogFragment.dismiss();
            createSnackBar(mEditVideo,getResources().getString(R.string.post_error));
        }
    }

    private void createSnackBar(View view,String prompt){
        Snackbar.make(view,prompt,Snackbar.LENGTH_LONG).show();
    }


    private void isBack(){
        String description = mEditVideoIntroduce.getText().toString();
        if (!description.isEmpty() || !videoPath.isEmpty()) {
            PromptBackDialogUtils backDialogUtils = new PromptBackDialogUtils();
            backDialogUtils.showUnEditDialog(EditVideoActivity.this);
        }else {
            finish();
        }
    }
}
