package com.pan.society.service.model.strategy;

import org.springframework.stereotype.Component;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/5/8
 */
@Component
public class HumanSeeEachOther implements SeeEachOtherIn {
    @Override
    public void seeEachOther() {
        System.out.println("human see each other will say hi");
    }
}
