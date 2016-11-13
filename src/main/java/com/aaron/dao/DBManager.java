package com.aaron.dao;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */
public class DBManager{
    private DaoSession daoSession;
    private static DBManager dbManager;
    private static String mUsername;

    public DBManager(Context context,String username){
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context,
                username + ".db",null);
        DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        mUsername = username;
    }

    public static DBManager getDbManager(Context context,String username){
        if (dbManager == null || username != mUsername){//切换账号时更换数据库
            synchronized (DBManager.class){
                if (dbManager == null || username != mUsername){
                    dbManager = new DBManager(context,username);
                }
            }
        }
        return dbManager;
    }

    public synchronized void insertConversation(ConversationItem conversationItem){
        ConversationItemDao conversationItemDao = daoSession.getConversationItemDao();
        conversationItemDao.insert(conversationItem);
    }
    public synchronized void deleteConversation(ConversationItem conversationItem){
        ConversationItemDao conversationItemDao = daoSession.getConversationItemDao();
        conversationItemDao.delete(conversationItem);
    }
    public synchronized void updateConversation(ConversationItem conversationItem){
        ConversationItemDao conversationItemDao = daoSession.getConversationItemDao();
        conversationItemDao.update(conversationItem);
    }

    public List<ConversationItem> queryConversation(){
        QueryBuilder<ConversationItem> query = daoSession.queryBuilder(ConversationItem.class);
        query.orderDesc(ConversationItemDao.Properties.UpdateTime);
        List<ConversationItem> list = query.list();
        return list;
    }
    public  ConversationItem queryConversationById(String covId){
        QueryBuilder<ConversationItem> query = daoSession.queryBuilder(ConversationItem.class);
        query.where(ConversationItemDao.Properties.ConversationId.eq(covId));
        return query.unique();
    }
    public  ConversationItem queryConversationByUsername(String username){
        QueryBuilder<ConversationItem> query = daoSession.queryBuilder(ConversationItem.class);
        query.where(ConversationItemDao.Properties.Username.eq(username));
        return query.unique();
    }

    //on Contactinfo table
    public  ContactInfo queryContactInfoByUsername(String username){
        QueryBuilder<ContactInfo> query = daoSession.queryBuilder(ContactInfo.class);
        query.where(ContactInfoDao.Properties.Username.eq(username));
        return query.unique();
    }

    public List<ContactInfo> queryContactInfo(){
        QueryBuilder<ContactInfo> query = daoSession.queryBuilder(ContactInfo.class);
        List<ContactInfo> list = query.list();
        return list;
    }

    public synchronized void insertContactInfo(ContactInfo contactInfo){
        ContactInfoDao dao = daoSession.getContactInfoDao();
        dao.insert(contactInfo);
    }
    public synchronized void deleteContactInfo(ContactInfo contactInfo){
        ContactInfoDao dao = daoSession.getContactInfoDao();
        dao.delete(contactInfo);
    }
    public synchronized void updateContactInfo(ContactInfo contactInfo){
        ContactInfoDao dao = daoSession.getContactInfoDao();
        dao.update(contactInfo);
    }


}
