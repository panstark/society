package com.pan.society.calculate;


import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/5
 */
public class CalculateUtil {

    public static List<BigDecimal> stepReduce(BigDecimal deduction, BigDecimal step){

        Assert.notNull(deduction,"冲抵比例不能为空。");
        Assert.notNull(step,"冲抵比例阶梯不能为空。");
        Assert.state(BigDecimal.ZERO.compareTo(deduction)<=0,"冲抵比例需要为正数");

        List<BigDecimal> stepList = new ArrayList<>();
        stepList.add(deduction);

        BigDecimal[] bigDecimals = deduction.divideAndRemainder(step);
        System.out.println("bigDecimals0:"+bigDecimals[0]+",bigDecimals1:"+bigDecimals[1]);
        BigDecimal subtract = deduction.subtract(bigDecimals[1]);
        while(BigDecimal.ZERO.compareTo(subtract)<0){
            subtract = subtract.subtract(step);
            stepList.add(subtract);
        }

        return stepList;
    }


    public static void main(String[] args) {
//        BigDecimal deduction = new BigDecimal(-97);
//        BigDecimal step = new BigDecimal(5);
//
//        List<BigDecimal> stepList = stepReduce(deduction,step);
//
//        System.out.println(stepList.size());

        List<String> nums = new ArrayList<>();
        for(int i=0;i<10;i++){
            nums.add(String.valueOf(i));
        }

        removeNums(nums);

        System.out.println(nums.size());
    }

    private static void removeNums(List<String> nums) {

        Iterator<String> iterator = nums.iterator();
        while(iterator.hasNext()){
            String num = iterator.next();
            if(Integer.valueOf(num)%2==0){
                iterator.remove();
            }
        }

    }
}
