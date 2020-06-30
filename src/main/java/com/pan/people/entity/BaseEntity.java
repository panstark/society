package com.pan.people.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pan.people.enums.PersistStatus;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/29
 */
public abstract class BaseEntity implements Serializable ,IEntity{

    protected String id;

    protected Integer dr = DrEnum.NORMAL.getCode();

    protected Date ts;

    protected String creator;

    protected Date createTime;

    protected String modifier;

    protected Date modifyTime;

    protected String persistStatus = PersistStatus.ADDED;

    /**
     * 数据库中的原值。
     */
    @Transient
    @JsonIgnore
    protected BaseEntity oldValue;


    public BaseEntity getOldValue() {
        return oldValue;
    }

    public void setOldValue(BaseEntity oldValue) {
        this.oldValue = oldValue;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Integer getDr() {
        return dr;
    }

    @Override
    public void setDr(Integer dr) {
        this.dr = dr;
    }

    @Override
    public Date getTs() {
        return ts;
    }

    @Override
    public void setTs(Date ts) {
        this.ts = ts;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public String getPersistStatus() {
        return persistStatus;
    }

    @Override
    public void setPersistStatus(String persistStatus) {
        this.persistStatus = persistStatus;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getModifyTime() {
        return modifyTime;
    }

    @Override
    public void setModifyTime(Date modifiedTime) {
        this.modifyTime = modifiedTime;
    }


}
