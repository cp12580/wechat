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
public class EditRegionActivity extends BaseActivity {
    @BindView(R.id.et_edit_region)
    EditText mEtEditRegion;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String region = intent.getStringExtra("oldRegion");
        mEtEditRegion.setText(region);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit_region;
    }

    @OnClick({R.id.iv_back_edit_region, R.id.tv_save_edit_region})
    public void onClick(View view) {
        Intent in = new Intent();
        in.putExtra("newRegion", mEtEditRegion.getText().toString());
        setResult(1,in);
        finish();
    }
}
