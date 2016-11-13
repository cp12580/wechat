package com.aaron.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/10/23.
 */
@Entity
public class ContactInfo {
    @Id(autoincrement = true)
    public Long id;
    public String username;
    public String nickname;
    public String avatarUrl;
    public String isMale;
    public String region;
    public String sign;
    public String pinyin;
    public String beizhu;
    public String getBeizhu() {
        return this.beizhu;
    }
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
    public String getPinyin() {
        return this.pinyin;
    }
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    public String getSign() {
        return this.sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getRegion() {
        return this.region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getIsMale() {
        return this.isMale;
    }
    public void setIsMale(String isMale) {
        this.isMale = isMale;
    }
    public String getAvatarUrl() {
        return this.avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 286125645)
    public ContactInfo(Long id, String username, String nickname, String avatarUrl,
            String isMale, String region, String sign, String pinyin, String beizhu) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.isMale = isMale;
        this.region = region;
        this.sign = sign;
        this.pinyin = pinyin;
        this.beizhu = beizhu;
    }
    @Generated(hash = 2019856331)
    public ContactInfo() {
    }

}
