package com.pan.society.vo.human;

import com.pan.society.anno.AnnoMethod;
import com.pan.society.anno.Display;
import com.pan.society.anno.GoodPeople;
import com.pan.society.vo.car.Car;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * create by panstark
 * create date 2019/5/5
 */
@GoodPeople
@Slf4j
@Data
public class Lisi extends HumanWithCar {

    public Lisi(Car car){
        super(car);
    }
    public Lisi(){
        super();
    }
    @Display("名字")
    public String name;
    @Display("性别")
    public String gender;
    @Display("身高")
    public String height;


    @Override
    @AnnoMethod(name="meat",value="a peace of meat")
    public void goHome() {
        car.run();
        car.stop();
    }

   // @AnnoMethod(name="meat",value="a peace of meat")
    public void eat(String food){
        System.out.println("I want to eat:"+food);
    }

}
