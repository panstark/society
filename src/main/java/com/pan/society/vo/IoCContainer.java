package com.pan.society.vo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by panstark
 * create date 2019/5/5
 * 一个container的作用
 * 实例化bean
 * 保存bean
 * 提供bean
 *
 * 每一个bean要有一个对应的唯一id
 */
public class IoCContainer {

    private Map<String,Object> beans = new ConcurrentHashMap<>();

    public Object getBean(String beanId){

        return beans.get(beanId);
    }

    /**
     * 委托IOC创建一个bean
     * @param clazz
     * @param beanId
     * @param paramBeanIds
     */
    public void setBeans(Class<?> clazz,String beanId,String... paramBeanIds){
        //1、组装构造方法所需的参数值
        Object[] paramValues = new Object[paramBeanIds.length];
        for(int i=0;i<paramBeanIds.length;i++){
            paramValues[i] = beans.get(paramBeanIds[i]);
        }

        //2、调用构造方法实例化bean
        Object bean = null;
        for(Constructor<?> constructor: clazz.getConstructors()){
            try {
                bean = constructor.newInstance(paramValues);
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }

            if(bean ==null){
                throw new RuntimeException("找不到合适的方法实例化bean");
            }
        }

        //3、将实例化的bean放入beans中
        beans.put(beanId,bean);

    }

}
