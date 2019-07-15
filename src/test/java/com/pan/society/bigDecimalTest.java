package com.pan.society;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * create by panstark
 * create date 2019/7/4
 */
public class bigDecimalTest {

    @Test
    public void bigDecimalAdd(){
        BigDecimal quantity = new BigDecimal(0);
        BigDecimal weight = new BigDecimal(0);
        BigDecimal valume = new BigDecimal(0);
        StringBuilder sb =  new StringBuilder();

        addSomeNum(quantity,weight,valume,sb);
        System.out.println("quantity:"+quantity.toString()+",sb:"+sb.toString());


    }

    private void addSomeNum(BigDecimal quantity, BigDecimal weight, BigDecimal valume, StringBuilder sb) {
        BigDecimal totalOrderNum = new BigDecimal(1);
        quantity = quantity.add(totalOrderNum);
        sb.append("wowo").append(",");
    }


}
