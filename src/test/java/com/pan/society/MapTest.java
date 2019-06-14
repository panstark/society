package com.pan.society;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * create by panstark
 * create date 2019/6/14
 */
public class MapTest {

    @Test
    public void MultiKeyMapTest(){

        MultiKeyMap multiKeyMap = new MultiKeyMap();

        //SmultiKeyMap.put(,"stark");



    }

    @Test
    public void MultiValueMapTest(){

        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("01","stark1");
        multiValueMap.add("01","stark2");
        multiValueMap.add("01","stark3");
        multiValueMap.add("01","stark4");
        List valueList = multiValueMap.get("01");
        System.out.println(valueList.size());

        valueList.stream().forEach(e->{
            System.out.println(e);
        });


    }
}
