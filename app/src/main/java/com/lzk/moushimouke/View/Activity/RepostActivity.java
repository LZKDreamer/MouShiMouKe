package com.lzk.moushimouke.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Bean.Post;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.Model.Utils.PromptBackDialogUtils;
import com.lzk.moushimouke.Presenter.RepostPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Fragment.LoadingDialogFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class RepostActivity extends AppCompatActivity {


    @BindView(R.id.edit_action_bar_back)
    ImageView mEditActionBarBack;
    @BindView(R.id.edit_action_bar_post)
    ImageView mEditActionBarPost;
    @BindView(R.id.repost_description)
    MaterialEditText mRepostDescription;
    @BindView(R.id.repost_post_image)
    ImageView mRepostPostImage;
    @BindView(R.id.repost_post_description)
    TextView mRepostPostDescription;
    private RepostPresenter mRepostPresenter;
    public static RepostActivity sRepostActivity;
    private LoadingDialogFragment loadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repost);
        ButterKnife.bind(this);
        mRepostPresenter=new RepostPresenter();
        sRepostActivity=this;
        Intent intent=getIntent();
        final int TAG=intent.getIntExtra("TAG",0);
        if (TAG==1){//图片
            String description=getIntent().getStringExtra("description");
            if (description!=null){
                mRepostPostDescription.setText(description);
            }
            String urlString=getIntent().getStringExtra("imageUrlString");
            Glide.with(this).load(urlString).thumbnail(0.5f).into(mRepostPostImage);
        }else if (TAG==2){//视频
            String description=getIntent().getStringExtra("description");
            if (description!=null){
                mRepostPostDescription.setText(description);
            }
            String thumbnailUriString=getIntent().getStringExtra("imageUrlString");
            Glide.with(this).load(thumbnailUriString).into(mRepostPostImage);
        }else if (TAG==3){
            String description=getIntent().getStringExtra("description");
            mRepostPostImage.setVisibility(View.GONE);
            if (description!=null){
                mRepostPostDescription.setText(description);
            }

        }
    }

    @OnClick({R.id.edit_action_bar_back, R.id.edit_action_bar_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_action_bar_back:
                PromptBackDialogUtils backDialogUtils=new PromptBackDialogUtils();
                backDialogUtils.showUnEditDialog(this);
                break;
            case R.id.edit_action_bar_post:
                IsNetWorkConnectedUtils connectedUtils=new IsNetWorkConnectedUtils();
                boolean netState=connectedUtils.IsNetWorkConnected(this);
                if (netState){
                    FragmentManager manager=getSupportFragmentManager();
                    loadingDialogFragment=new LoadingDialogFragment();
                    loadingDialogFragment.show(manager,"");
                    String text=mRepostDescription.getText().toString();
                    if (text.isEmpty()){
                        text=getResources().getString(R.string.repost_default_text);
                    }
                    Intent intent=getIntent();
                    Post post= (Post) intent.getSerializableExtra("post");
                    MyUser user= BmobUser.getCurrentUser(MyUser.class);
                    mRepostPresenter.requestRepost(user,post,text);
                    break;
                }else {
                    createSnackBar(mRepostDescription,getResources().getString(R.string.network_error));
                }


        }
    }

    public  void showRepostResult(boolean result){
        if (result){
            loadingDialogFragment.dismiss();
            Toast.makeText(this,getString(R.string.repost_success),Toast.LENGTH_SHORT).show();
            finish();
        }else {
            loadingDialogFragment.dismiss();
            Toast.makeText(this,getString(R.string.repost_error),Toast.LENGTH_SHORT).show();
            createSnackBar(mRepostDescription,getResources().getString(R.string.post_error));
        }
    }

    private void createSnackBar(View view,String prompt){
        Snackbar.make(view,prompt,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        PromptBackDialogUtils backDialogUtils=new PromptBackDialogUtils();
        backDialogUtils.showUnEditDialog(this);
    }
}
