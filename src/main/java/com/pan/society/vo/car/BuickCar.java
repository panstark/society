package com.pan.society.vo.car;

import com.pan.society.anno.SafeCar;
import lombok.extern.slf4j.Slf4j;

/**
 * create by panstark
 * create date 2019/5/5
 */
@Slf4j
@SafeCar
public class BuickCar implements Car {
    @Override
    public void run() {
        log.info("the car is running.");
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
        log.info("the car turn left.");
    }

    @Override
    public void turnRight() {
        log.info("the car turn right.");
    }
}
