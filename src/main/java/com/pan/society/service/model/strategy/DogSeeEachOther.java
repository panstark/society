package com.pan.society.service.model.strategy;

import org.springframework.stereotype.Component;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/8
 */
@Component
public class DogSeeEachOther implements SeeEachOtherIn {

    @Override
    public void seeEachOther() {
        System.out.println("dog will wang wang wang");
    }
}
