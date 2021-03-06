package com.aaron.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.aaron.dao.ContactInfo;
import com.aaron.dao.ConversationItem;

import com.aaron.dao.ContactInfoDao;
import com.aaron.dao.ConversationItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig contactInfoDaoConfig;
    private final DaoConfig conversationItemDaoConfig;

    private final ContactInfoDao contactInfoDao;
    private final ConversationItemDao conversationItemDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        contactInfoDaoConfig = daoConfigMap.get(ContactInfoDao.class).clone();
        contactInfoDaoConfig.initIdentityScope(type);

        conversationItemDaoConfig = daoConfigMap.get(ConversationItemDao.class).clone();
        conversationItemDaoConfig.initIdentityScope(type);

        contactInfoDao = new ContactInfoDao(contactInfoDaoConfig, this);
        conversationItemDao = new ConversationItemDao(conversationItemDaoConfig, this);

        registerDao(ContactInfo.class, contactInfoDao);
        registerDao(ConversationItem.class, conversationItemDao);
    }
    
    public void clear() {
        contactInfoDaoConfig.clearIdentityScope();
        conversationItemDaoConfig.clearIdentityScope();
    }

    public ContactInfoDao getContactInfoDao() {
        return contactInfoDao;
    }

    public ConversationItemDao getConversationItemDao() {
        return conversationItemDao;
    }

}
