package com.pan.society.service.model.strategy;

import org.springframework.stereotype.Component;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/8
 */
public class SeeEachOtherContext {

    private String type;
    private SeeEachOtherIn deal;

    public  SeeEachOtherContext(String type,SeeEachOtherIn deal){
        this.type = type;
        this.deal = deal;
    }

    public SeeEachOtherIn getDeal(){
        return deal;
    }
    public boolean options(String type){
        return this.type.equals(type);
    }

}
