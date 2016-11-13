package com.aaron.wechat;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.dao.ContactInfo;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.ContactChangeEvent;
import com.aaron.utils.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/11.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back_userinfo)
    ImageView mIvBackUserinfo;
    @BindView(R.id.iv_detail_userinfo)
    ImageView mIvDetailUserinfo;
    @BindView(R.id.iv_avatar_userinfo)
    ImageView mIvAvatarUserinfo;
    @BindView(R.id.tv_name_userinfo)
    TextView mTvNameUserinfo;
    @BindView(R.id.iv_sex_userinfo)
    ImageView mIvSexUserinfo;
    @BindView(R.id.tv_nickname_userinfo)
    TextView mTvNicknameUserinfo;
    @BindView(R.id.btn_add_to_list_userinfo)
    Button mBtnAddToList;

    private String objectId;
    private User  mFindUser;

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mIvBackUserinfo.setOnClickListener(this);
        mIvDetailUserinfo.setOnClickListener(this);
        mBtnAddToList.setOnClickListener(this);
        AVQuery<User> query = AVUser.getQuery(User.class);
        query.whereEqualTo("objectId",objectId);
        query.getFirstInBackground(new GetCallback<User>() {
            @Override
            public void done(User user, AVException e) {
                if (e == null){
                    mFindUser = user;
                    if (user.getString("nickname") == null){
                        mTvNameUserinfo.setText(user.getUsername());
                    }else {
                        mTvNameUserinfo.setText(user.getString("nickname"));
                    }
                    mTvNicknameUserinfo.setText(user.getUsername());
                    if (user.getString("isMale") == null){
                        mIvSexUserinfo.setVisibility(View.INVISIBLE);
                    }else {
                        mIvSexUserinfo.setVisibility(View.VISIBLE);
                        if (user.getString("isMale").equals("男")){
                            mIvSexUserinfo.setImageResource(R.drawable.ic_sex_male);
                        }else {
                            mIvSexUserinfo.setImageResource(R.drawable.ic_sex_female);
                        }
                    }
                    Glide.with(UserInfoActivity.this).load(user.getAvatarUrl())
                            .placeholder(R.drawable.default_useravatar)
                            .error(R.drawable.default_useravatar).into(mIvAvatarUserinfo);
                }else {
                    Util.T(UserInfoActivity.this,e.getMessage());
                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_userinfo:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.iv_detail_userinfo:
                Util.T(this,"detail");
                break;
            case R.id.btn_add_to_list_userinfo:
                AVUser.getCurrentUser().followInBackground(objectId, new FollowCallback() {
                    @Override
                    public void done(AVObject object, AVException e) {
                    }
                    @Override
                    protected void internalDone0(Object o, AVException e) {

                    }
                });
                ContactInfo info = new ContactInfo(null,mFindUser.getUsername(),mFindUser.getString("nickname"),
                mFindUser.getAvatarUrl(),mFindUser.getString("isMale"),mFindUser.getString("region"),
                        mFindUser.getString("sign"),null,null);
                DBManager manager = DBManager.getDbManager(UserInfoActivity.this,User.getCurrentUser().getUsername());
                manager.insertContactInfo(info);
                List<ContactInfo> infos = manager.queryContactInfo();
                EventBus.getDefault().post(new ContactChangeEvent(infos));
                Intent intent = new Intent(UserInfoActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
