package com.lzk.moushimouke.View.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzk.moushimouke.Model.Utils.RememberAccountUtils;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.RegisterPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.IRegister;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements IRegister {
    @BindView(R.id.register_nickname)
    EditText mRegisterNickname;
    @BindView(R.id.register_phone_number)
    EditText mRegisterPhoneNumber;
    @BindView(R.id.register_password)
    EditText mRegisterPassword;
    @BindView(R.id.register_password_again)
    EditText mRegisterPasswordAgain;
    @BindView(R.id.register_verification_code)
    EditText mRegisterVerificationCode;
    @BindView(R.id.register_get_verification_code)
    Button mRegisterGetVerificationCode;
    @BindView(R.id.register_register)
    Button mRegisterRegister;
    private RegisterPresenter mRegisterPresenter;
    public static RegisterActivity sRegisterActivity = null;
    private String userName,phoneNumber,code,passWord,passwordAgain,codeSendAgain;
    private List<String> dataList;
    private boolean checkUserName,checkPhoneNum;
    private Handler mHandler;
    private static final int USERNAME=1;
    private static final int PHONE_RESULT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        sRegisterActivity = this;
        mRegisterPresenter = new RegisterPresenter();
        codeSendAgain=getResources().getString(R.string.code_send_again);
        dataList=new ArrayList<>();

        mRegisterPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mRegisterPhoneNumber.length()==11){
                    mRegisterGetVerificationCode.setVisibility(View.VISIBLE);
                }else {
                    mRegisterGetVerificationCode.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.register_get_verification_code, R.id.register_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_get_verification_code:
                phoneNumber=mRegisterPhoneNumber.getText().toString();
                mRegisterPresenter.requestVerificationCode(phoneNumber);
                break;
            case R.id.register_register:
                userName=mRegisterNickname.getText().toString();
                phoneNumber=mRegisterPhoneNumber.getText().toString();
                code=mRegisterVerificationCode.getText().toString();
                passWord=mRegisterPassword.getText().toString();
                passwordAgain=mRegisterPasswordAgain.getText().toString();
                mRegisterPresenter.checkUserNameExist(userName);

                mHandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case USERNAME:
                                mRegisterPresenter.checkPhoneNumResult(phoneNumber);
                                break;
                            case PHONE_RESULT:
                                if (checkAllDataNotEmpty()){
                                    createSnackBar(mRegisterPasswordAgain,getResources().getString(R.string.data_not_empty_prompt));
                                } else if (checkUserName){
                                    createSnackBar(mRegisterPasswordAgain,getResources().getString(R.string.userName_error_prompt));
                                } else if (checkPhoneNum){
                                    createSnackBar(mRegisterGetVerificationCode,getResources().getString(R.string.phone_number_is_verified));
                                } else if (!passWord.equals(passwordAgain)){
                                    createSnackBar(mRegisterPasswordAgain,getResources().getString(R.string.error_two_password_prompt));
                                }else {
                                    mRegisterRegister.setText(getResources().getString(R.string.registering));
                                    mRegisterPresenter.requestRegister(userName,phoneNumber,code,passWord);
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

    @Override
    public void showUserNameResult(boolean result) {
        if (result){
            checkUserName=true;
        }else {
            checkUserName=false;
        }
        Message userMessage=new Message();
        userMessage.what=USERNAME;
        mHandler.sendMessage(userMessage);

    }

    @Override
    public void showSendVerificationResult(boolean result) {
        if (result){
            createSnackBar(mRegisterGetVerificationCode,getResources().getString(R.string.get_code_true_prompt));
        }else {
            createSnackBar(mRegisterGetVerificationCode,getResources().getString(R.string.get_code_error_prompt));
        }
        CountDownTimer countDownTimer=new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long l) {
                String value=String.valueOf((int)l/1000);
                mRegisterGetVerificationCode.setEnabled(false);
                mRegisterGetVerificationCode.setText(value+"s");
            }

            @Override
            public void onFinish() {
                mRegisterGetVerificationCode.setEnabled(true);
                mRegisterGetVerificationCode.setText(RegisterActivity.this.codeSendAgain);
            }
        };
        countDownTimer.start();
    }



    @Override
    public void showRegisterResult(boolean result) {
        if (result){
            RememberAccountUtils accountUtils=new RememberAccountUtils();
            accountUtils.rememberAccountPreferences(this,phoneNumber,passWord);
            Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }else {
            createSnackBar(mRegisterPassword,getResources().getString(R.string.register_error_prompt));
            mRegisterRegister.setText(getResources().getString(R.string.register));
        }
    }

    @Override
    public void checkPhoneNumVerified(boolean result) {
        if (result){//已被注册
            checkPhoneNum=true;
        }else {
            checkPhoneNum=false;
        }
        Message phoneMsg=new Message();
        phoneMsg.what=PHONE_RESULT;
        mHandler.sendMessage(phoneMsg);
    }

    private boolean checkAllDataNotEmpty(){

        dataList.clear();
        dataList.add(userName);
        dataList.add(phoneNumber);
        dataList.add(code);
        dataList.add(passWord);
        dataList.add(passwordAgain);
        if (dataList.contains("")){
            return true;
        }else {
            return false;
        }

    }
}