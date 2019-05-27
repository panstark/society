package com.pan.society.Common;

import com.yonyou.ocm.common.config.ExtensionRegister;

/**
 * 类构造器
 *
 * 当类被扩展后，可以构造出扩展类，避免基础工程中的功能无法返回扩展内容，
 * 凡是需要new新建的类entity或dto都以此工具来构建
 * @author gaopengf
 * @date 2019-05-13 10:02:26
 */
public class BeanConstructor {

    public static <T> T construct(Class<? extends T> clazz){

        if (ExtensionRegister.hasExt(clazz)){
            try {
                return (T) ExtensionRegister.getTopExt(clazz).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
