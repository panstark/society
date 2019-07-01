package com.pan.society;

import com.pan.society.vo.Address;
import org.junit.Test;

/**
 * create by panstark
 * create date 2019/6/27
 */
public class ClassTest {

    @Test
    public void getClassTest(){
        Address ass = new Address();
        System.out.println(ass.getClass()+":"+Address.class+":"+ass.getClass().equals(Address.class));
    }
}
