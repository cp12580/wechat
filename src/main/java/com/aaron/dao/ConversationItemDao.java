package com.aaron.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONVERSATION_ITEM".
*/
public class ConversationItemDao extends AbstractDao<ConversationItem, Long> {

    public static final String TABLENAME = "CONVERSATION_ITEM";

    /**
     * Properties of entity ConversationItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ConversationId = new Property(1, String.class, "conversationId", false, "CONVERSATION_ID");
        public final static Property UnreadCount = new Property(2, int.class, "unreadCount", false, "UNREAD_COUNT");
        public final static Property UpdateTime = new Property(3, long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property Username = new Property(4, String.class, "username", false, "USERNAME");
        public final static Property Nickname = new Property(5, String.class, "nickname", false, "NICKNAME");
        public final static Property Beizhu = new Property(6, String.class, "beizhu", false, "BEIZHU");
        public final static Property LastMessage = new Property(7, String.class, "lastMessage", false, "LAST_MESSAGE");
        public final static Property AvatarUrl = new Property(8, String.class, "avatarUrl", false, "AVATAR_URL");
    }


    public ConversationItemDao(DaoConfig config) {
        super(config);
    }
    
    public ConversationItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONVERSATION_ITEM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CONVERSATION_ID\" TEXT," + // 1: conversationId
                "\"UNREAD_COUNT\" INTEGER NOT NULL ," + // 2: unreadCount
                "\"UPDATE_TIME\" INTEGER NOT NULL ," + // 3: updateTime
                "\"USERNAME\" TEXT," + // 4: username
                "\"NICKNAME\" TEXT," + // 5: nickname
                "\"BEIZHU\" TEXT," + // 6: beizhu
                "\"LAST_MESSAGE\" TEXT," + // 7: lastMessage
                "\"AVATAR_URL\" TEXT);"); // 8: avatarUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONVERSATION_ITEM\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ConversationItem entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String conversationId = entity.getConversationId();
        if (conversationId != null) {
            stmt.bindString(2, conversationId);
        }
        stmt.bindLong(3, entity.getUnreadCount());
        stmt.bindLong(4, entity.getUpdateTime());
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(6, nickname);
        }
 
        String beizhu = entity.getBeizhu();
        if (beizhu != null) {
            stmt.bindString(7, beizhu);
        }
 
        String lastMessage = entity.getLastMessage();
        if (lastMessage != null) {
            stmt.bindString(8, lastMessage);
        }
 
        String avatarUrl = entity.getAvatarUrl();
        if (avatarUrl != null) {
            stmt.bindString(9, avatarUrl);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ConversationItem entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String conversationId = entity.getConversationId();
        if (conversationId != null) {
            stmt.bindString(2, conversationId);
        }
        stmt.bindLong(3, entity.getUnreadCount());
        stmt.bindLong(4, entity.getUpdateTime());
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(6, nickname);
        }
 
        String beizhu = entity.getBeizhu();
        if (beizhu != null) {
            stmt.bindString(7, beizhu);
        }
 
        String lastMessage = entity.getLastMessage();
        if (lastMessage != null) {
            stmt.bindString(8, lastMessage);
        }
 
        String avatarUrl = entity.getAvatarUrl();
        if (avatarUrl != null) {
            stmt.bindString(9, avatarUrl);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ConversationItem readEntity(Cursor cursor, int offset) {
        ConversationItem entity = new ConversationItem( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // conversationId
            cursor.getInt(offset + 2), // unreadCount
            cursor.getLong(offset + 3), // updateTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // username
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // nickname
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // beizhu
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // lastMessage
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // avatarUrl
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ConversationItem entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setConversationId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUnreadCount(cursor.getInt(offset + 2));
        entity.setUpdateTime(cursor.getLong(offset + 3));
        entity.setUsername(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNickname(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setBeizhu(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLastMessage(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAvatarUrl(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ConversationItem entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ConversationItem entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ConversationItem entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
