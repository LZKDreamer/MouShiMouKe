package com.lzk.moushimouke.View.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzk.moushimouke.Model.Utils.RememberAccountUtils;
import com.lzk.moushimouke.Presenter.LoginPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.ILogin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILogin {
    private boolean phoneResult;
    private String phoneNumber,password;
    private LoginPresenter mLoginPresenter;
    public static LoginActivity sLoginActivity;
    private static final int PHONE_RESULT=1;
    private Handler mHandler;

    @BindView(R.id.login_username)
    EditText mLoginUsername;
    @BindView(R.id.login_password)
    EditText mLoginPassword;
    @BindView(R.id.login_forget_password)
    Button mLoginForgetPassword;
    @BindView(R.id.login_register)
    Button mLoginRegister;
    @BindView(R.id.login)
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sLoginActivity=this;
        mLoginPresenter=new LoginPresenter();
    }


    @OnClick({R.id.login_forget_password, R.id.login_register,R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_forget_password:
                Intent resetIntent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(resetIntent);
                break;
            case R.id.login_register:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
                break;
            case R.id.login:
                phoneNumber=mLoginUsername.getText().toString();
                password=mLoginPassword.getText().toString();
                mLogin.setText(getResources().getString(R.string.logging));
                mLoginPresenter.requestCheckPhoneIsVerified(phoneNumber);

                mHandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what){
                            case PHONE_RESULT:
                                if (phoneNumber.isEmpty()||password.isEmpty()){
                                    createSnackBar(mLogin,getResources().getString(R.string.data_not_empty_prompt));
                                }else if (phoneNumber.length()!=11){
                                    createSnackBar(mLogin,getResources().getString(R.string.phone_number_error));
                                } else if (phoneResult==false){
                                    createSnackBar(mLogin,getResources().getString(R.string.phone_number_not_verified));
                                }else {
                                    mLoginPresenter.requestLogin(phoneNumber,password);
                                }
                                break;
                        }
                    }
                };
                break;
        }
    }

    @Override
    public void getLoginResult(boolean result) {
        if (result){//登录成功
            RememberAccountUtils accountUtils=new RememberAccountUtils();
            accountUtils.rememberAccountPreferences(this,phoneNumber,password);
            Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }else {
            createSnackBar(mLogin,getResources().getString(R.string.error_password));
            mLogin.setText(getResources().getString(R.string.login));
        }

    }

    @Override
    public void getLoginPhoneNumIsVerified(boolean result) {
        if (result){//手机号已被注册
            phoneResult=true;
        }else {
            phoneResult=false;
        }
        Message phoneMsg=new Message();
        phoneMsg.what=PHONE_RESULT;
        mHandler.sendMessage(phoneMsg);
    }

    private void createSnackBar(View view,String prompt){
        Snackbar.make(view,prompt,Snackbar.LENGTH_LONG).show();
    }

}
