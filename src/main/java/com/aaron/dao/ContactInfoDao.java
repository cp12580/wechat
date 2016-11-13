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
 * DAO for table "CONTACT_INFO".
*/
public class ContactInfoDao extends AbstractDao<ContactInfo, Long> {

    public static final String TABLENAME = "CONTACT_INFO";

    /**
     * Properties of entity ContactInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Username = new Property(1, String.class, "username", false, "USERNAME");
        public final static Property Nickname = new Property(2, String.class, "nickname", false, "NICKNAME");
        public final static Property AvatarUrl = new Property(3, String.class, "avatarUrl", false, "AVATAR_URL");
        public final static Property IsMale = new Property(4, String.class, "isMale", false, "IS_MALE");
        public final static Property Region = new Property(5, String.class, "region", false, "REGION");
        public final static Property Sign = new Property(6, String.class, "sign", false, "SIGN");
        public final static Property Pinyin = new Property(7, String.class, "pinyin", false, "PINYIN");
        public final static Property Beizhu = new Property(8, String.class, "beizhu", false, "BEIZHU");
    }


    public ContactInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ContactInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACT_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USERNAME\" TEXT," + // 1: username
                "\"NICKNAME\" TEXT," + // 2: nickname
                "\"AVATAR_URL\" TEXT," + // 3: avatarUrl
                "\"IS_MALE\" TEXT," + // 4: isMale
                "\"REGION\" TEXT," + // 5: region
                "\"SIGN\" TEXT," + // 6: sign
                "\"PINYIN\" TEXT," + // 7: pinyin
                "\"BEIZHU\" TEXT);"); // 8: beizhu
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACT_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ContactInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(2, username);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
 
        String avatarUrl = entity.getAvatarUrl();
        if (avatarUrl != null) {
            stmt.bindString(4, avatarUrl);
        }
 
        String isMale = entity.getIsMale();
        if (isMale != null) {
            stmt.bindString(5, isMale);
        }
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(6, region);
        }
 
        String sign = entity.getSign();
        if (sign != null) {
            stmt.bindString(7, sign);
        }
 
        String pinyin = entity.getPinyin();
        if (pinyin != null) {
            stmt.bindString(8, pinyin);
        }
 
        String beizhu = entity.getBeizhu();
        if (beizhu != null) {
            stmt.bindString(9, beizhu);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ContactInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(2, username);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
 
        String avatarUrl = entity.getAvatarUrl();
        if (avatarUrl != null) {
            stmt.bindString(4, avatarUrl);
        }
 
        String isMale = entity.getIsMale();
        if (isMale != null) {
            stmt.bindString(5, isMale);
        }
 
        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(6, region);
        }
 
        String sign = entity.getSign();
        if (sign != null) {
            stmt.bindString(7, sign);
        }
 
        String pinyin = entity.getPinyin();
        if (pinyin != null) {
            stmt.bindString(8, pinyin);
        }
 
        String beizhu = entity.getBeizhu();
        if (beizhu != null) {
            stmt.bindString(9, beizhu);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ContactInfo readEntity(Cursor cursor, int offset) {
        ContactInfo entity = new ContactInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // username
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nickname
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // avatarUrl
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // isMale
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // region
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // sign
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // pinyin
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // beizhu
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ContactInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUsername(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAvatarUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIsMale(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRegion(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSign(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPinyin(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBeizhu(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ContactInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ContactInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ContactInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
