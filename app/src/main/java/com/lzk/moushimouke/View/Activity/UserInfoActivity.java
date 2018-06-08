package com.lzk.moushimouke.View.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzk.moushimouke.Presenter.UserInfoPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IUserInfoActivityCallBack;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity implements IUserInfoActivityCallBack{

    @BindView(R.id.user_info_toolbar_back)
    ImageView mUserInfoToolbarBack;
    @BindView(R.id.user_info_toolbar)
    Toolbar mUserInfoToolbar;
    @BindView(R.id.user_info_portrait)
    CircleImageView mUserInfoPortrait;
    @BindView(R.id.user_info_portrait_ll)
    FrameLayout mUserInfoPortraitLl;
    @BindView(R.id.user_info_nickname_title)
    TextView mUserInfoNicknameTitle;
    @BindView(R.id.user_info_nickname)
    TextView mUserInfoNickname;
    @BindView(R.id.user_info_nickname_ll)
    LinearLayout mUserInfoNicknameLl;
    @BindView(R.id.user_info_profile_title)
    TextView mUserInfoProfileTitle;
    @BindView(R.id.user_info_profile)
    TextView mUserInfoProfile;
    @BindView(R.id.user_info_profile_ll)
    LinearLayout mUserInfoProfileLl;
    @BindView(R.id.user_info_change_password)
    TextView mUserInfoChangePassword;
    @BindView(R.id.user_info_logout)
    TextView mUserInfoLogout;

    private UserInfoPresenter mPresenter;
    private String finalNickName,finalProfile,cropUriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        String portraitUrl= (String) BmobUser.getObjectByKey("portrait");
        if (portraitUrl!=null&&!portraitUrl.isEmpty()){
            Glide.with(this).load(portraitUrl).thumbnail(0.5f).into(mUserInfoPortrait);
        }else {
            Glide.with(this).load(R.drawable.ic_user_portrait_default).thumbnail(0.5f).into(mUserInfoPortrait);
        }

        String username= (String) BmobUser.getObjectByKey("username");
        if (username!=null&&!username.isEmpty()){
            mUserInfoNickname.setText(username);
        }

        String profile= (String) BmobUser.getObjectByKey("profile");
        if (profile!=null&&!profile.isEmpty()){
            mUserInfoProfile.setText(profile);
        }else {
            mUserInfoProfile.setHint(getString(R.string.introduce_yourself));

        }

        mPresenter=new UserInfoPresenter();
    }

    @OnClick({R.id.user_info_toolbar_back, R.id.user_info_portrait_ll, R.id.user_info_nickname_ll, R.id.user_info_profile_ll, R.id.user_info_change_password, R.id.user_info_logout})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.user_info_toolbar_back:
                finish();
                break;
            case R.id.user_info_portrait_ll:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .imageSpanCount(3)
                        .selectionMode(PictureConfig.SINGLE)
                        .previewImage(true)
                        .isCamera(true)
                        .sizeMultiplier(0.5f)
                        .isGif(false)
                        .previewEggs(true)
                        .enableCrop(true)
                        .withAspectRatio(1,1)
                        .freeStyleCropEnabled(true)
                        .circleDimmedLayer(true)
                        .showCropFrame(false)
                        .showCropGrid(false)
                        .rotateEnabled(true)
                        .scaleEnabled(true)
                        .isDragFrame(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.user_info_nickname_ll:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                final AlertDialog dialog=builder.create();
                View dialogView=View.inflate(this,R.layout.activity_user_info_edit_dialog,null);
                dialog.setView(dialogView);
                dialog.show();
                final EditText editText=dialogView.findViewById(R.id.user_info_edit);
                TextView titleView=dialogView.findViewById(R.id.user_info_edit_title);
                TextView cancelText=dialogView.findViewById(R.id.user_info_edit_cancel);
                TextView saveText=dialogView.findViewById(R.id.user_info_edit_sure);
                String title=getResources().getString(R.string.user_info_edit_title);
                String result=String.format(title,mUserInfoNicknameTitle.getText().toString());
                titleView.setText(result);
                editText.setText(mUserInfoNickname.getText().toString());
                cancelText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                saveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nickName=editText.getText().toString();
                        if (!nickName.isEmpty()){
                            finalNickName=nickName;
                            mPresenter.requestUpdateNickName(nickName,UserInfoActivity.this);
                            dialog.dismiss();
                        }else {
                            Snackbar.make(mUserInfoNicknameLl,getString(R.string.data_not_empty_prompt),Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
            case R.id.user_info_profile_ll:
                AlertDialog.Builder builder1=new AlertDialog.Builder(this);
                final AlertDialog dialog1=builder1.create();
                View dialogView1=View.inflate(this,R.layout.activity_user_info_edit_dialog,null);
                dialog1.setView(dialogView1);
                dialog1.show();
                final EditText editText1=dialogView1.findViewById(R.id.user_info_edit);
                TextView titleView1=dialogView1.findViewById(R.id.user_info_edit_title);
                TextView cancelText1=dialogView1.findViewById(R.id.user_info_edit_cancel);
                TextView saveText1=dialogView1.findViewById(R.id.user_info_edit_sure);
                String title1=getResources().getString(R.string.user_info_edit_title);
                String profileResult=String.format(title1,mUserInfoProfileTitle.getText().toString());
                titleView1.setText(profileResult);
                editText1.setText(mUserInfoProfile.getText().toString());
                cancelText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

                saveText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String profile=editText1.getText().toString();
                        if (!profile.isEmpty()){
                            finalProfile=profile;
                            mPresenter.requestUpdateProfile(profile,UserInfoActivity.this);
                            dialog1.dismiss();
                        }else {
                            Snackbar.make(mUserInfoNicknameLl,getString(R.string.data_not_empty_prompt),Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });


                break;
            case R.id.user_info_change_password:
                Intent intent=new Intent(this,ResetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.user_info_logout:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.prompt))
                        .setMessage(getString(R.string.are_you_sure_logout))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BmobUser.logOut();
                                Intent loginIntent=new Intent(UserInfoActivity.this,LoginActivity.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginIntent);
                                finish();
                            }
                        }).create().show();
                break;
        }
    }

    @Override
    public void getUpdateNickNameResult(boolean result) {
        if (result){
            mUserInfoNickname.setText(finalNickName);
            Snackbar.make(mUserInfoNicknameLl,getString(R.string.modify_success),Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(mUserInfoNicknameLl,getString(R.string.modify_error),Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getUpdateProfileResult(boolean result) {
        if (result){
            mUserInfoProfile.setText(finalProfile);
            Snackbar.make(mUserInfoNicknameLl,getString(R.string.modify_success),Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(mUserInfoNicknameLl,getString(R.string.modify_error),Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getUpdatePortraitResult(boolean result) {
        if (result){
            Glide.with(this).load(cropUriString).thumbnail(0.5f).into(mUserInfoPortrait);
            Toast.makeText(this,getString(R.string.modify_success),Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,getString(R.string.modify_error),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> mediaList=PictureSelector.obtainMultipleResult(data);
                    LocalMedia media=mediaList.get(0);
                    if (media.isCut()){
                        cropUriString=media.getCutPath();
                        mPresenter.requestUpdatePortrait(cropUriString,UserInfoActivity.this);
                    }
                    break;
            }
        }

    }
}
