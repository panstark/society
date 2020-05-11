package com.pan.society.vo.human;

import com.pan.society.vo.car.Car;

/**
 * create by panstark
 * create date 2019/5/5
 */
public abstract class Driver implements Human {

    protected Car car;

    public Driver(Car car){
        this.car = car;
    }

    public Driver(){

    }

    @Override
    public abstract void goHome();
}
