package org.wangpai.calculator.model.data;

import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operand.Decimal;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;
import org.wangpai.calculator.model.symbol.operation.RationalNumberOperation;
import org.wangpai.calculator.model.symbol.operator.Operator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Stack;

import static org.wangpai.calculator.model.symbol.enumeration.Symbol.DOT;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.LEFT_BRACKET;

/**
 * @since 2021-8-1
 */
@Scope("singleton")
@Repository("calculatorData")
public final class CalculatorData implements Operable, Cloneable {
    /**
     * 注意：对于 Stack，其栈底的序号为 0，入栈、出栈操作均是在栈顶进行的
     */

    /**
     * opnds：operand 操作数
     * <p>
     * 目前，这个栈里面储存的是有理数。这是由方法 loadOpnd 来决定的
     */
    @Getter(AccessLevel.PUBLIC) // FIXME
    private Stack<Operand> opnds = new Stack<>();

    // opndBuff：operand buffer 缓存的操作数的每一位的值，包括小数点
    private Stack<Symbol> opndBuff = new Stack<>();

    // optrs：operator 运算符
    private Stack<Operator> optrs = new Stack<>();

    // exp：expresion 当前已读取的表达式
    @Getter(AccessLevel.PUBLIC) // FIXME
    private Stack<Symbol> exp = new Stack<>();

    // 对已读取的表达式进行计算后的表达式。其中，此表达式可为操作数或运算符
    private Stack<Object> calculatedExp = new Stack<>();

    public CalculatorData() {
        super();
    }

    /**
     * @lastModified 2021-8-8
     * @since 2021-8-1
     */
    @SneakyThrows
    @Override
    protected Object clone() {
        var cloned = (CalculatorData) super.clone();

        cloned.opnds = (Stack<Operand>) this.opnds.clone();
        cloned.opndBuff = (Stack<Symbol>) this.opndBuff.clone();
        cloned.optrs = (Stack<Operator>) this.optrs.clone();
        cloned.exp = (Stack<Symbol>) this.exp.clone();
        cloned.calculatedExp = (Stack<Object>) this.calculatedExp.clone();

        return cloned;
    }

    /**
     * @lastModified 2021-8-8
     * @since 2021-8-1
     */
    @SneakyThrows
    public void pushSymbol(Symbol symbol) {
        if (symbol.isDigit() || symbol == DOT) {
            this.opndBuff.push(symbol);
        } else {
            var operator = new Operator(symbol);
            this.optrs.push(operator);
            this.calculatedExp.push(operator);
        }
        this.exp.push(symbol);
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

    public boolean expIsEmpty() {
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

        // 如果最后 pares 为空，说明左右括号相等
        if (pares.empty()) {
            return 0;
        } else { // 如果最后 pares 不为空，说明左括号数量多于右括号
            return 1;
        }
    }

    /**
     * @lastModified 2021-8-8
     * @since 2021-8-4
     */
    public boolean loadOpnd() {
        if (this.opndBuff.empty()) {
            return false;
        }

        var rn = new Decimal(this.opndBuff.toArray(Symbol[]::new)).toRationalNumber();
        this.opnds.push(rn);
        this.calculatedExp.push(rn);
        this.opndBuff.clear();
        return true;
    }

    /**
     * @since 2021-8-5
     * @lastModified 2021-8-8
     */
    public void oneTimeCalculation() throws SyntaxException, UndefinedException {
        var optr = this.optrs.pop();
        var opndRight = this.opnds.pop();
        var opndLeft = this.opnds.pop();
        for (int i = 1; i <= 3; ++i) {
            this.calculatedExp.pop(); // 弹出两个操作数、一个运算符，共三个元素
        }

        Operand result = CalculatorData.oneTimeCalculation(opndLeft, optr, opndRight);
        this.opnds.push(result);
        this.calculatedExp.push(result);
    }

    /**
     * 当遇到此方法不能处理的运算，均会抛出异常
     *
     * @since 2021-8-5
     * @lastModified 2021-8-8
     */
    public static Operand oneTimeCalculation(Operand opndLeft, Operator optr, Operand opndRight)
            throws SyntaxException, UndefinedException {
        if (opndLeft instanceof RationalNumber
                && opndRight instanceof RationalNumber) {
            /**
             * 介于可读性及因反射的危险性带来的复杂性，此处不要使用反射
             */
            switch (optr.getSymbol()) {
                case ADD:
                    return RationalNumberOperation.add(
                            (RationalNumber) opndLeft, (RationalNumber) opndRight);
                case SUBTRACT:
                    return RationalNumberOperation.subtract(
                            (RationalNumber) opndLeft, (RationalNumber) opndRight);
                case MULTIPLY:
                    return RationalNumberOperation.multiply(
                            (RationalNumber) opndLeft, (RationalNumber) opndRight);
                case DIVIDE:
                    return RationalNumberOperation.divide(
                            (RationalNumber) opndLeft, (RationalNumber) opndRight);

                default:
                    throw new UndefinedException("异常：不支持这种运算");
            }
        }

        throw new UndefinedException("异常：含有不支持的运算符");
    }

    /**
     * @since 2021-8-4
     */
    public CalculatorData clearAllCalData() {
        this.opnds.clear();
        this.opndBuff.clear();
        this.optrs.clear();
        this.exp.clear();
        this.calculatedExp.clear();

        return this;
    }


    /**
     * @since 2021-8-9
     */
    public String expToString() {
        StringBuilder sb = new StringBuilder();
        for (var symbol : this.exp) {
            sb.append(symbol);
        }
        return sb.toString();
    }

    /**
     * @since 2021-8-9
     */
    public String calculatedExpToString() {
        StringBuilder sb = new StringBuilder();
        for (var ele : this.calculatedExp) {
            sb.append(ele);
        }
        return sb.toString();
    }


    /**
     * 因为此方法的含义模糊，所以尽量不要使用此方法
     *
     * 此方法显示的是 this.exp 的信息，不是 this.calculatedExp 的信息
     *
     * @since 2021-8-9
     */
    @Deprecated
    @Override
    public String toString() {
        return this.expToString();
    }
}
