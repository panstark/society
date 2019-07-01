package com.pan.society.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * create by panstark
 * create date 2019/5/9
 */
@Data
public class Address {

    public Address(){

    }

    public Address(String postcode, String street, String state) {
        this.postcode = postcode;
        this.street = street;
        this.state = state;
    }

    public String postcode;

    private String street;

    private String state;

    private List<String> countryList = new ArrayList<>();

}
