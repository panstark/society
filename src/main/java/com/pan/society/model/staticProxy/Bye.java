package com.pan.society.model.staticProxy;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/9
 */
public class Bye implements ByeInterface {

    @Override
    public void sayBye() {
        System.out.println("I wanna say bye");
    }

    @Override
    public void yeilBye() {
        System.out.println("I wanna yeilBye");
    }

    @Override
    public void shoutBye() {
        System.out.println("I wanna shoutBye");
    }
}
