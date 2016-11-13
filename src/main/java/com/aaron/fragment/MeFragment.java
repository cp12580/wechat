package com.aaron.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.dao.ContactInfo;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.ProfileChangeEvent;
import com.aaron.wechat.ProfileActivity;
import com.aaron.wechat.R;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/3.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.iv_avatar_fgme)
    ImageView mIvAvatarFgme;
    @BindView(R.id.tv_nickname_fgme)
    TextView mTvNicknameFgme;
    @BindView(R.id.iv_sex_fgme)
    ImageView mIvSexFgme;
    @BindView(R.id.tv_username_fgme)
    TextView mTvUsernameFgme;
    @BindView(R.id.iv_erwei_fgme)
    ImageView mIvErweiFgme;
    @BindView(R.id.re_myinfo_fgme)
    RelativeLayout mReMyinfoFgme;
    @BindView(R.id.re_xiangce_fgme)
    RelativeLayout mReXiangceFgme;
    @BindView(R.id.re_shoucang_fgme)
    RelativeLayout mReShoucangFgme;
    @BindView(R.id.re_money_bag_fgme)
    RelativeLayout mReMoneyBagFgme;
    @BindView(R.id.re_card_bag_fgme)
    RelativeLayout mReCardBagFgme;
    @BindView(R.id.re_setting_fgme)
    RelativeLayout mReSettingFgme;

    private User mUser;
    private DBManager mDBManager;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        mIvErweiFgme.setOnClickListener(this);
        mReMyinfoFgme.setOnClickListener(this);
        mReXiangceFgme.setOnClickListener(this);
        mReShoucangFgme.setOnClickListener(this);
        mReMoneyBagFgme.setOnClickListener(this);
        mReCardBagFgme.setOnClickListener(this);
        mReSettingFgme.setOnClickListener(this);
        ContactInfo contactInfo = mDBManager.queryContactInfoByUsername(mUser.getUsername());
        if (contactInfo != null){
            if (contactInfo.getNickname() == null){
                mTvNicknameFgme.setText(contactInfo.getUsername());
            }else {
                mTvNicknameFgme.setText(contactInfo.getNickname());
            }
            mTvUsernameFgme.setText(contactInfo.getUsername());
            if (contactInfo.getIsMale() == null){
                mIvSexFgme.setVisibility(View.INVISIBLE);
            }else {
                mIvSexFgme.setVisibility(View.VISIBLE);
                if ("男".equals(contactInfo.getIsMale())){
                    mIvSexFgme.setImageDrawable(getResources().getDrawable(R.drawable.ic_sex_male));
                }else {
                    mIvSexFgme.setImageDrawable(getResources().getDrawable(R.drawable.ic_sex_female));
                }
            }
                Glide.with(mActivity).load(contactInfo.getAvatarUrl())
                        .placeholder(R.drawable.default_useravatar).error(R.drawable.default_useravatar)
                        .into(mIvAvatarFgme);

        }else {
            updateInfoFromNet();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mUser = User.getCurrentUser();
        EventBus.getDefault().register(this);
        mDBManager = DBManager.getDbManager(mActivity,mUser.getUsername());
    }
    @Subscribe
    public void onProfileChanged(ProfileChangeEvent event){
        updateInfoFromNet();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_erwei_fgme:

                break;
            case R.id.re_myinfo_fgme:
                Intent intent = new Intent(mActivity, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.re_xiangce_fgme:

                break;
            case R.id.re_shoucang_fgme:

                break;
            case R.id.re_money_bag_fgme:


                break;
            case R.id.re_card_bag_fgme:

                break;
            case R.id.re_setting_fgme:

                break;
        }
    }

    /**
     * 一开始就保存信息到数据库中
     */
    private void updateInfoFromNet(){
        mUser.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                String nickname = mUser.getString("nickname");
                String username = mUser.getUsername();
                String isMale = mUser.getString("isMale");
                String avatarUrl = mUser.getAvatarUrl();
                String region = mUser.getString("region");
                String sign = mUser.getString("sign");
                mTvNicknameFgme.setText(nickname != null ? nickname : username);
                mTvUsernameFgme.setText(username);
                if (isMale == null){
                    mIvSexFgme.setVisibility(View.INVISIBLE);
                }else {
                    mIvSexFgme.setVisibility(View.VISIBLE);
                    if ("男".equals(isMale)){
                        mIvSexFgme.setImageDrawable(getResources().getDrawable(R.drawable.ic_sex_male));
                    }else {
                        mIvSexFgme.setImageDrawable(getResources().getDrawable(R.drawable.ic_sex_female));
                    }
                }
                if (avatarUrl == null){
                    mIvAvatarFgme.setImageResource(R.drawable.default_useravatar);
                }else {
                    Glide.with(mActivity).load(avatarUrl).into(mIvAvatarFgme);
                }
                ContactInfo contactInfo1 = new ContactInfo(null,username,nickname,avatarUrl,
                        isMale,region,sign,null,null);
                mDBManager.insertContactInfo(contactInfo1);
            }
        });
    }

}