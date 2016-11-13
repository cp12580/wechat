package com.aaron.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.aaron.adapter.ConversationListAdapter;
import com.aaron.dao.ContactInfo;
import com.aaron.dao.ConversationItem;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.ContactChangeEvent;
import com.aaron.event.ConversationItemEvent;
import com.aaron.event.MsgStateChangeEvent;
import com.aaron.event.ReceiveMessageEvent;
import com.aaron.event.UnreadMsgEvent;
import com.aaron.wechat.ChatActivity;
import com.aaron.wechat.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/3.
 */
public class ConversationFragment extends BaseFragment {
    @BindView(R.id.rl_net_error)
    RelativeLayout mRlNetError;
    @BindView(R.id.rv_message_list)
    RecyclerView mRvMessageList;

    private ConversationListAdapter mAdapter;
    private DBManager mDBManager;
    private int mUnreadCount = 0;


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_message, null);
        ButterKnife.bind(this, view);
        mRvMessageList.setAdapter(mAdapter);
        mRvMessageList.setLayoutManager(new LinearLayoutManager(mActivity));
        updateConversationList();
        return view;
    }

    @Override
    protected void initData() {
        mAdapter = new ConversationListAdapter(mActivity);
        mDBManager =DBManager.getDbManager(mActivity, User.getCurrentUser().getUsername());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void updateConversationList(){
        List<ConversationItem> conversationItems = mDBManager.queryConversation();
        List<ConversationItem> converList = new ArrayList<>();
        mUnreadCount = 0;
        for (ConversationItem converItem: conversationItems
             ) {
            converList.add(converItem);
            mUnreadCount += converItem.getUnreadCount();
        }
        mAdapter.refresh(converList);
        EventBus.getDefault().post(new UnreadMsgEvent(mUnreadCount));
    }
    @Subscribe
    public void onBus(ReceiveMessageEvent recMsgEvent){
        updateConversationList();
    }
    @Subscribe
    public void onItemClick(ConversationItemEvent event){
        String username = (String) event.view.getTag();
        Intent intent = new Intent(mActivity, ChatActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
    @Subscribe
    public void onContactChangeEvent(ContactChangeEvent event){
        List<ContactInfo> contactInfos = event.infos;
        for (ContactInfo info: contactInfos
             ) {
            String username = info.getUsername();
            String beizhu = info.getBeizhu();
            String url = info.getAvatarUrl();
            ConversationItem conversationItem = mDBManager.queryConversationByUsername(username);
            conversationItem.setBeizhu(beizhu);
            conversationItem.setAvatarUrl(url);
            mDBManager.updateConversation(conversationItem);
        }
        updateConversationList();
    }

    @Subscribe
    public void onMsgStateChangeBus(MsgStateChangeEvent event){
        updateConversationList();
    }
}