package com.aaron.wechat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.dao.ContactInfo;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.ProfileChangeEvent;
import com.aaron.utils.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back_profile)
    ImageView mIvBackProfile;
    @BindView(R.id.iv_avatar_profile)
    ImageView mIvAvatarProfile;
    @BindView(R.id.re_avatar_profile)
    RelativeLayout mReAvatarProfile;
    @BindView(R.id.tv_nickname_profile)
    TextView mTvNicknameProfile;
    @BindView(R.id.re_nickname_profile)
    RelativeLayout mReNicknameProfile;
    @BindView(R.id.tv_username_profile)
    TextView mTvUsernameProfile;
    @BindView(R.id.re_username)
    RelativeLayout mReUsername;
    @BindView(R.id.re_erweima_profile)
    RelativeLayout mReErweimaProfile;
    @BindView(R.id.tv_sex_profile)
    TextView mTvSexProfile;
    @BindView(R.id.re_sex_profile)
    RelativeLayout mReSexProfile;
    @BindView(R.id.tv_region_profile)
    TextView mTvRegionProfile;
    @BindView(R.id.re_region)
    RelativeLayout mReRegion;
    @BindView(R.id.tv_sign_profile)
    TextView mTvSignProfile;
    @BindView(R.id.re_sign_profile)
    RelativeLayout mReSignProfile;
    @BindView(R.id.tv_save_profile)
    TextView mTvSaveProfile;


    private User mUser;
    private DBManager mDBManager;



    @Override
    protected void initData() {
        super.initData();
        mUser = User.getCurrentUser();
        mDBManager = DBManager.getDbManager(this,mUser.getUsername());
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mIvBackProfile.setOnClickListener(this);
        mReAvatarProfile.setOnClickListener(this);
        mReNicknameProfile.setOnClickListener(this);
        mReSexProfile.setOnClickListener(this);
        mReRegion.setOnClickListener(this);
        mReSignProfile.setOnClickListener(this);
        mTvSaveProfile.setOnClickListener(this);
        ContactInfo contactInfo = mDBManager.queryContactInfoByUsername(mUser.getUsername());
        if (contactInfo != null){
            mTvNicknameProfile.setText(contactInfo.getNickname());
            mTvUsernameProfile.setText(contactInfo.getUsername());
            mTvSexProfile.setText(contactInfo.getIsMale());
            mTvRegionProfile.setText(contactInfo.getRegion());
            mTvSignProfile.setText(contactInfo.getSign());
            if (contactInfo.getAvatarUrl() != null){
                Glide.with(this).load(contactInfo.getAvatarUrl()).placeholder(R.drawable.default_useravatar)
                        .error(R.drawable.default_useravatar).into(mIvAvatarProfile);
            }else {
                mIvAvatarProfile.setImageResource(R.drawable.default_useravatar);
            }

        }

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_profile:
                finish();
                break;
            case R.id.re_avatar_profile:
                Intent intent0 = new Intent(Intent.ACTION_PICK, null);
                intent0.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent0, 1);
                break;
            case R.id.re_nickname_profile:
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("oldnickname", mTvNicknameProfile.getText());
                startActivityForResult(intent, 0);
                break;
            case R.id.re_sex_profile:
                showSexDialog();
                break;
            case R.id.re_region:
                Intent intent1 = new Intent(ProfileActivity.this, EditRegionActivity.class);
                intent1.putExtra("oldRegion", mTvRegionProfile.getText());
                startActivityForResult(intent1, 0);
                break;
            case R.id.re_sign_profile:
                Intent intent2 = new Intent(ProfileActivity.this, EditSignActivity.class);
                intent2.putExtra("oldSign", mTvSignProfile.getText());
                startActivityForResult(intent2, 0);
                break;
            case R.id.tv_save_profile:
                //update profile
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("保存中");
                dialog.show();
                mUser.put("nickname", mTvNicknameProfile.getText());
                mUser.put("region", mTvRegionProfile.getText());
                mUser.put("sign", mTvSignProfile.getText());
                mUser.put("isMale", mTvSexProfile.getText());
                mUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            dialog.dismiss();
                            Util.T(ProfileActivity.this, "保存成功");
                            EventBus.getDefault().post(new ProfileChangeEvent());
                            ContactInfo info = mDBManager.queryContactInfoByUsername(mUser.getUsername());
                            info.setNickname(mTvNicknameProfile.getText().toString());
                            info.setIsMale(mTvSexProfile.getText().toString());
                            info.setRegion(mTvRegionProfile.getText().toString());
                            info.setSign(mTvSignProfile.getText().toString());
                            mDBManager.updateContactInfo(info);
                        } else {
                            Util.T(ProfileActivity.this, "保存失败请重试");
                            dialog.dismiss();
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_OK) {
            switch (resultCode) {
                case 0:
                    mTvNicknameProfile.setText(data.getExtras().getString("newNickname"));
                    break;
                case 1:
                    mTvRegionProfile.setText(data.getExtras().getString("newRegion"));
                    break;
                case 2:
                    mTvSignProfile.setText(data.getExtras().getString("newSign"));
                    break;
            }
        }
            switch (requestCode) {
                case 1:
                    if (data != null) {
                        startPhotoZoom(data.getData(), 480);
                    }
                    break;
                case 2:
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getCanonicalPath()
                                + "/wechat/" + "avatar.png");
                        if (bitmap == null) {
                            mIvAvatarProfile.setImageResource(R.drawable.default_useravatar);
                        } else {
                            mIvAvatarProfile.setImageBitmap(bitmap);
                            mUser.saveAvatar(Environment.getExternalStorageDirectory().getCanonicalPath()
                                    + "/wechat/avatar.png", new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    ContactInfo info = mDBManager.queryContactInfoByUsername(mUser.getUsername());
                                    info.setAvatarUrl(mUser.getAvatarUrl());
                                    mDBManager.updateContactInfo(info);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }

    }

    /**
     * 打开性别选择界面
     */
    private void showSexDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);
        LinearLayout ll_title = (LinearLayout) window
                .findViewById(R.id.ll_title);
        ll_title.setVisibility(View.VISIBLE);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        tv_title.setText("性别");
        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("男");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTvSexProfile.setText("男");
                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("女");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTvSexProfile.setText("女");
                dlg.cancel();
            }
        });
    }

    /**
     * 打开裁剪界面
     * @param uri1
     * @param size
     */
    private void startPhotoZoom(Uri uri1, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory().getCanonicalPath()
                    + "/wechat/", "avatar.png");
            File tpfile = new File(Environment.getExternalStorageDirectory().getCanonicalPath()
                    + "/wechat/");//SD卡根目录下的WeChat文件夹
            if (!tpfile.exists()) {
                tpfile.mkdir();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));// 专入目标文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, 2);
    }
}
