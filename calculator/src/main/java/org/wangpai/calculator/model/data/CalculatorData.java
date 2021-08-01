package org.wangpai.calculator.model.data;


import lombok.AccessLevel;
import lombok.Getter;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;
import org.wangpai.calculator.model.symbol.operation.FractionOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;
import org.wangpai.calculator.model.symbol.operator.Operator;

import java.util.Collections;
import java.util.Stack;

import static org.wangpai.calculator.model.symbol.enumeration.Symbol.DOT;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.LEFT_BRACKET;

public final class CalculatorData implements Operable {
    // 对于 Stack 定义的栈，其栈底的序号为 0，入栈、出栈操作均是在栈顶进行的。

    @Getter(AccessLevel.PUBLIC)
    private Stack<Operand> opnds = new Stack<>(); // opnds：operand 操作数

    private Stack<Symbol> opndBuff = new Stack<>(); // opndBuff：operand buffer 缓存的操作数的每一位的值

    private Stack<Operator> optrs = new Stack<>(); // optrs：operator 运算符

    // 以后尝试重命名为 originInput
    @Deprecated
    @Getter(AccessLevel.PUBLIC)
    private Stack<Symbol> inte = new Stack<>(); // inte：integration 当前整个表达式的状态

    public CalculatorData() {
        super();
    }

    public void pushSymbol(Symbol symbol) {
        if (symbol.isDigit() || symbol == DOT) {
            this.opndBuff.push(symbol);
            this.inte.push(symbol);
        } else {
            try {
                this.optrs.push(new Operator(symbol));
            } catch (SyntaxException exception) {
                // 上面的 try 块并不会抛出异常
            }
            this.inte.push(symbol);
        }
    }

    public void pushToInte(Symbol symbol) {
        this.inte.push(symbol);
    }

    public void pushToOptrs(Operator operator) {
        this.optrs.push(operator);
    }

    public void pushToBuff(Symbol symbol) {
        this.opndBuff.push(symbol);
    }

    public void pushToOpnds(Operand operand) {
        this.opnds.push(operand);
    }

    public int searchFromBuff(Symbol symbol) {
        return this.opndBuff.search(symbol);
    }

    public Symbol peekFromBuff() {
        return this.opndBuff.peek();
    }

    public Operator peekFromOptrs() {
        return this.optrs.peek();
    }

    public Operand peekFromOpnds() {
        return this.opnds.peek();
    }

    public Symbol peekFromInte() {
        return this.inte.peek();
    }

    public Operator popFromOptrs() {
        return this.optrs.pop();
    }

    public Operand popFromOpnds() {
        return this.opnds.pop();
    }

    public Symbol popFromInte() {
        return this.inte.pop();
    }

    public boolean optrsIsEmpty() {
        return this.optrs.empty();

    }

    public boolean opndsIsEmpty() {
        return this.opnds.empty();
    }

    public boolean opndBuffIsEmpty() {
        return this.opndBuff.empty();
    }

    public boolean inteIsEmpty() {
        return this.inte.empty();
    }

    public int opndBuffSize() {
        return this.opndBuff.size();
    }


    /**
     * @return 左右括号相等时，返回 0；左括号多于右括号，返回 1；左括号小于右括号，返回 2
     * @apiNote pareMatch：parentheses match 括号匹配
     */
    public int pareMatch() {
        var antiOptrs = (Stack<Operator>) this.optrs.clone(); // antiOptrs：anti optr optr的反转
        Collections.reverse(antiOptrs);

        Stack<Symbol> pares = new Stack<>();

        while (!antiOptrs.empty()) {
            var tempOptr = antiOptrs.pop().getSymbol();

            switch (tempOptr) {
                case LEFT_BRACKET:
                    pares.push(tempOptr);
                    break;
                case RIGHT_BRACKET:
                    if (!pares.empty() && LEFT_BRACKET == pares.peek()) {
                        pares.pop();
                    } else {
                        return 2;
                    }
                default:
                    break;
            }
        }

        // 如果最后pares为空，说明左右括号相等
        if (pares.empty()) {
            return 0;
        } else { // 如果最后pares不为空，说明左括号数量多于右括号
            return 1;
        }
    }

    @Deprecated
    public boolean loadOpnd() {
        if (true == this.opndBuff.empty()) {
            return false;
        }

        Operand sum = new RationalNumber(0);

        // 得到小数点的位置，这个位置是把栈顶视为起点来计算的，且视栈顶序号为1
        int pointLoc = this.opndBuff.search(DOT);// pointLoc：point loction

        if (-1 != pointLoc) {
            int fractionLen = pointLoc - 1;
            var fraction = new Stack<Symbol>();

            // 如果有小数部分，先要反转才能弹出
            for (int digitOrder = 0; digitOrder < fractionLen; ++digitOrder) {
                var topTemp = this.opndBuff.pop();
                fraction.push(topTemp);
            }
            for (int digitBit = 1; !fraction.empty(); ++digitBit) {
                var topTemp = new RationalNumber(Integer.parseInt(fraction.pop().toString()));
                try {
                    // 下面表达式指的是：sum = sum + topTemp / pow(10, digitBit)
                    sum = Operation.add(sum,
                            Operation.divide(topTemp,
                                    FractionOperation.power(10, digitBit)));
                } catch (CalculatorException exception) {
                }
            }
            this.opndBuff.pop();// 小数部分已读取完毕，应去掉缓存里的小数点
        }

        // 整数部分可以不用反转直接弹出
        for (int digit = 1; !this.opndBuff.empty(); ++digit) {
            var topTemp = new RationalNumber(Integer.parseInt(this.opndBuff.pop().toString()));

            // 下面表达式指的是：sum = sum + topTemp * pow(10, digit -1)
            try {
                sum = Operation.add(sum,
                        Operation.multiply(topTemp,
                                FractionOperation.power(10, digit - 1)));
            } catch (CalculatorException exception) {
            }
        }

        this.opnds.push(sum);
        return true;
    }

    public void oneTimeCalculation() throws CalculatorException {
        var optr = this.optrs.pop();
        var opndRight = this.opnds.pop();
        var opndLeft = this.opnds.pop();

        /**
         * 介于可读性及因反射的危险性带来的复杂性，此处不要使用反射
         */
        switch (optr.getSymbol()) {
            case ADD:
                this.opnds.push(Operation.add(opndLeft, opndRight));
                break;
            case SUBTRACT:
                this.opnds.push(Operation.subtract(opndLeft, opndRight));
                break;
            case MULTIPLY:
                this.opnds.push(Operation.multiply(opndLeft, opndRight));
                break;
            case DIVIDE:
                this.opnds.push(Operation.divide(opndLeft, opndRight));
                break;
        }
    }

    public static Operand oneTimeCalculation(Operand opndLeft, Operator optr, Operand opndRight)
            throws CalculatorException {
        /**
         * 介于可读性及因反射的危险性带来的复杂性，此处不要使用反射
         */
        switch (optr.getSymbol()) {
            case ADD:
                return Operation.add(opndLeft, opndRight);
            case SUBTRACT:
                return Operation.subtract(opndLeft, opndRight);
            case MULTIPLY:
                return Operation.multiply(opndLeft, opndRight);
            case DIVIDE:
                return Operation.divide(opndLeft, opndRight);
        }
        return null;
    }

    public void clearAllCalData() {
        /**
         * 这里使用的是丢弃原对象的清空方法，这会加重虚拟机的负担，不推荐
         */
        this.opnds = new Stack<>(); // 清空 opnd 原来的 Stack
        this.opndBuff = new Stack<>(); // 清空 this.opndBuff 原来的 Stack
        this.optrs = new Stack<>(); // 清空 optr 原来的 Stack
        this.inte = new Stack<>();// 清空 inte 原来的 Stack
    }

}
