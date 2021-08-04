package org.wangpai.calculator.model.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operand.Decimal;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operation.Operation;
import org.wangpai.calculator.model.symbol.operator.Operator;

import java.util.Collections;
import java.util.Stack;

import static org.wangpai.calculator.model.symbol.enumeration.Symbol.DOT;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.LEFT_BRACKET;

/**
 * @since 2021-8-1
 */
public final class CalculatorData implements Operable, Cloneable {
    // 对于 Stack 定义的栈，其栈底的序号为 0，入栈、出栈操作均是在栈顶进行的。

    @Getter(AccessLevel.PUBLIC)
    private Stack<Operand> opnds = new Stack<>(); // opnds：operand 操作数

    private Stack<Symbol> opndBuff = new Stack<>(); // opndBuff：operand buffer 缓存的操作数的每一位的值

    private Stack<Operator> optrs = new Stack<>(); // optrs：operator 运算符

    @Getter(AccessLevel.PUBLIC)
    private Stack<Symbol> exp = new Stack<>(); // inte：integration 当前整个表达式的状态

    public CalculatorData() {
        super();
    }

    @SneakyThrows
    @Override
    protected Object clone() {
        var cloned = (CalculatorData) super.clone();

        cloned.opnds = (Stack<Operand>) this.opnds.clone();
        cloned.opndBuff = (Stack<Symbol>) this.opndBuff.clone();
        cloned.optrs = (Stack<Operator>) this.optrs.clone();
        cloned.exp = (Stack<Symbol>) this.exp.clone();

        return cloned;
    }

    public void pushSymbol(Symbol symbol) {
        if (symbol.isDigit() || symbol == DOT) {
            this.opndBuff.push(symbol);
            this.exp.push(symbol);
        } else {
            try {
                this.optrs.push(new Operator(symbol));
            } catch (SyntaxException exception) {
                // 上面的 try 块并不会抛出异常
            }
            this.exp.push(symbol);
        }
    }

    public void pushToExp(Symbol symbol) {
        this.exp.push(symbol);
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

    public Symbol peekFromExp() {
        return this.exp.peek();
    }

    public Operator popFromOptrs() {
        return this.optrs.pop();
    }

    public Operand popFromOpnds() {
        return this.opnds.pop();
    }

    public Symbol popFromExp() {
        return this.exp.pop();
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
        return this.exp.empty();
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

    /**
     * @since 2021-8-4
     */
    public boolean loadOpnd() {
        if (this.opndBuff.empty()) {
            return false;
        }

        var symbols = this.opndBuff.toArray(Symbol[]::new);
        this.opnds.push(new Decimal(symbols).toRationalNumber());
        this.opndBuff.clear();
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

    /**
     * @since 2021-8-4
     */
    public CalculatorData clearAllCalData() {
        this.opnds.clear(); // 清空 opnd 原来的 Stack
        this.opndBuff.clear(); // 清空 this.opndBuff 原来的 Stack
        this.optrs.clear(); // 清空 optr 原来的 Stack
        this.exp.clear();// 清空 inte 原来的 Stack

        return this;
    }

}