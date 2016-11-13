package com.aaron.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.domain.User;
import com.aaron.event.UnreadMsgEvent;
import com.aaron.fragment.ContactFragment;
import com.aaron.fragment.ConversationFragment;
import com.aaron.fragment.FindFragment;
import com.aaron.fragment.MeFragment;
import com.aaron.handler.DefaultMessageHandler;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    @BindView(R.id.vp_main)
    ViewPager mVpMain;
    @BindView(R.id.iv_tab_message)
    ImageView mIvTabMessage;
    @BindView(R.id.tv_unread_message)
    TextView mTvUnreadMessage;
    @BindView(R.id.tv_tab_message)
    TextView mTvTabMessage;
    @BindView(R.id.iv_tab_contact)
    ImageView mIvTabContact;
    @BindView(R.id.tv_unread_contact)
    TextView mTvUnreadContact;
    @BindView(R.id.tv_tab_contact)
    TextView mTvTabContact;
    @BindView(R.id.iv_tab_find)
    ImageView mIvTabFind;
    @BindView(R.id.tv_unread_find)
    TextView mTvUnreadFind;
    @BindView(R.id.tv_tab_find)
    TextView mTvTabFind;
    @BindView(R.id.iv_tab_me)
    ImageView mIvTabMe;
    @BindView(R.id.tv_tab_me)
    TextView mTvTabMe;
    @BindView(R.id.ll_message)
    LinearLayout mLlMessage;
    @BindView(R.id.ll_contact)
    LinearLayout mLlContact;
    @BindView(R.id.ll_find)
    LinearLayout mLlFind;
    @BindView(R.id.ll_me)
    LinearLayout mLlMe;


    private List<Fragment> mFragments;
    private List<ImageView> mImages;
    private List<TextView> mTexts;


    private DefaultMessageHandler msgHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
    }


    private void initData() {
        mFragments = new ArrayList<>();
        mImages = new ArrayList<>();
        mTexts = new ArrayList<>();
        mFragments.add(new ConversationFragment());
        mFragments.add(new ContactFragment());
        mFragments.add(new FindFragment());
        mFragments.add(new MeFragment());
        mImages.add(mIvTabMessage);
        mImages.add(mIvTabContact);
        mImages.add(mIvTabFind);
        mImages.add(mIvTabMe);
        mTexts.add(mTvTabMessage);
        mTexts.add(mTvTabContact);
        mTexts.add(mTvTabFind);
        mTexts.add(mTvTabMe);


        //注册默认的消息处理逻辑
        AVIMClient.getInstance(User.getCurrentUser().getUsername()).open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e != null){
                    Log.d("Aaron",e.getMessage());
                }
            }
        });
        AVIMMessageManager.registerDefaultMessageHandler(msgHandler = new DefaultMessageHandler(this));

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onUnreadMsgBus(UnreadMsgEvent event){
        int unReadCount = event.unreadMsgCount;
        if (unReadCount == 0){
            mTvUnreadMessage.setVisibility(View.INVISIBLE);
        }else {

            mTvUnreadMessage.setVisibility(View.VISIBLE);
            mTvUnreadMessage.setText(unReadCount + "");
        }

    }
    private void getUnreadMsgCount(){
    }

    private void initView() {
        getActionBar().setDisplayShowHomeEnabled(false);
        setOverflowButtonAlways();
        setBottonBar(0);
        mVpMain.setAdapter(new MainAdapter(getSupportFragmentManager()));
        mVpMain.setOffscreenPageLimit(3);
        mLlMessage.setOnClickListener(this);
        mLlContact.setOnClickListener(this);
        mLlFind.setOnClickListener(this);
        mLlMe.setOnClickListener(this);
        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0){
                    mImages.get(position).setAlpha(1.0f - positionOffset);
                    mImages.get(position + 1).setAlpha(positionOffset);
                    mTexts.get(position).setAlpha(1.0f - positionOffset);
                    mTexts.get(position + 1).setAlpha(positionOffset);

                }
            }

            @Override
            public void onPageSelected(int position) {
//                setBottonBar(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void resetTab() {
        for (int i = 0; i < 4 ; i++) {
            mImages.get(i).setAlpha(0f);
            mTexts.get(i).setAlpha(0f);
        }
    }
    private void setBottonBar(int index) {
        resetTab();
        mVpMain.setCurrentItem(index,false);
        mImages.get(index).setAlpha(1.0f);
        mTexts.get(index).setAlpha(1.0f);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_feedback:
                AVIMClient.getInstance(User.getCurrentUser().getUsername()).close(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient client, AVIMException e) {
                        AVIMMessageManager.unregisterMessageHandler(AVIMMessage.class,msgHandler);
                    }
                });
                AVUser.logOut();// 清除缓存用户对象
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                MainActivity.this.finish();
                break;
            case R.id.action_add_friend:
                startActivity(new Intent(MainActivity.this,AddFriendActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        set action menu icon visiable
         */
    private void setOverflowButtonAlways() {
        ViewConfiguration configuration = ViewConfiguration.get(this);
        try {
            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(configuration, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set menu icon visiable
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_message:
                setBottonBar(0);
                break;
            case R.id.ll_contact:
                setBottonBar(1);
                break;
            case R.id.ll_find:
                setBottonBar(2);
                break;
            case R.id.ll_me:
                setBottonBar(3);
                break;
        }
    }

    class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

}
