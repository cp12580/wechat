package com.aaron.domain;


/**
 * Created by Administrator on 2016/10/11.
 */
public class AddRequestManager {
    /**
     * 用户端未读的邀请消息的数量
     */
    private int unreadAddRequestsCount = 0;

    private static AddRequestManager addRequestManager;
    private AddRequestManager(){}
    public static AddRequestManager getInstance(){
        if (addRequestManager == null){
            synchronized (AddRequestManager.class){
                addRequestManager = new AddRequestManager();
            }
        }
        return addRequestManager;
    }
    /**
     * 是否有未读的消息
     */
    public boolean hasUnreadRequests() {
        return unreadAddRequestsCount > 0;
    }
    /**
     * 推送过来时自增
     */
    public void unreadRequestsIncrement() {
        ++ unreadAddRequestsCount;
    }

    public void sendAddRequest(){

    }
}
