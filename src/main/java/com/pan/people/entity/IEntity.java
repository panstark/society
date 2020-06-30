package com.pan.people.entity;

import java.util.Date;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/29
 */
public interface IEntity {

    String getId();

    void setId(String id);

    Integer getDr();

    void setDr(Integer dr);

    Date getTs();

    void setTs(Date ts);

    String getCreator();

    void setCreator(String creator);

    Date getCreateTime();

    void setCreateTime(Date createTime);

    String getModifier();

    void setModifier(String modifier);

    Date getModifyTime();

    void setModifyTime(Date modifyTime);

    String getPersistStatus();

    void setPersistStatus(String persistStatus);
}
