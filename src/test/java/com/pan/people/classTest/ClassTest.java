package com.pan.people.classTest;


/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/30
 */

interface hasBatteries {

}

interface waterproof {

}

interface shoots {

}

class Toy {

    Toy(){

    }

    Toy(int i){
        System.out.println("i"+i);
    }

}

class FancyToy extends Toy implements hasBatteries,waterproof,shoots{
    FancyToy(){
        super(1);
    }
}

public class ClassTest {

    static void printInfo(Class cc){
        System.out.println("ClassName:"+cc.getName()+",is interface:["+cc.isInterface()
        +"],simpleName:"+cc.getSimpleName()+",CanonicalName:"+cc.getCanonicalName());
    }

    public static void main(String[] args) {
        Class c = null;
        try {
            c = Class.forName("com.pan.people.classTest.FancyToy");
        } catch (ClassNotFoundException e) {
            System.out.println("can not find fancyToy.");
            e.printStackTrace();
        }
        printInfo(c);

        for (Class anInterface : c.getInterfaces()) {
            printInfo(anInterface);
        }

        Class up = c.getSuperclass();
        Object obj = null;
        try {
            obj = up.newInstance();
        } catch (InstantiationException e) {
            System.out.println("cannot instantiate.");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("cannot access.");
            e.printStackTrace();
        }

        printInfo(obj.getClass());

    }



}
