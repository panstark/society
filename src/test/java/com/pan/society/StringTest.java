package com.pan.society;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * create by panstark
 * create date 2019/5/27
 */
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

}
