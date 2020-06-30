package com.pan.people.entity;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/29
 */
public enum DrEnum {

    NORMAL(0,"正常"),
    DEL(1,"删除")
    ;

    private Integer code;
    private String description;


    DrEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
