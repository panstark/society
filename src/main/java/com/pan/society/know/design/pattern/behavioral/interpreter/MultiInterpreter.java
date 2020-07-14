package com.pan.society.know.design.pattern.behavioral.interpreter;

/**
 * Created by geely.
 *
 * 乘法解释器，进行乘法解释
 */
public class MultiInterpreter implements Interpreter {

    private Interpreter firstExpression,secondExpression;

    public MultiInterpreter(Interpreter firstExpression, Interpreter secondExpression){
        this.firstExpression=firstExpression;
        this.secondExpression=secondExpression;
    }

    @Override
    public int interpret(){
        return this.firstExpression.interpret()*this.secondExpression.interpret();
    }

    @Override
    public String toString(){
        return "*";
    }

}
