package com.pan.society.model.staticProxy;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/9
 */
public class HelloTest {

    public static void main(String[] args) {
        HelloProxy helloProxy = new HelloProxy();

        helloProxy.sayHello();
    }
}
