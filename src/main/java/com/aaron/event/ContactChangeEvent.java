package com.aaron.event;

import com.aaron.dao.ContactInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ContactChangeEvent {
    public List<ContactInfo> infos;
    public ContactChangeEvent(List<ContactInfo> infos){
        this.infos = infos;
    }
}
