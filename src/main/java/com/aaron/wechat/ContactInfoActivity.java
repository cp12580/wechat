package com.aaron.wechat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.dao.ContactInfo;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.ContactChangeEvent;
import com.aaron.utils.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/16.
 */
public class ContactInfoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back_contactinfo)
    ImageView mIvBackContactinfo;
    @BindView(R.id.iv_detail_contactinfo)
    ImageView mIvDetailContactinfo;
    @BindView(R.id.iv_avatar_contactinfo)
    ImageView mIvAvatarContactinfo;
    @BindView(R.id.tv_name_contactinfo)
    TextView mTvNameContactinfo;
    @BindView(R.id.iv_sex_contactinfo)
    ImageView mIvSexContactinfo;
    @BindView(R.id.tv_nickname_contactinfo)
    TextView mTvNicknameContactinfo;
    @BindView(R.id.btn_sendmsg_contactinfo)
    Button mBtnSendmsgContactinfo;
    @BindView(R.id.tv_region_contactinfo)
    TextView mTvRegionContactinfo;
    @BindView(R.id.re_region_contactinfo)
    RelativeLayout mReRegionContactinfo;
    @BindView(R.id.tv_sign_contactinfo)
    TextView mTvSignContactinfo;
    @BindView(R.id.re_sign_contactinfo)
    RelativeLayout mReSignContactinfo;

    private String mUsername;
    private DBManager mDBManager;
    private String objectId;
    private ContactInfo mContactInfo;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mIvBackContactinfo.setOnClickListener(this);
        mBtnSendmsgContactinfo.setOnClickListener(this);
        mIvDetailContactinfo.setOnClickListener(this);
        mContactInfo = mDBManager.queryContactInfoByUsername(mUsername);
        Glide.with(this).load(mContactInfo.getAvatarUrl()).placeholder(R.drawable.default_useravatar)
                .error(R.drawable.default_useravatar).into(mIvAvatarContactinfo);
        if (mContactInfo.getBeizhu() != null){
            mTvNameContactinfo.setText(mContactInfo.getBeizhu());
        }else if (mContactInfo.getNickname() != null){
            mTvNameContactinfo.setText(mContactInfo.getNickname());
        }else {
            mTvNameContactinfo.setText(mUsername);
        }
        mTvNicknameContactinfo.setText(mUsername);
        if (mContactInfo.getIsMale() == null){
            mIvSexContactinfo.setVisibility(View.INVISIBLE);
        }else {
            mIvSexContactinfo.setVisibility(View.VISIBLE);
            mIvSexContactinfo.setImageResource("男".equals(mContactInfo.getIsMale()) ?
                R.drawable.ic_sex_male : R.drawable.ic_sex_female);
        }

        if (mContactInfo.getRegion() == null){
            mReRegionContactinfo.setVisibility(View.GONE);
        }else {
            mReRegionContactinfo.setVisibility(View.VISIBLE);
            mTvRegionContactinfo.setText(mContactInfo.getRegion());
        }

        if (mContactInfo.getSign() == null){
            mReSignContactinfo.setVisibility(View.GONE);
        }else {
            mReSignContactinfo.setVisibility(View.VISIBLE);
            mTvSignContactinfo.setText(mContactInfo.getSign());
        }
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        mDBManager = DBManager.getDbManager(this, User.getCurrentUser().getUsername());
        AVQuery<User> query = User.getQuery(User.class);
        query.whereEqualTo("username",mUsername);
        query.getFirstInBackground(new GetCallback<User>() {
            @Override
            public void done(User user, AVException e) {
                objectId = user.getObjectId();
            }
        });

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_contactinfo;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_contactinfo:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.btn_sendmsg_contactinfo:
                Intent intent = new Intent(ContactInfoActivity.this, ChatActivity.class);
                intent.putExtra("username", mUsername);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.iv_detail_contactinfo:
                showDetailDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    /**
     * 打开性别选择界面
     */
    private void showDetailDialog() {
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
        tv_title.setText("菜单");
        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("修改备注");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactInfoActivity.this,ChangeBeizhuActivity.class);
                startActivityForResult(intent,0);
                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("删除好友");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
                new AlertDialog.Builder(ContactInfoActivity.this)
                        .setMessage("是否删除该好友")
                        .setPositiveButton("是",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface anInterface, int i) {
                                User.getCurrentUser().unfollowInBackground(objectId, new FollowCallback() {
                                    @Override
                                    public void done(AVObject object, AVException e) {
                                        if (e == null) {
                                            mDBManager.deleteContactInfo(mContactInfo);
                                            List<ContactInfo> infos = mDBManager.queryContactInfo();
                                            EventBus.getDefault().post(new ContactChangeEvent(infos));
                                            ContactInfoActivity.this.finish();
                                            Util.T(ContactInfoActivity.this,"删除成功");
                                        } else {
                                            Log.w("Aaron", "unfollow failed.");
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface anInterface, int i) {

                            }
                        }).create().show();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == resultCode){
            String beizhu = data.getExtras().getString("beizhu");
            if (beizhu !=null && !"".equals(beizhu)){
                mTvNameContactinfo.setText(beizhu);
                mContactInfo.setBeizhu(beizhu);
                mDBManager.updateContactInfo(mContactInfo);
                List<ContactInfo> infos = mDBManager.queryContactInfo();
                EventBus.getDefault().post(new ContactChangeEvent(infos));
            }
        }
    }
}
