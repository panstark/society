package com.pan.society.model.staticProxy;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/9
 */
public class Hello implements HelloInterface {
    @Override
    public void sayHello() {
        System.out.println("hello what's your name？");
    }

}
