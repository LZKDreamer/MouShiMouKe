package com.lzk.moushimouke.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzk.moushimouke.Model.Utils.RememberAccountUtils;
import com.lzk.moushimouke.Presenter.ResetPasswordPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IResetPassword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResetPasswordActivity extends AppCompatActivity implements IResetPassword {
    private boolean checkNumResult;
    public static ResetPasswordActivity sPasswordActivity;
    private ResetPasswordPresenter mResetPasswordPresenter;
    private String phoneNumber,code,password,passwordAgain,codeSendAgain;
    private List<String> allDataList;
    private  Handler phoneHandler;
    private static final int PHONE_RESULT=1;

    @BindView(R.id.reset_phone_number)
    EditText mResetPhoneNumber;
    @BindView(R.id.reset_verification_code)
    EditText mResetVerificationCode;
    @BindView(R.id.reset_get_verification_code)
    Button mResetGetVerificationCode;
    @BindView(R.id.reset_password)
    EditText mResetPassword;
    @BindView(R.id.reset_password_again)
    EditText mResetPasswordAgain;
    @BindView(R.id.reset_reset)
    Button mResetReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        sPasswordActivity=this;
        mResetPasswordPresenter=new ResetPasswordPresenter();
        codeSendAgain=getResources().getString(R.string.code_send_again);

        mResetPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mResetPhoneNumber.length()==11){
                    mResetGetVerificationCode.setVisibility(View.VISIBLE);
                }else {
                    mResetGetVerificationCode.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void showCheckPhoneNumResult(boolean result) {
        if (result){
            checkNumResult=true;
        }else {
            checkNumResult=false;
        }
        Message phoneMessage=new Message();//当判断手机号已注册之后发送消息给Handler,再执行后面的语句。
        phoneMessage.what=PHONE_RESULT;
        phoneHandler.sendMessage(phoneMessage);
    }

    @Override
    public void showResetPasswordResult(boolean result) {
        if (result){
            RememberAccountUtils accountUtils=new RememberAccountUtils();
            accountUtils.rememberAccountPreferences(this,phoneNumber,password);
            Intent loginIntent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }else {
            createSnackBar(mResetPassword,getResources().getString(R.string.error_reset_password_prompt));
        }
    }

    @Override
    public void showSendVerificationCodeResult(boolean result) {
        if (result){
            createSnackBar(mResetPassword,getResources().getString(R.string.get_code_true_prompt));
        }else {
            createSnackBar(mResetPassword,getResources().getString(R.string.get_code_error_prompt));
        }
        CountDownTimer countDownTimer=new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long l) {
                String value=String.valueOf((int)l/1000);
                mResetGetVerificationCode.setEnabled(false);
                mResetGetVerificationCode.setText(value+"s");
            }

            @Override
            public void onFinish() {
                mResetGetVerificationCode.setEnabled(true);
                mResetGetVerificationCode.setText(ResetPasswordActivity.this.codeSendAgain);
            }
        };
        countDownTimer.start();
    }

    @OnClick({R.id.reset_get_verification_code, R.id.reset_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset_get_verification_code:
                phoneNumber=mResetPhoneNumber.getText().toString();
                mResetPasswordPresenter.requestGetVerifyCode(phoneNumber);
                break;
            case R.id.reset_reset:
                phoneNumber=mResetPhoneNumber.getText().toString();
                code=mResetVerificationCode.getText().toString();
                password=mResetPassword.getText().toString();
                passwordAgain=mResetPasswordAgain.getText().toString();
                mResetPasswordPresenter.requestPhoneNumVerified(phoneNumber);
                /*请求判断手机号是在子线程中执行，而我们需要当子线程执行完之后再执行后面的语句，所以将后面的语句
                * 放在Handler中，收到消息之后再执行。*/
                phoneHandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case PHONE_RESULT:
                                if (checkDataEmpty()){
                                    createSnackBar(mResetPassword,getResources().getString(R.string.data_not_empty_prompt));
                                }else if(phoneNumber.length()!=11) {
                                    createSnackBar(mResetPassword,getResources().getString(R.string.phone_number_error));
                                }else if (checkNumResult==false){
                                    createSnackBar(mResetPassword,getResources().getString(R.string.phone_number_not_verified));
                                }else if (!password.equals(passwordAgain)){
                                    createSnackBar(mResetPassword,getResources().getString(R.string.error_two_password_prompt));
                                }else {
                                    mResetReset.setText(getResources().getString(R.string.committing));
                                    mResetPasswordPresenter.commitResetPassword(code,password);

                                }
                                break;
                        }
                    }
                };
                break;
        }
    }

    private void createSnackBar(View view,String prompt){
        Snackbar.make(view,prompt,Snackbar.LENGTH_LONG).show();
    }

    private boolean checkDataEmpty(){
        allDataList=new ArrayList<>();
        allDataList.clear();
        allDataList.add(phoneNumber);
        allDataList.add(code);
        allDataList.add(password);
        allDataList.add(passwordAgain);
        if (allDataList.contains("")){
            return true;
        }else {
            return false;
        }
    }
}
