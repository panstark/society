package com.pan.society;

import com.pan.society.anno.AnnoMethod;
import com.pan.society.anno.Display;
import com.pan.society.anno.GoodPeople;
import com.pan.society.vo.car.AudiCar;
import com.pan.society.vo.human.Lisi;
import com.pan.society.vo.human.People;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * create by panstark
 * create date 2019/5/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class annoTest {

     @Autowired
     People people;

    @Test
    public void howToUseAnno(){

        Annotation[] annos = Lisi.class.getAnnotations();

        for (Annotation anno:annos) {
            System.out.println(anno.toString());
            System.out.println(GoodPeople.class.toString());
        }
    }

    @Test
    public void displayTest(){

        Field[] fields = Lisi.class.getFields();

        for (Field field:fields){
            if (field.isAnnotationPresent(Display.class)){
                Display display = field.getAnnotation(Display.class);
                System.out.println("字段:"+field.getName()+",value:"+display.value()+display.toString());
            }
        }

    }

    @Test
    public void annoMethodTest(){
        Lisi lisi = new Lisi(new AudiCar());
        Method[] mds = Lisi.class.getMethods();

        try {
            for (Method md : mds){
                if (md.isAnnotationPresent(AnnoMethod.class)){
                    AnnoMethod amd = md.getAnnotation(AnnoMethod.class);
                    md.invoke(lisi);

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void configurationTest(){
        System.out.println(people.toString());
    }
}
