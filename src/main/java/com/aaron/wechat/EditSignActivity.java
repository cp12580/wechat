package com.aaron.wechat;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/23.
 */
public class EditSignActivity extends BaseActivity {

    @BindView(R.id.et_edit_sign)
    EditText mEtEditSign;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String sign = intent.getStringExtra("oldSign");
        mEtEditSign.setText(sign);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit_sign;
    }


    @OnClick({R.id.iv_back_edit_sign, R.id.tv_save_edit_sign})
    public void onClick(View view) {
        Intent in = new Intent();
        in.putExtra("newSign", mEtEditSign.getText().toString());
        setResult(2, in);
        finish();
    }
}
