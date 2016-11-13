package com.aaron.wechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aaron.utils.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/7.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener,TextWatcher{

    @BindView(R.id.et_username_signup)
    EditText mEtUsernameSignup;
    @BindView(R.id.et_password_signup)
    EditText mEtPasswordSignup;
    @BindView(R.id.et_confirm_signup)
    EditText mEtConfirmSignup;
    @BindView(R.id.et_email_signup)
    EditText mEtEmailSignup;
    @BindView(R.id.btn_register_signup)
    Button mBtnRegisterSignup;


    private ProgressDialog mDialog;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mBtnRegisterSignup.setOnClickListener(this);
        mEtUsernameSignup.addTextChangedListener(this);
        mEtPasswordSignup.addTextChangedListener(this);
        mEtConfirmSignup.addTextChangedListener(this);
        mEtEmailSignup.addTextChangedListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_signup;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register_signup:
                mDialog = ProgressDialog.show(this,"注册中","请稍后。。。",true,false);
                mBtnRegisterSignup.setEnabled(false);
                register();
                break;
        }
    }

    private void register(){
        final String username = mEtUsernameSignup.getText().toString();
        final String password = mEtPasswordSignup.getText().toString();
        String confirm = mEtConfirmSignup.getText().toString();
        final String email = mEtEmailSignup.getText().toString();

        if (!password.equals(confirm)){
            Util.T(this,"两次输入的密码不一致");
            mBtnRegisterSignup.setEnabled(true);
            mDialog.dismiss();
            return;
        }else {
            final AVUser user = new AVUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null){
                        Util.T(SignUpActivity.this,"注册成功");
                        mBtnRegisterSignup.setEnabled(true);
                        mDialog.dismiss();
                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                        SignUpActivity.this.finish();
                    }else {
                        mDialog.dismiss();
                        mBtnRegisterSignup.setEnabled(true);
                        switch (e.getCode()) {
                            case 202:
                                Util.T(SignUpActivity.this,"用户名已存在");
                                break;
                            case 203:
                                Util.T(SignUpActivity.this,"邮箱已注册");
                                break;
                            default:
                                Util.T(SignUpActivity.this,"网络异常");
                                break;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
        boolean s1 = mEtUsernameSignup.getText().length() > 0;
        boolean s2 = mEtPasswordSignup.getText().length() > 0;
        boolean s3 = mEtConfirmSignup.getText().length() > 0;
        boolean s4 = mEtEmailSignup.getText().length() > 0;
        if (s1 & s2&s3&s4){
            mBtnRegisterSignup.setEnabled(true);
            mBtnRegisterSignup.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
        }else {
            mBtnRegisterSignup.setEnabled(false);
            mBtnRegisterSignup.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
