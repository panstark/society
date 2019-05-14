package com.pan.society.vo.human;

import com.pan.society.vo.car.Car;

/**
 * create by panstark
 * create date 2019/5/5
 */
public class Zhangsan extends HumanWithCar{

    public Zhangsan(Car car) {
        super(car);
    }

    @Override
    public void goHome() {
        car.run();
        car.turnLeft();
        car.turnRight();
        car.stop();
    }
}
