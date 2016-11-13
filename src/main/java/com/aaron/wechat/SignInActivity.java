package com.aaron.wechat;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.utils.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/7.
 */
public class SignInActivity extends BaseActivity implements View.OnClickListener,TextWatcher{
    @BindView(R.id.et_username_signin)
    EditText mEtUsernameSignin;
    @BindView(R.id.et_password_signin)
    EditText mEtPasswordSignin;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_fgpassword_signin)
    TextView mTvFgpasswordSignin;
    @BindView(R.id.btn_register_signin)
    Button mBtnRegisterSignin;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mBtnLogin.setOnClickListener(this);
        mTvFgpasswordSignin.setOnClickListener(this);
        mBtnRegisterSignin.setOnClickListener(this);
        mEtUsernameSignin.addTextChangedListener(this);
        mEtPasswordSignin.addTextChangedListener(this);
        if (AVUser.getCurrentUser() != null){
            startActivity(new Intent(SignInActivity.this,MainActivity.class));
            SignInActivity.this.finish();
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_signin;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                showDialog("登录中","请稍后...");
                login();
                break;
            case R.id.tv_fgpassword_signin:
                startActivity(new Intent(SignInActivity.this,ForgetPswActivity.class));
                break;
            case R.id.btn_register_signin:
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                SignInActivity.this.finish();
                break;
        }
    }

    private void login() {
        String username =  mEtUsernameSignin.getText().toString();
        String password = mEtPasswordSignin.getText().toString();

        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if (user != null){
                    dismissDialog();
                    startActivity(new Intent(SignInActivity.this,MainActivity.class));
                    SignInActivity.this.finish();
                }else {
                    dismissDialog();
                    Util.T(SignInActivity.this,e.getMessage());
                }

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
        boolean s1 = mEtUsernameSignin.getText().length() > 0;
        boolean s2 = mEtPasswordSignin.getText().length() > 0;
        if (s1 & s2){
            mBtnLogin.setEnabled(true);
            mBtnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
        }else {
            mBtnLogin.setEnabled(false);
            mBtnLogin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
