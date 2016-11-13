package com.aaron.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;

/**
 * Created by Administrator on 2016/10/19.
 */
public class ReceiveMessageEvent {
    public AVIMMessage message;
    public AVIMConversation conversation;
    public ReceiveMessageEvent(AVIMMessage msg,AVIMConversation conver){
        message = msg;
        conversation = conver;
    }
}
