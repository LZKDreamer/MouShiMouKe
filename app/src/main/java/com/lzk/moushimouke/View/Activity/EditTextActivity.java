package com.lzk.moushimouke.View.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.Model.Utils.PromptBackDialogUtils;
import com.lzk.moushimouke.Presenter.EditTextPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Fragment.LoadingDialogFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTextActivity extends AppCompatActivity {

    @BindView(R.id.edit_action_bar_back)
    ImageView mEditActionBarBack;
    @BindView(R.id.edit_action_bar_post)
    ImageView mEditActionBarPost;
    @BindView(R.id.edit_text_introduce)
    MaterialEditText mEditTextIntroduce;
    private EditTextPresenter mTextPresenter;
    private LoadingDialogFragment mLoadingDialogFragment;
    public static EditTextActivity sEditTextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        ButterKnife.bind(this);
        mTextPresenter=new EditTextPresenter();
        mLoadingDialogFragment=new LoadingDialogFragment();
        sEditTextActivity=this;
    }

    @OnClick({R.id.edit_action_bar_back, R.id.edit_action_bar_post})
    public void onViewClicked(View view) {
        String text=mEditTextIntroduce.getText().toString();
        switch (view.getId()) {
            case R.id.edit_action_bar_back:
                isBack();
                break;
            case R.id.edit_action_bar_post:
                FragmentManager manager=getSupportFragmentManager();
                mLoadingDialogFragment.setCancelable(false);
                mLoadingDialogFragment.show(manager,"");
                IsNetWorkConnectedUtils connectedUtils=new IsNetWorkConnectedUtils();
                boolean state=connectedUtils.IsNetWorkConnected(this);
                if (state){
                    if (!text.isEmpty()){
                        mTextPresenter.requestUploadText(text);
                    }else {
                        mLoadingDialogFragment.dismiss();
                        createSnackBar(mEditTextIntroduce,getResources().getString(R.string.data_not_empty_prompt));
                    }
                }else {
                    mLoadingDialogFragment.dismiss();
                    createSnackBar(mEditTextIntroduce,getResources().getString(R.string.network_error));
                }
                break;
        }
    }

    private void createSnackBar(View view,String prompt){
        Snackbar.make(view,prompt,Snackbar.LENGTH_LONG).show();
    }

    public  void showUploadTextResult(boolean result){
        if (result){
            mLoadingDialogFragment.dismiss();
            finish();
        }else {
            mLoadingDialogFragment.dismiss();
            createSnackBar(mEditTextIntroduce,getResources().getString(R.string.post_error));
        }
    }

    @Override
    public void onBackPressed() {
        isBack();
    }

    private void isBack(){
        String text=mEditTextIntroduce.getText().toString();
        if (!text.isEmpty()){
            PromptBackDialogUtils dialogUtils=new PromptBackDialogUtils();
            dialogUtils.showUnEditDialog(this);
        }else {
            finish();
        }
    }
}
