package com.pan.society.know.design.pattern.behavioral.interpreter;

/**
 * Created by geely.
 *
 * 加法解释器
 * 进行加法
 */
public class AddInterpreter implements Interpreter {

    private Interpreter firstExpression,secondExpression;

    public AddInterpreter(Interpreter firstExpression, Interpreter secondExpression){
        this.firstExpression=firstExpression;
        this.secondExpression=secondExpression;
    }

    @Override
    public int interpret(){
        return this.firstExpression.interpret()+this.secondExpression.interpret();
    }

    @Override
    public String toString(){
        return "+";
    }
}
