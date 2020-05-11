package com.pan.society.service.model.strategy;

import org.springframework.stereotype.Component;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/8
 */
@Component
public class CatSeeEachOther implements SeeEachOtherIn {
    @Override
    public void seeEachOther() {
        System.out.println("cat will miaomiaomiao");
    }
}
