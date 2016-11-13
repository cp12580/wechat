package com.aaron.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aaron.adapter.ContactListAdapter;
import com.aaron.adapter.ContactsDividerDecoration;
import com.aaron.dao.ContactInfo;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.ContactChangeEvent;
import com.aaron.wechat.ContactInfoActivity;
import com.aaron.wechat.R;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/3.
 */
public class ContactFragment extends BaseFragment{
    @BindView(R.id.rv_contact_list)
    RecyclerView mRvContactList;


    private List<ContactInfo> mContacts;
    private ContactListAdapter mAdapter;
    private DBManager mDBManager;
    private User mUser;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_contact, null);
        ButterKnife.bind(this, view);
        mAdapter = new ContactListAdapter(mActivity,mContacts);
        mRvContactList.setAdapter(mAdapter);
        mRvContactList.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvContactList.addItemDecoration(new ContactsDividerDecoration(mActivity));
        mAdapter.setOnContactClickListener(new ContactListAdapter.OnContactItemClickListener() {
            @Override
            public void onItemClick(View v, String username) {
                Intent intent = new Intent(mActivity, ContactInfoActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        List<ContactInfo> contactInfos = mDBManager.queryContactInfo();
        for (ContactInfo info : contactInfos
                ) {
            if (info.getUsername() != null && !mUser.getUsername().equals(info.getUsername())){
                mContacts.add(info);
            }
        }
        if (mContacts != null && mContacts.size() != 0){
            mAdapter.notifyDataSetChanged();
        }else {
            refreshContactsFromNet(new ContactFromNetCallback() {
                @Override
                public void contactFromNetDone(List<ContactInfo> contactInfos) {
                    mContacts.clear();
                    mContacts.addAll(contactInfos);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    @Override
    protected void initData() {
        mContacts = new ArrayList<>();
        mUser = User.getCurrentUser();
        mDBManager = DBManager.getDbManager(mActivity,mUser.getUsername());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void refreshContactBus(ContactChangeEvent event){
        List<ContactInfo> contactInfos = event.infos;
        mContacts.clear();
        for (ContactInfo info : contactInfos) {
            if (!mUser.getUsername().equals(info.getUsername())){
                mContacts.add(info);
            }
        }
        if (mContacts != null && mContacts.size() != 0){
            mAdapter.notifyDataSetChanged();
        }
    }

    //从网上加载联系人列表
    private void refreshContactsFromNet(final ContactFromNetCallback callback){
        //查询关注者
        AVQuery<User> followeeQuery = User.followeeQuery(mUser.getObjectId(), User.class);
        followeeQuery.include("followee");
        followeeQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> avObjects, AVException avException) {
                if (avObjects != null) {
                    //avObjects 就是用户的关注用户列表
                    List<ContactInfo> infos = new ArrayList<ContactInfo>();
                    for (User user : avObjects) {
                        ContactInfo info = new ContactInfo(null, user.getUsername(), user.getString("nickname")
                                , user.getAvatarUrl(), user.getString("isMale"), user.getString("region")
                                , user.getString("sign"), null, null);
                        //写入数据库
                        mDBManager.insertContactInfo(info);
                        infos.add(info);
                    }
                    callback.contactFromNetDone(infos);
                }
            }
        });
    }
    //从网上加载数据后的回调
    public interface ContactFromNetCallback{
        void contactFromNetDone(List<ContactInfo> contactInfos);
    }

}
