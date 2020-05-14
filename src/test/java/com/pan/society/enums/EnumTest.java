package com.pan.society.enums;

import com.pan.society.Common.ManyContants;
import com.pan.society.Common.enums.ManyEnum;
import org.junit.Test;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/12
 */
public class EnumTest {


    @Test
    public void enumTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        boolean b = ManyEnum.DrinkEnum.HARDWATER.equals(ManyEnum.DrinkEnum.WATER);

        System.out.println(b);

        String water = ManyContants.drink.WATER;
    }
}
