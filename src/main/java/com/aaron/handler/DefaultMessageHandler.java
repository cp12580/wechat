package com.aaron.handler;

import android.app.Activity;
import android.util.Log;

import com.aaron.dao.ContactInfo;
import com.aaron.dao.ConversationItem;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.ReceiveMessageEvent;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class DefaultMessageHandler extends AVIMMessageHandler {
    DBManager dbmanager;
    public DefaultMessageHandler(Activity activity){
        dbmanager =DBManager.getDbManager(activity,User.getCurrentUser().getUsername());
    }
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        if (User.getCurrentUserId() == null) {
            Log.d("Aaron","selfId is null, please call open!");
            client.close(null);
        } else {
            if (!client.getClientId().equals(User.getCurrentUser().getUsername())) {
                client.close(null);
                AVIMClient.getInstance(User.getCurrentUser().getUsername()).open(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient client, AVIMException e) {
                        if (e != null){
                            Log.d("Aaron",e.getMessage());
                        }
                    }
                });
            } else {
                if (!message.getFrom().equals(client.getClientId())) {
//                    if (LCIMNotificationUtils.isShowNotification(conversation.getConversationId())) {
//                        sendNotification(message, conversation);
//                    }
//                    LCIMConversationItemCache.getInstance().increaseUnreadCount(message.getConversationId());
//                    sendEvent(message, conversation);
                    onReceivedMessageEvent(message,conversation);
                } else {
//                    LCIMConversationItemCache.getInstance().insertConversation(message.getConversationId());
                }
            }
        }
    }

    @Override
    public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }

    private void onReceivedMessageEvent(AVIMMessage message,final AVIMConversation conversation){

        if (conversation.getName() == null){
            AVIMClient.getInstance(User.getCurrentUser().getUsername()).createConversation(Arrays.asList(message.getFrom())
                    , message.getFrom(), null, false, true, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(final AVIMConversation conversation, AVIMException e) {
                            if (e != null){
                                Log.d("Aaron",e.getMessage());
                            }else {
                                insertIntoDatabase(conversation);
                            }
                        }
            });
        }else {
            insertIntoDatabase(conversation);
        }
    }

    //update database
    private void insertIntoDatabase(final AVIMConversation conversation){
        conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
            @Override
            public void done(AVIMMessage message, AVIMException e) {
                String lastMessage = null;

                if (message instanceof AVIMTextMessage){//最后一个消息
                    lastMessage =  (( AVIMTextMessage) message).getText();
                }
                if (message instanceof AVIMImageMessage){
                    lastMessage = "[图片]";
                }
                ArrayList<String> covName = new ArrayList<>();
                List<String> members = conversation.getMembers();
                for (String member: members) {
                    if (!member.equals(User.getCurrentUser().getUsername())){
                        covName.add(member);
                    }
                }
                ConversationItem conversationItem = dbmanager.queryConversationByUsername(covName.get(0));
                ContactInfo info = dbmanager.queryContactInfoByUsername(covName.get(0));
                if (conversationItem == null){
                    ConversationItem  item = new ConversationItem(null,conversation.getConversationId(),
                            1,message.getTimestamp(),info.getUsername(),info.getNickname(),info.getBeizhu()
                    ,lastMessage,info.getAvatarUrl());
                    dbmanager.insertConversation(item);
                }else {
                    conversationItem.setLastMessage(lastMessage);
                    conversationItem.setUpdateTime(message.getTimestamp());
                    conversationItem.setUnreadCount(conversationItem.getUnreadCount()+1);
                    dbmanager.updateConversation(conversationItem);
                }
                ReceiveMessageEvent event = new ReceiveMessageEvent(message,conversation);
                EventBus.getDefault().post(event);
            }
        });
    }
}
