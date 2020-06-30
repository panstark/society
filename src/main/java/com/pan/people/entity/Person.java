package com.pan.people.entity;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/29
 */
public class Person extends BaseEntity{

    private String idCardNum;

    private String name;

    private String gender;

    private Integer age;

    private String phoneNum;

    private String hight;

    private String weight;

    public Person(String idCardNum, String name, String phoneNum) {
        this.idCardNum = idCardNum;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getHight() {
        return hight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
