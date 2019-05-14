package com.pan.society;

import com.pan.society.vo.IoCContainer;
import com.pan.society.vo.car.AudiCar;
import com.pan.society.vo.car.BuickCar;
import com.pan.society.vo.human.Human;
import com.pan.society.vo.human.Lisi;
import com.pan.society.vo.human.Zhangsan;
import org.junit.Before;
import org.junit.Test;

/**
 * create by panstark
 * create date 2019/5/5
 */
public class IoCTest {

    private IoCContainer ioc = new IoCContainer();

    @Before
    public void setBean(){
        ioc.setBeans(AudiCar.class,"audicar");
        ioc.setBeans(BuickCar.class,"buickcar");
        ioc.setBeans(Lisi.class,"lisi","buickcar");
        ioc.setBeans(Zhangsan.class,"zhangsan","buickcar");

    }

    @Test
    public void testIoC(){
        Human zhangsan =(Human) ioc.getBean("zhangsan");
        zhangsan.goHome();
        Human lisi =(Human) ioc.getBean("lisi");
        lisi.goHome();
    }
}
