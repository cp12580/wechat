package com.aaron.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/10/20.
 */
@Entity
public class ConversationItem {
    @Id(autoincrement = true)
    public Long id;
    public String conversationId ;
    public int unreadCount;
    public long updateTime;
    public String username;
    public String nickname;
    public String beizhu;
    public String lastMessage;
    public String avatarUrl;
    public String getAvatarUrl() {
        return this.avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getLastMessage() {
        return this.lastMessage;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public long getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    public int getUnreadCount() {
        return this.unreadCount;
    }
    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
    public String getConversationId() {
        return this.conversationId;
    }
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBeizhu() {
        return this.beizhu;
    }
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
    @Generated(hash = 781338851)
    public ConversationItem(Long id, String conversationId, int unreadCount,
            long updateTime, String username, String nickname, String beizhu,
            String lastMessage, String avatarUrl) {
        this.id = id;
        this.conversationId = conversationId;
        this.unreadCount = unreadCount;
        this.updateTime = updateTime;
        this.username = username;
        this.nickname = nickname;
        this.beizhu = beizhu;
        this.lastMessage = lastMessage;
        this.avatarUrl = avatarUrl;
    }
    @Generated(hash = 788519751)
    public ConversationItem() {
    }
}
