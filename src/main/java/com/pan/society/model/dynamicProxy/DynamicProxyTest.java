package com.pan.society.model.dynamicProxy;

import com.pan.society.model.staticProxy.Bye;
import com.pan.society.model.staticProxy.ByeInterface;
import com.pan.society.model.staticProxy.Hello;
import com.pan.society.model.staticProxy.HelloInterface;

import java.lang.reflect.Proxy;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/9
 */
public class DynamicProxyTest {


    public static void main(String[] args) {

        System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        HelloInterface hello =new Hello();

        ByeInterface bye = new Bye();

        ProxyHandler helloProxy = new ProxyHandler(hello);
        ProxyHandler byeProxy = new ProxyHandler(bye);

        HelloInterface proxyHello = (HelloInterface)  Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), helloProxy);

        ByeInterface proxyBye = (ByeInterface)  Proxy.newProxyInstance(bye.getClass().getClassLoader(), bye.getClass().getInterfaces(), byeProxy);

        Class<? extends ByeInterface> aClass = bye.getClass();
        ClassLoader classLoader = aClass.getClassLoader();
        Class<?>[] interfaces = aClass.getInterfaces();

        proxyHello.sayHello();
        proxyBye.shoutBye();

    }



}
