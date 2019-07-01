package com.pan.society;

import org.junit.Test;

/**
 * create by panstark
 * create date 2019/6/25
 */
public class BooleanTest {

    @Test
    public void changeBooleanTest(){
        Boolean change = false;
        changeBoolean(change);
        System.out.println(change);
    }

    private void changeBoolean(Boolean change) {
        change = true;
    }
}
