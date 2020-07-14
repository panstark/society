package com.pan.society.know.design.pattern.behavioral.interpreter;

import java.util.Stack;

/**
 * Created by geely.
 *
 * Geely解释器
 *
 */
public class GeelyExpressionParser {

    private Stack<Interpreter> stack = new Stack<Interpreter>();

    public int parse(String str) {

        String[] strItemArray = str.split(" ");

        for (String symbol : strItemArray) {

            //不是运算符号，放入数字栈
            if (!OperatorUtil.isOperator(symbol)) {

                Interpreter numberExpression = new NumberInterpreter(symbol);
                stack.push(numberExpression);
                System.out.println(String.format("入栈: %d", numberExpression.interpret()));

            } else {

                //按照顺序进行计算

                //是运算符号，可以计算
                Interpreter firstExpression = stack.pop();
                Interpreter secondExpression = stack.pop();

                System.out.println(String.format("出栈: %d 和 %d", firstExpression.interpret(), secondExpression.interpret()));

                //获取计算器
                Interpreter operator = OperatorUtil.getExpressionObject(firstExpression, secondExpression, symbol);

                System.out.println(String.format("应用运算符: %s", operator));

                //进行计算
                int result = operator.interpret();
                NumberInterpreter resultExpression = new NumberInterpreter(result);

                //将计算结果入栈
                stack.push(resultExpression);
                System.out.println(String.format("阶段结果入栈: %d", resultExpression.interpret()));

            }
        }

        int result = stack.pop().interpret();

        return result;

    }
}
