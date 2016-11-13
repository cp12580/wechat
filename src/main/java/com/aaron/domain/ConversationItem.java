package com.aaron.domain;

import com.aaron.utils.Util;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2016/10/20.
 */
class ConversationItem implements Comparable {
    private static final String ITEM_KEY_CONVCERSATION_ID = "conversation_id";
    private static final String ITEM_KEY_UNREADCOUNT = "unreadcount";
    private static final String ITEM_KEY_UNDATE_TIME = "upadte_time";
    public String conversationId = "";
    public int unreadCount = 0;
    public long updateTime = 0;

    public ConversationItem() {
    }

    public ConversationItem(String conversationId) {
        this.conversationId = conversationId;
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ITEM_KEY_CONVCERSATION_ID, conversationId);
        jsonObject.put(ITEM_KEY_UNREADCOUNT, unreadCount);
        jsonObject.put(ITEM_KEY_UNDATE_TIME, updateTime);
        return jsonObject.toJSONString();
    }

    public static ConversationItem fromJsonString(String json) {
        ConversationItem item = new ConversationItem();
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(json);
            item.conversationId = jsonObject.getString(ITEM_KEY_CONVCERSATION_ID);
            item.unreadCount = jsonObject.getInteger(ITEM_KEY_UNREADCOUNT);
            item.updateTime = jsonObject.getLong(ITEM_KEY_UNDATE_TIME);
        } catch (Exception e) {
            Util.L(e.getMessage());
        }
        return item;
    }

    @Override
    public int compareTo(Object another) {
        return (int) (((ConversationItem) another).updateTime - updateTime);
    }
}
