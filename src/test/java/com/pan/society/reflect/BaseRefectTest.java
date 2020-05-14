package com.pan.society.reflect;

import com.pan.society.anno.Display;
import com.pan.society.vo.human.Lisi;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/11
 */
public class BaseRefectTest {

    @Test
    public void baseReflectTest() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Lisi lisi = new Lisi();
        Class<? extends Lisi> lisiClass = lisi.getClass();
        Field[] declaredFields = lisiClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.toString());
        }

        Field name = lisiClass.getDeclaredField("name");
        System.out.println("fieldName:"+name);

        Annotation[] annotations = lisiClass.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("annotation:"+annotation.toString()+"annotationType:"+annotation.annotationType());
        }

        Method[] methods = lisiClass.getMethods();
        for (Method method : methods) {
            System.out.println("method:"+method.toString());
        }


        ClassLoader classLoader = lisiClass.getClassLoader();
        System.out.println(classLoader.toString());

        Lisi lisi1 = lisiClass.newInstance();
        lisi1.setName("zhangsan");
        System.out.println(lisi1.toString());

    }

    @Test
    public void setFeildTest() throws InvocationTargetException, IllegalAccessException {
        Lisi lisi = new Lisi();
        Method[] methods = lisi.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Override annotation = method.getAnnotation(Override.class);
            if(null!=annotation){
                method.invoke(lisi);
            }
        }
    }

    public void codeTest(){

    }

}
