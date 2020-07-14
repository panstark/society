package com.pan.society.know.design.pattern.behavioral.interpreter;

/**
 * Created by geely.
 *
 * 数字解释器
 * 解释数字
 */
public class NumberInterpreter implements Interpreter {

    private int number;

    public NumberInterpreter(int number){
        this.number=number;
    }

    public NumberInterpreter(String number){
        this.number=Integer.parseInt(number);
    }

    @Override
    public int interpret(){
        return this.number;
    }
}
