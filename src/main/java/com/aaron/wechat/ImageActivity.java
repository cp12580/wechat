package com.aaron.wechat;

import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/28.
 */
public class ImageActivity extends BaseActivity {

    @BindView(R.id.iv_preview_image)
    ImageView mIvPreviewImage;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String path = intent.getStringExtra("imageUri");
        Glide.with(this).load(path).into(mIvPreviewImage);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_image;
    }

    @OnClick(R.id.iv_preview_image)
    public void onClick() {
        finish();
    }
}
