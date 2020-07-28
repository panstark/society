package com.pan.society.enums;

import com.pan.society.common.ManyContants;
import com.pan.society.common.enums.ManyEnum;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/12
 */
public class EnumTest {

    private final Set<String> beanNames = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    @Test
    public void enumTest() {

        boolean b = ManyEnum.DrinkEnum.HARDWATER.equals(ManyEnum.DrinkEnum.WATER);
        //枚举与string的转化
        ManyEnum.DrinkEnum SOFTWATER = ManyEnum.DrinkEnum.valueOf("SOFTWATER");
        System.out.println(SOFTWATER);

        System.out.println(b);

        String water = ManyContants.drink.WATER;
        System.out.println(water);


    }



}
