package com.aaron.wechat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aaron.utils.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/7.
 */
public class ForgetPswActivity extends BaseActivity {
    @BindView(R.id.et_email_reset)
    EditText mEtEmailReset;
    @BindView(R.id.btn_reset_psw)
    Button mBtnResetPsw;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mBtnResetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEtEmailReset.getText().toString();
                mBtnResetPsw.setEnabled(false);
                if (email != null){
                    AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null){
                                Util.T(ForgetPswActivity.this,"请查收邮箱");
                                ForgetPswActivity.this.finish();
                            }else {
                                Util.T(ForgetPswActivity.this,e.getMessage());
                                mBtnResetPsw.setEnabled(true);
                            }
                        }
                    });
                }else {
                    Util.T(ForgetPswActivity.this,"not null");
                    mBtnResetPsw.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_forget_psw;
    }
}
