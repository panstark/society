package com.pan.society.model.staticProxy;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/9
 */
public class HelloProxy implements HelloInterface{

    Hello hello = new Hello();

    @Override
    public void sayHello() {
        System.out.println("before hello say hello.");

        hello.sayHello();

        System.out.println("after hello say hello");
    }

}
