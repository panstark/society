package com.pan.society.vo.car;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * create by panstark
 * create date 2019/5/5
 */
@Component
@Slf4j
public class AudiCar implements Car{

    @Override
    public void run() {
        log.info("the car is started.");
    }

    @Override
    public void stop() {
        log.info("the car is stopped.");
    }

    @Override
    public void turnOnLight() {

    }

    @Override
    public void turnOffLight() {

    }

    @Override
    public void turnLeft() {
        log.info("the car turns left.");
    }

    @Override
    public void turnRight() {
        log.info("the car turns right.");
    }
}
