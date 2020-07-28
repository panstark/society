package com.pan.society.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.mvel2.util.StringAppender;

/**
 * create by panstark
 * create date 2019/5/27
 */
public class StringUtil {

    /**
     * 对字符串数组进行自然排序
     * @param input
     * @return
     */
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

    /**
     *
     */
    public static void StringJoin(){

        String[] strings = new String[10];
        for(int i=0;i<10;i++){
            strings[i] = "a"+i;
        }
        System.out.println("'"+StringUtils.join(strings,"','")+"'");
    }

    public static void main(String[] args) {
        StringJoin();
    }
}
