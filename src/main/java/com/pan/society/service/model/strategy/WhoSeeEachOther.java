package com.pan.society.service.model.strategy;

import com.pan.society.config.ApplicationContextRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/8
 */
public class WhoSeeEachOther {

    private static List<SeeEachOtherContext> algs = new ArrayList();
    //静态代码块,先加载所有的策略
    static {
        algs.add(new SeeEachOtherContext("dog",new DogSeeEachOther()));
        algs.add(new SeeEachOtherContext("cat",new CatSeeEachOther()));
        algs.add(new SeeEachOtherContext("human",new HumanSeeEachOther()));

    }
    public void seeEachOther(String type){
        SeeEachOtherIn seeEachOther = null;
        for (SeeEachOtherContext context  : algs) {
            if (context.options(type)) {
                seeEachOther = context.getDeal();
                break;
            }
        }
        seeEachOther.seeEachOther();
    }

}
