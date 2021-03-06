package com.pan.society;

import com.pan.society.vo.Address;
import com.pan.society.vo.car.AudiCar;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.*;

/**
 * create by panstark
 * create date 2019/6/11
 */
public class listTest {

    @Test
    public void testElement(){
        List<String> strings = new ArrayList<>();
        String abc = new String("abc");
        String bcd = new String("bcd");
        String cdf = new String("cdf");
        strings.add(abc);
        strings.add(bcd);
        strings.add(cdf);

        String abc1 = strings.get(0);
        abc = "what";
        String abc2 = strings.get(0);


        strings.stream().forEach(e->{
            System.out.println(e);
        });
        System.out.println(abc1);
        System.out.println(abc);
        System.out.println(abc2);
        System.out.println(abc2==abc1);
    }

    @Test
    public void testAddress(){
        List<Address> strings = new ArrayList<>();
        Address abc = new Address("abc","1","2");
        Address bcd = new Address("bcd","1","2");
        Address cdf = new Address("cdf","1","2");
        strings.add(abc);
        strings.add(bcd);
        strings.add(cdf);

        Address abc1 = strings.get(0);
        abc = new Address("eee","1","2");
        Address abc2 = strings.get(0);
        abc2.setPostcode("wowow");


        strings.stream().forEach(e->{
            System.out.println(e);
        });
        System.out.println(abc1);
        System.out.println(abc);
        System.out.println(abc2);
        System.out.println(abc2==abc1);
    }

    @Test
    public void testRemove(){
        List<Address> addresses = new ArrayList<>();
        Address abc = new Address("abc","1","2");
        Address bcd = new Address("bcd","1","2");
        Address cdf = new Address("cdf","1","2");
        addresses.add(abc);
        addresses.add(bcd);
        addresses.add(cdf);

        for (Address one:addresses) {
            if ("cdf".equals(one.getPostcode())){
                addresses.remove(one);
            }
        }
    }

    @Test
    public void addEle(){
        List<Address> addresses = new ArrayList<>();
        putValue(addresses);

        addresses.stream().forEach(e->{
            System.out.println(e);
        });
    }

    private void putValue(List<Address> addresses) {
        Address abc = new Address("abc","1","2");
        Address bcd = new Address("bcd","1","2");
        Address cdf = new Address("cdf","1","2");
        addresses.add(abc);
        addresses.add(bcd);
        addresses.add(cdf);

        addresses = changeList(addresses);
    }

    private List<Address> changeList(List<Address> addresses) {
        Address dfg = new Address("cdf","1","2");
        addresses.add(dfg);
        addresses.get(0).setPostcode("0000");
        return addresses;
    }

    @Test
    public void iterateList(){
        List<Address> addresses = new ArrayList<>();
        putValue(addresses);
        Iterator<Address> it = addresses.iterator();
        while(it.hasNext()){
            Address address = it.next();
            List<String> list = address.getCountryList();
            for(int i=0;i<10;i++){
                list.add("row"+i);
                if(list.size()>5){
                    list.add("555"+5);
                    break;
                }
            }
        }

        addresses.stream().forEach(e->{
            System.out.println(e);
        });
    }

    @Test
    public void listAddSb(){
        List<String> errMsg = new ArrayList<>();
        for(int i=0;i<10;i++){
            StringBuilder sb = new StringBuilder();
            if(i%2==0){
                sb.append(i+"加进来");
            }
            sb.insert(0,"wowo");
            if(StringUtils.isNotBlank(sb.toString())){
                errMsg.add(sb.toString());
            }
        }

        System.out.println(errMsg.size());
        errMsg.stream().forEach(e->{
            System.out.println(e);
        });
        System.out.println(String.join("。",errMsg));
    }

    @Test
    public void hashSetPut(){
        Set set = new HashSet();
        set.add(null);
        set.add("");
        System.out.println("size:"+set.size());
    }

}
