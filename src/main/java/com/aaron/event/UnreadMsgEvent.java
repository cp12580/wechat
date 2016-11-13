package com.aaron.event;

/**
 * Created by Administrator on 2016/10/28.
 */
public class UnreadMsgEvent {
    public int unreadMsgCount;
    public UnreadMsgEvent(int count) {
        unreadMsgCount = count;
    }
}
