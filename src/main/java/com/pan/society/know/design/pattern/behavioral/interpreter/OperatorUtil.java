package com.pan.society.know.design.pattern.behavioral.interpreter;

/**
 * Created by geely.
 *
 * 操作工具
 *
 */
public class OperatorUtil {

    public static boolean isOperator(String symbol) {

        return (symbol.equals("+") || symbol.equals("*") ||symbol.equals("-"));
    }



    public static Interpreter getExpressionObject(Interpreter firstExpression, Interpreter secondExpression, String symbol) {
        if (symbol.equals("+")) {

            return new AddInterpreter(firstExpression, secondExpression);
        } else if (symbol.equals("*")) {

            return new MultiInterpreter(firstExpression, secondExpression);
        }else if (symbol.equals("-")){

            return new MinusInterpreter(firstExpression, secondExpression);
        }
        return null;
    }
}
