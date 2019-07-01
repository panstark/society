package com.pan.society;

import com.pan.society.vo.Address;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    @Test
    public void iteratorTest(){
        Map<String, Address> mapName = new HashMap<>();

        for(int i=0;i<10;i++){
            Address a = new Address();
            a.setPostcode("code"+i);
            mapName.put("key"+i,a);
        }

        Iterator<Address> it = mapName.values().iterator();

        changeValue(it);

        for(Address value : mapName.values()){
            System.out.println(value);
        }
    }

    private void changeValue(Iterator<Address> it) {

        while(it.hasNext()){
            Address a = it.next();
            a.setPostcode(a.getPostcode()+"change");
        }

    }
}
