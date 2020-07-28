package com.pan.society;

import com.pan.society.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * create by panstark
 * create date 2019/5/27
 */
@Slf4j
public class StringTest {

    public static void main(String[] args) {
        String[] input=new String[] {"tmc","apad","apache","nihao","nihaoa"};
        String[] keys=arraySort(input);
        for (String key : keys) {
            System.out.println(key);
        }
        System.out.println(Arrays.stream(keys).collect(Collectors.joining(",")));
    }

    public static String[] arraySort(String[] input){
        for (int i=0;i<input.length-1;i++){
            for (int j=0;j<input.length-i-1;j++) {
                if(input[j].compareTo(input[j+1])>0){
                    String temp=input[j];
                    input[j]=input[j+1];
                    input[j+1]=temp;
                }
            }
        }
        return input;
    }


    @Test
    public void addString(){
        String littleName = "pan";
        String name = "stark"+ (StringUtils.isNotBlank(littleName)?littleName:"");
        System.out.println(name);

    }

    @Test
    public void addBigDecimal(){
        BigDecimal num = new BigDecimal(100);

        String name = "stark"+ num;
        System.out.println(name);

    }

    @Test
    public void stringNull(){
        String a = "";
        log.info("qq"+String.format("%s",a));

        Object bb = null;
        Map<String,String> cc = (Map<String,String>)bb;
        System.out.println(cc);
    }

    @Test
    public void stringLength(){
        String a = "你我";
        System.out.println("length："+a.length());

        String b = null;
        boolean bl = StringUtils.isNotBlank(b)&&b.length()>50;
        System.out.println(bl);
    }

}
