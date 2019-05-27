package com.pan.society.Common;


import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 反射工具类
 *
 * @author gaopengf
 * @description 导入导出Excel
 * @date 2017-08-02
 */
public class ReflectUtils {
    private final static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    /**
     * 设置对象属性值
     *
     * @param obj       实体对象
     * @param fieldName 属性名
     * @param value     属性值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws ParseException
     * @throws Exception
     */
    public static void setProperty(Object obj, String fieldName, Object value) throws Exception {

        Field field = getFieldName(obj, fieldName);
        if (field == null) {
            logger.error(fieldName + ":获取对象属性值失败");
            throw new RuntimeException(fieldName + ":获取对象属性值失败");
        }
        if (field != null) {
            Class<?> fieldType = field.getType();
            field.setAccessible(true);
            // 根据字段类型给字段赋值
            if (String.class == fieldType) {
                if (value == null || value == "") {
                    value = "";
                }
                field.set(obj, String.valueOf(value));
            } else if ((Integer.TYPE == fieldType)
                    || (Integer.class == fieldType)) {
                field.set(obj, Integer.parseInt(value.toString()));
            } else if ((Long.TYPE == fieldType)
                    || (Long.class == fieldType)) {
                field.set(obj, Long.valueOf(value.toString()));
            } else if ((Float.TYPE == fieldType)
                    || (Float.class == fieldType)) {
                field.set(obj, Float.valueOf(value.toString()));
            } else if ((Short.TYPE == fieldType)
                    || (Short.class == fieldType)) {
                field.set(obj, Short.valueOf(value.toString()));
            } else if ((Double.TYPE == fieldType)
                    || (Double.class == fieldType)) {
                field.set(obj, Double.valueOf(value.toString()));
            } else if (Character.TYPE == fieldType) {
                if ((value != null) && (value.toString().length() > 0)) {
                    field.set(obj,
                            Character.valueOf(value.toString().charAt(0)));
                }
            } else if (Date.class == fieldType) {
                if (value instanceof Date) {
                    field.set(obj, value);
                } else if (value instanceof String) {
                    if (((String) value).length() < 19) {
                        field.set(obj, new SimpleDateFormat(
                                "yyyy-MM-dd").parse(value.toString()));
                    } else {

                        field.set(obj, new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss").parse(value.toString()));
                    }
                }
            } else if (BigDecimal.class == fieldType) {
                if (value != null) {
                    field.set(obj, new BigDecimal(value.toString().replace(",","")));
                } else {
                    field.set(obj, value);
                }
            } else {
                field.set(obj, value);
            }
            field.setAccessible(false);
        }

    }

    /**
     * 获取对象属性值
     *
     * @param obj       实体对象
     * @param fieldName 属性名
     * @return Object
     * 属性值
     * @throws Exception
     */
    public static Object getProperty(Object obj, String fieldName) throws Exception {
        Field field = getFieldName(obj, fieldName);
        if (field == null) {
            logger.error(fieldName + ":获取对象属性值失败");
            throw new RuntimeException(fieldName + ":获取对象属性值失败");
        }
        Object value = null;
        try {
            if (field != null) {
                Class<?> fieldType = field.getType();
                field.setAccessible(true);
                // 根据字段类型给字段赋值
                if (Date.class == fieldType) {
                    Object o = field.get(obj);
                    if (o != null) {
                        value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(o);
                    }
                } else {
                    value = field.get(obj);
                }
                field.setAccessible(false);
            }
        } catch (Exception e) {
        	logger.error("获取对象属性值失败",e);
            throw e;
        }
        return value;
    }

    /**
     * 获取对象嵌套属性值
     *
     * @param obj       实体对象
     * @param fieldName 属性名
     * @return Object
     * 属性值
     * @throws Exception
     */
    public static Object getNestedProperty(Object obj, String fieldName) throws Exception {
        Object value = null;
        String[] attributes = fieldName.split("\\.");
        try {
            value = getProperty(obj, attributes[0]);
            for (int i = 1; i < attributes.length; i++) {
                value = getProperty(value, attributes[i]);
            }
        } catch (Exception e) {
        	logger.error("获取对象嵌套属性值失败",e);
            throw e;
        }
        return value;
    }

    /**
     * 获取对象属性包含父类内的
     *
     * @param obj       实体对象
     * @param fieldName 属性名
     * @return Field
     * 属性
     * @throws Exception
     */
    public static Field getFieldName(Object obj, String fieldName) throws Exception {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
//            	throw e;
            }
        }
        return null;
    }

    /**
     * 获取Class的属性包含父类内的
     *
     * @param clz       实体对象类型
     * @param fieldName 属性名
     * @return Field
     * 属性
     * @throws Exception
     */
    public static Field getFieldName(Class<?> clz, String fieldName) throws Exception {
        for (Class<?> superClass = clz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
//            	throw e;
            }
        }
        return null;
    }

    /**
     * 获取对象的所有属性（包含父类的）
     */
    public static Field[] getAllFields(Class<?> clz) {
        if (clz == Object.class) {
            return null;
        }
        Field[] fields = clz.getDeclaredFields();
        for (Class<?> superClass = clz.getSuperclass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            fields = concatAll(fields, superClass.getDeclaredFields());
        }
        return fields;
    }

    /**
     * 合并对象
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }



    /**
     * 判断一个值是否包含在枚举类中
     *
     * @param clz 枚举类
     * @param obj 判断的值
     * @throws Exception
     */
    public static boolean containEnum(Class clz, Object obj) throws Exception {
        if (clz.isEnum()) {
            Enum[] enums = (Enum[]) clz.getEnumConstants();
            Method method = clz.getMethod("getCode");
            for (Enum constant : enums) {
                Object code = method.invoke(constant);
                if (code.toString().equalsIgnoreCase(obj.toString())) {
                    return true;
                }
            }
        } else {
            throw new Exception("该类型不是枚举类型，请传枚举类型参数");
        }
        return false;
    }

    /**
     * 判断枚举类中是否存在这个字段
     *
     * @param clz 枚举类
     * @param obj 名称
     * @return boolean
     * @throws Exception
     */
    public static boolean enumContainName(Class clz, Object obj) throws Exception {
        if (clz.isEnum()) {
            Enum[] enums = (Enum[]) clz.getEnumConstants();
            Method method = clz.getMethod("getName");
            for (Enum constant : enums) {
                Object name = method.invoke(constant);
                if (name.equals(obj.toString())) {
                    return true;
                }
            }
        } else {
            throw new Exception("该类型不是枚举类型，请传枚举类型参数");
        }
        return false;
    }



    /**
     * 获取对象中指定名称的属性值。
     *
     * @param object       对象实例。
     * @param propertyName 属性名。
     * @return 属性值。
     */
    public static Object getPropertyValue(Object object, String propertyName) {
        Assert.notNull(propertyName, "propertyName 不能为空！");

        if (object == null) {
            return null;
        }

        String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        try {
            return MethodUtils.invokeExactMethod(object, getterMethodName);
        } catch (NoSuchMethodException e) {
            logger.error("对象中没有" + getterMethodName + "方法。");
        } catch (IllegalAccessException e) {
            logger.error("对象中的" + getterMethodName + "方法无法访问。");
        } catch (InvocationTargetException e) {
            logger.error("对象中的" + getterMethodName + "方法调用异常。");
        }

        return null;
    }
}
