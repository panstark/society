package com.pan.society.anno;

import java.lang.annotation.*;

/**
 * create by panstark
 * create date 2019/5/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnoMethod {

    String value() default "wow";

    String name() default "annoMethod-name";
}
