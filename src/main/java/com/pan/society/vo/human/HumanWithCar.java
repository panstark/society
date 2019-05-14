package com.pan.society.vo.human;

import com.pan.society.vo.car.Car;

/**
 * create by panstark
 * create date 2019/5/5
 */
public abstract class HumanWithCar implements Human {

    protected Car car;

    public HumanWithCar(Car car){
        this.car = car;
    }

    public HumanWithCar(){

    }

    @Override
    public abstract void goHome();
}
