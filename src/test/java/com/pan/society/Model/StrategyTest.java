package com.pan.society.Model;

import com.pan.society.config.ApplicationContextRegister;
import com.pan.society.service.model.strategy.SeeEachOtherIn;
import com.pan.society.service.model.strategy.WhoSeeEachOther;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {

    @Test
    public void testMode() throws ClassNotFoundException {
        WhoSeeEachOther whoSeeEachOther = new WhoSeeEachOther();
        whoSeeEachOther.seeEachOther("dog");
        String className = "com.pan.society.service.model.strategy.CatSeeEachOther";
        SeeEachOtherIn see =  (SeeEachOtherIn) ApplicationContextRegister.getApplicationContext().getBean(Class.forName(className));
        see.seeEachOther();
    }
}
