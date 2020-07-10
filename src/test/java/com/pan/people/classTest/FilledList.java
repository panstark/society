package com.pan.people.classTest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/6/30
 */

class CountedInteger{
    private static long counter;
    private final long id = counter++;
    public String toString(){
        return  Long.toString(id);
    }
}

class FilledListChild extends FilledList<CountedInteger>{

    private CountedInteger countedInteger;

    public FilledListChild(Class<CountedInteger> type) {
        super(type);
    }

    public FilledListChild(Class<CountedInteger> type, CountedInteger countedInteger) {
        super(type);
        this.countedInteger = countedInteger;
    }

    public void test(){
        System.out.println(null==countedInteger);
    }



}

public class FilledList<T> {
    private Class<T> type;

    public FilledList() {
    }

    public FilledList(Class<T> type) {
        this.type = type;
    }

    public List<T> createList(int nElement){
        List<T> result = new ArrayList<>();

        try {

            for (int i=0;i<nElement;i++) {
                result.add(type.newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        FilledListChild countedIntegerFilledList = new FilledListChild(CountedInteger.class);
        System.out.println(countedIntegerFilledList.createList(15));
        Class<?> clazz = countedIntegerFilledList.getClass();
        //Type entityType = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        countedIntegerFilledList.getEntityClass();
    }

    protected Class<?> getEntityClass() {
        Class<?> entityClass = null;
        Class<?> clazz = getClass();
        while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
        }
        Type entityType = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            entityClass =  Class.forName(((Class) entityType).getName());

        } catch (ClassNotFoundException e) {

        }
        return entityClass;
    }
}
