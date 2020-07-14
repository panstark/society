package com.pan.society.know.design.pattern.behavioral.interpreter;

/**
 * Created by geely.
 * 解释器模式
 *
 * 对字符串进行解释
 *
 */
public class Test {

    public static void main(String[] args) {

        String geelyInputStr="6 100 11 1 + * -";

        GeelyExpressionParser expressionParser=new GeelyExpressionParser();

        int result=expressionParser.parse(geelyInputStr);

        System.out.println("解释器计算结果: "+result);

    }
}
