package com.aaron.wechat;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/23.
 */
public class EditProfileActivity extends BaseActivity {
    @BindView(R.id.iv_back_edit_nickname)
    ImageView mIvBackEditNickname;
    @BindView(R.id.et_edit_nickname)
    EditText mEtEditNickname;

    private Intent mIntent;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mIntent = getIntent();
        String oldnickname = mIntent.getStringExtra("oldnickname");
        mEtEditNickname.setText(oldnickname);
        mIvBackEditNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.putExtra("newNickname", mEtEditNickname.getText().toString());
                setResult(0, in);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit_profile;
    }

    @OnClick(R.id.tv_save_edit_nickname)
    public void onClick() {
        Intent in = new Intent();
        in.putExtra("newNickname", mEtEditNickname.getText().toString());
        setResult(0, in);
        finish();
    }
}
