package com.pan.society.know.design.pattern.behavioral.interpreter;

/**
 * @author by panstark
 * @description
 * @notice
 * @date 2020/7/14
 */
public class MinusInterpreter implements Interpreter {

    private Interpreter firstExpression,secondExpression;

    public MinusInterpreter(Interpreter firstExpression, Interpreter secondExpression){
        this.firstExpression=firstExpression;
        this.secondExpression=secondExpression;
    }

    @Override
    public int interpret(){
        return this.firstExpression.interpret()-this.secondExpression.interpret();
    }

    @Override
    public String toString(){
        return "-";
    }
}
