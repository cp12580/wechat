package com.aaron.wechat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.adapter.ChatListAdapter;
import com.aaron.dao.ContactInfo;
import com.aaron.dao.ConversationItem;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.MsgStateChangeEvent;
import com.aaron.event.PreviewImageEvent;
import com.aaron.event.ReceiveMessageEvent;
import com.aaron.utils.Util;
import com.aaron.view.PressToSpeekButton;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/16.
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.iv_back_chat)
    ImageView mIvBackChat;
    @BindView(R.id.name_chat)
    TextView mNameChat;
    @BindView(R.id.iv_setting_chat)
    ImageView mIvSettingChat;
    @BindView(R.id.iv_setting_group_chat)
    ImageView mIvSettingGroupChat;
    @BindView(R.id.btn_set_mode_voice)
    Button mBtnSetModeVoice;//语音模式
    @BindView(R.id.btn_set_mode_keyboard)
    Button mBtnSetModeKeyboard;//文字模式
    @BindView(R.id.btn_press_to_speak)
    PressToSpeekButton mBtnPressToSpeak;//按住说话
    @BindView(R.id.et_sendmessage)
    EditText mEtSendmessage;//文字输入框
    @BindView(R.id.iv_emoticons_normal)
    ImageView mIvEmoticonsNormal;//关闭emoji
    @BindView(R.id.iv_emoticons_checked)
    ImageView mIvEmoticonsChecked;//打开emoji
    @BindView(R.id.btn_more_chat)
    Button mBtnMoreChat;//更多
    @BindView(R.id.btn_send_chat)
    Button mBtnSendChat;//发送文字消息
    @BindView(R.id.vPager_face)
    ViewPager mVPagerFace;//emoji容器
    @BindView(R.id.ll_face_container)
    LinearLayout mLlFaceContainer;
    @BindView(R.id.ll_more_chat)
    LinearLayout mLlMoreChat;
    @BindView(R.id.pb_load_more)
    ProgressBar mPbLoadMore;//上拉加载聊天记录
    @BindView(R.id.recycview_chat)
    RecyclerView mRvChatList;//对话列表
    @BindView(R.id.mic_image_chat)
    ImageView mMicImageChat;//语音对话音量指示器
    @BindView(R.id.recording_hint_chat)
    TextView mRecordingHintChat;
    @BindView(R.id.recording_container)
    RelativeLayout mRecordingContainer;
    @BindView(R.id.btn_picture_chatactivity)
    ImageView mBtnPictureChatactivity;
    @BindView(R.id.btn_location_chatactivity)
    ImageView mBtnLocationChatactivity;
    @BindView(R.id.edittext_layout_chat)
    RelativeLayout mEdittextLayoutChat;




    private String mUsername;
    private AVIMConversation mConversation;
    private ChatListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DBManager mDBManager;
    private ContactInfo contact;
    private ConversationItem conversationBean;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mEtSendmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                if (sequence.length() == 0){
                    mBtnMoreChat.setVisibility(View.VISIBLE);
                    mBtnSendChat.setVisibility(View.GONE);
                }else {
                    mBtnMoreChat.setVisibility(View.GONE);
                    mBtnSendChat.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mIvBackChat.setOnClickListener(this);
        mBtnSendChat.setOnClickListener(this);
        mBtnMoreChat.setOnClickListener(this);
        mIvSettingChat.setOnClickListener(this);
        mIvEmoticonsNormal.setOnClickListener(this);
        mBtnPictureChatactivity.setOnClickListener(this);
        mBtnLocationChatactivity.setOnClickListener(this);
        mBtnSetModeVoice.setOnClickListener(this);
        mBtnSetModeKeyboard.setOnClickListener(this);
        mBtnMoreChat.setOnClickListener(this);
        mRvChatList.setAdapter(mAdapter);
        mRvChatList.setLayoutManager(mLayoutManager);
        mEtSendmessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                scrollToBottom();
                if (mLlMoreChat.getVisibility() == View.VISIBLE){
                    mLlMoreChat.setVisibility(View.GONE);
                }
                if (mLlFaceContainer.getVisibility() == View.VISIBLE){
                    mLlFaceContainer.setVisibility(View.GONE);
                }
                return false;
            }
        });

        if (contact.getBeizhu() != null){
            mNameChat.setText(contact.getBeizhu());
        }else if (contact.getNickname() != null){
            mNameChat.setText(contact.getNickname());
        }else {
            mNameChat.setText(contact.getUsername());
        }
        conversationBean = mDBManager.queryConversationByUsername(mUsername);
        if (conversationBean != null){
            conversationBean.setUnreadCount(0);
            mDBManager.updateConversation(conversationBean);
            EventBus.getDefault().post(new MsgStateChangeEvent());
        }
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        createConversation(mUsername);
        isFriend();
        mDBManager =DBManager.getDbManager(this,User.getCurrentUser().getUsername());
        contact = mDBManager.queryContactInfoByUsername(mUsername);
        mAdapter = new ChatListAdapter(this,contact);
        mLayoutManager = new LinearLayoutManager(this);

        EventBus.getDefault().register(this);
    }

    /**
     * 判断是否是好友
     */
    private void isFriend(){
        //查询我的粉丝
        AVQuery<User> qy = User.getQuery(User.class);
        qy.whereEqualTo("username",mUsername);
        qy.getFirstInBackground(new GetCallback<User>() {
            @Override
            public void done(User user, AVException e) {
                AVQuery<User> query = User.getCurrentUser().followerQuery(User.getCurrentUserId(),User.class);
                query.whereEqualTo("follower",user);
                query.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> list, AVException e) {
                        if (list.toArray().length == 0){
                            new AlertDialog.Builder(ChatActivity.this)
                                    .setMessage("您还不是对方的好友,请先通过验证")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface anInterface, int i) {
                                            mDBManager.deleteConversation(mDBManager.queryConversationByUsername(mUsername));
                                            EventBus.getDefault().post(new MsgStateChangeEvent());
                                            ChatActivity.this.finish();
                                        }
                                    })
                                    .create().show();
                            return;
                        }
                    }
                });
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_chat;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_chat:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.btn_more_chat:
                if (mLlMoreChat.getVisibility() == View.GONE){
                    mLlMoreChat.setVisibility(View.VISIBLE);
                    hideKeyboard();
                    if (mLlFaceContainer.getVisibility() == View.VISIBLE){
                        mLlFaceContainer.setVisibility(View.GONE);
                        mIvEmoticonsChecked.setVisibility(View.GONE);
                    }
                }else {
                    mLlMoreChat.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_send_chat:
                final String text = mEtSendmessage.getText().toString();
                mEtSendmessage.setText("");
                 final AVIMTextMessage msg = new AVIMTextMessage();
                msg.setText(text);
                mAdapter.sendMessage(msg);
                scrollToBottom();
                mConversation.sendMessage(msg, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        mAdapter.notifyDataSetChanged();
                        //更新数据库
                        updateDatabase(msg,text);
                    }
                });
                break;
            case R.id.iv_setting_chat:
                //点击头像进入菜单
                mDBManager.deleteConversation(mDBManager.queryConversationByUsername(mUsername));
                EventBus.getDefault().post(new MsgStateChangeEvent());
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_emoticons_normal:
                if (mLlFaceContainer.getVisibility() == View.GONE){
                    mLlFaceContainer.setVisibility(View.VISIBLE);
                    hideKeyboard();
                    if (mLlMoreChat.getVisibility() == View.VISIBLE){
                        mLlMoreChat.setVisibility(View.GONE);
                    }
                    mIvEmoticonsChecked.setVisibility(View.VISIBLE);
                }else {
                    mLlFaceContainer.setVisibility(View.GONE);
                    mIvEmoticonsChecked.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_picture_chatactivity:
                Intent picIntent = new Intent(Intent.ACTION_PICK);
                picIntent.setType("image/*");
                startActivityForResult(picIntent,0);
                break;
            case R.id.btn_location_chatactivity:

                break;
            case R.id.btn_set_mode_voice:
                mBtnSetModeVoice.setVisibility(View.GONE);
                mBtnSetModeKeyboard.setVisibility(View.VISIBLE);
                mBtnPressToSpeak.setVisibility(View.VISIBLE );
                mEdittextLayoutChat.setVisibility(View.GONE);
                hideKeyboard();
                break;
            case R.id.btn_set_mode_keyboard:
                mBtnSetModeVoice.setVisibility(View.VISIBLE);
                mBtnSetModeKeyboard.setVisibility(View.GONE );
                mBtnPressToSpeak.setVisibility(View.GONE );
                mEdittextLayoutChat.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            String imgPath = null;
            if (requestCode == 0 ){
                if (data != null){
                    Uri imgUri = data.getData();
                    Cursor cursor = getContentResolver().query(imgUri, null, null, null, null);
                    if (cursor !=null){
                        cursor.moveToFirst();
                        int dataIndex = cursor.getColumnIndex("_data");
                        imgPath = cursor.getString(dataIndex);
                        cursor.close();
                        if (imgPath == null || imgPath.equals("null")) {
                            Util.T(ChatActivity.this,"找不到图片地址");
                            return;
                        }

//                        Intent previewIntent = new Intent(ChatActivity.this,ImageActivity.class);
//                        previewIntent.putExtra("path",imgPath);
//                        previewIntent.putExtra("type","send");
//                        startActivityForResult(previewIntent,1);
                        try {
                            final AVIMImageMessage imgMsg = new AVIMImageMessage(imgPath);
                            mAdapter.sendMessage(imgMsg);
                            imgMsg.setText(imgPath);
                            scrollToBottom();
                            mConversation.sendMessage(imgMsg, new AVIMConversationCallback() {
                                @Override
                                public void done(AVIMException e) {
                                    mAdapter.notifyDataSetChanged();
                                    //更新数据库
                                    updateDatabase(imgMsg,"[图片]");
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBus(ReceiveMessageEvent event){
        if (event.conversation.getConversationId().equals(mConversation.getConversationId())){
            mAdapter.addMessage(event.message);
            scrollToBottom();
            ConversationItem item = mDBManager.queryConversationById(mConversation.getConversationId());
            item.setUnreadCount(0);
            mDBManager.updateConversation(item);
        }
    }


    @Subscribe
    public void onPreImageBus(PreviewImageEvent event){
        String imageUrl= event.url;
        Intent intent = new Intent(this,ImageActivity.class);
        intent.putExtra("imageUri",imageUrl);
        startActivity(intent);
    }
    /**
     * 滚动 recyclerView 到底部
     */
    private void scrollToBottom() {
        mLayoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
    }

    /**
     * 创建会话
     * @param username
     */
    public void createConversation(final String username){
        AVIMClient.getInstance(User.getCurrentUser().getUsername()).createConversation(Arrays.asList(username)
                , username, null, false, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(final AVIMConversation conversation, AVIMException e) {
                        if (e != null){
                            Log.d("Aaron",e.getMessage());
                        }else {
                            mConversation = conversation;
                            mAdapter.setConversation(conversation);
                            AVIMConversation histroy = AVIMClient.getInstance(User.getCurrentUser().getUsername())
                                    .getConversation(conversation.getConversationId());
                            histroy.queryMessages(new AVIMMessagesQueryCallback() {
                                @Override
                                public void done(List<AVIMMessage> list, AVIMException e) {
                                    //成功获取最新20条消息记录
                                    mAdapter.addMessageList(list);
                                    scrollToBottom();
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void updateDatabase(AVIMMessage msg,String lastText){
        //更新数据库
        if (conversationBean != null){
            conversationBean.setUpdateTime(msg.getTimestamp());
            conversationBean.setLastMessage(lastText);
            conversationBean.setUnreadCount(0);
            mDBManager.updateConversation(conversationBean);
        }else if (conversationBean == null){
            conversationBean = new ConversationItem(null,mConversation.getConversationId(),0,msg.getTimestamp()
                    ,contact.getUsername(),contact.getNickname(),contact.getBeizhu(),lastText,contact.getAvatarUrl());
            mDBManager.insertConversation(conversationBean);
        }
        EventBus.getDefault().post(new MsgStateChangeEvent());
    }

}
