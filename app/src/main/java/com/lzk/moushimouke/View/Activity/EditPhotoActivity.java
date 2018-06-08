package com.lzk.moushimouke.View.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzk.moushimouke.Model.Adapter.EditItemAdapter;
import com.lzk.moushimouke.Model.Bean.EditPhotoItemBean;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.Model.Utils.PromptBackDialogUtils;
import com.lzk.moushimouke.Presenter.EditPhotoPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Fragment.LoadingDialogFragment;
import com.zrq.divider.Divider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPhotoActivity extends AppCompatActivity {


    @BindView(R.id.edit_action_bar_back)
    ImageView mEditActionBarBack;
    @BindView(R.id.edit_action_bar_post)
    ImageView mEditActionBarPost;
    @BindView(R.id.edit_image_introduce)
    EditText mEditImageIntroduce;
    @BindView(R.id.edit_recycler_view)
    RecyclerView mEditRecyclerView;
    private List<EditPhotoItemBean> mDataList;
    private EditItemAdapter mItemAdapter;
    public static EditPhotoActivity sEditPhotoActivity;
    private EditPhotoPresenter mPhotoPresenter;
    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        sEditPhotoActivity =this;
        mDataList=new ArrayList<>();
        initItemData();
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        mEditRecyclerView.setLayoutManager(layoutManager);
        mItemAdapter=new EditItemAdapter(mDataList);
        mEditRecyclerView.addItemDecoration(Divider.builder()
                .color(Color.WHITE)
                .width(10)
                .height(10)
                .build());
        mEditRecyclerView.setAdapter(mItemAdapter);
        mPhotoPresenter=new EditPhotoPresenter();
    }

    @OnClick({R.id.edit_action_bar_back,R.id.edit_action_bar_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_action_bar_back:
                isBack();
                break;
            case R.id.edit_action_bar_post:
                FragmentManager manager=getSupportFragmentManager();
                loadingDialogFragment=new LoadingDialogFragment();
                loadingDialogFragment.setCancelable(false);
                loadingDialogFragment.show(manager,"LOADING_DIALOG");
                IsNetWorkConnectedUtils netWorkConnectedUtils=new IsNetWorkConnectedUtils();
                boolean network=netWorkConnectedUtils.IsNetWorkConnected(EditPhotoActivity.this);
                if (network){
                    List<String> photoList=new ArrayList<>();
                    String photoDescription=mEditImageIntroduce.getText().toString();
                    if (!mDataList.isEmpty()){
                        for (EditPhotoItemBean data:mDataList){
                            photoList.add(data.getImagePath());
                        }
                        mPhotoPresenter.requestUpLoadPhoto(photoDescription,photoList);
                    }else {
                        createSnackBar(mEditImageIntroduce,getResources().getString(R.string.edit_media_null_prompt));
                    }
                }else {
                    createSnackBar(mEditImageIntroduce,getResources().getString(R.string.network_error));
                }
                break;
        }
    }

    private void initItemData(){
        mDataList.clear();
        if (getIntent().getStringArrayListExtra("photoPath")!=null){
            List<String> list=getIntent().getStringArrayListExtra("photoPath");
            for (int i=0;i<list.size();i++){
                EditPhotoItemBean data=new EditPhotoItemBean(list.get(i));
                mDataList.add(data);
            }
        }else if (getIntent().getStringExtra("cameraPath")!=null){
            String cameraPath=getIntent().getStringExtra("cameraPath");
            EditPhotoItemBean data=new EditPhotoItemBean(cameraPath);
            mDataList.add(data);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PictureConfig.CHOOSE_REQUEST:
                if (resultCode==RESULT_OK){
                    List<LocalMedia> photoList=PictureSelector.obtainMultipleResult(data);
                    if (photoList!=null){
                        for (LocalMedia localMedia:photoList){
                            EditPhotoItemBean data1=new EditPhotoItemBean(localMedia.getPath());
                            mDataList.add(data1);

                        }
                        mItemAdapter.notifyItemInserted(mDataList.size());
                        mItemAdapter.notifyDataSetChanged();
                    }
                }else {
                    return;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
       isBack();
    }



    public void showUploadPhotoResult(boolean result){
        if (result){
            finish();
            loadingDialogFragment.dismiss();
        }else {
            loadingDialogFragment.dismiss();
            createSnackBar(mEditImageIntroduce,getResources().getString(R.string.post_error));
        }
    }

    private void createSnackBar(View view,String prompt){
        Snackbar.make(view,prompt,Snackbar.LENGTH_LONG).show();
    }

    private void isBack(){
        String description=mEditImageIntroduce.getText().toString();
        if (!description.isEmpty()||mDataList.size()!=0){
            PromptBackDialogUtils backDialogUtils=new PromptBackDialogUtils();
            backDialogUtils.showUnEditDialog(EditPhotoActivity.this);
        }else {
            finish();
        }
    }
}
