package com.aaron.wechat;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ChangeBeizhuActivity extends BaseActivity {
    @BindView(R.id.iv_back_edit_beizhu)
    ImageView mIvBackEditBeizhu;
    @BindView(R.id.tv_save_edit_beizhu)
    TextView mTvSaveEditBeizhu;
    @BindView(R.id.et_edit_beizhu)
    EditText mEtEditBeizhu;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_change_beizhu;
    }

    @OnClick({R.id.iv_back_edit_beizhu, R.id.tv_save_edit_beizhu})
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("beizhu", mEtEditBeizhu.getText().toString());
        setResult(0, intent);
        finish();
    }
}
