package org.wangpai.calculator.model.data;

import java.util.Collections;
import java.util.Stack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operand.Decimal;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;
import org.wangpai.calculator.model.symbol.operation.RationalNumberOperation;
import org.wangpai.calculator.model.symbol.operator.Operator;

import static org.wangpai.calculator.model.symbol.enumeration.Symbol.DOT;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.LEFT_BRACKET;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.RIGHT_BRACKET;

/**
 * @since 2021-8-1
 */
@Slf4j
public final class CalculatorData implements Cloneable {
    /**
     * 注意：对于 Stack，其栈底的序号为 0，入栈、出栈操作均是在栈顶进行的
     */

    /**
     * opnds：operand 操作数
     * <p>
     * 目前，这个栈里面储存的是有理数。这是由方法 loadOpnd 来决定的
     */
    @Getter(AccessLevel.PUBLIC)
    private Stack<Operand> opnds = new Stack<>();

    // opndBuff：operand buffer 缓存的操作数的每一位的值，包括小数点
    private Stack<Symbol> opndBuff = new Stack<>();

    // optrs：operator 运算符
    private Stack<Operator> optrs = new Stack<>();

    // exp：expresion 当前已读取的表达式
    @Getter(AccessLevel.PUBLIC)
    private Stack<Symbol> exp = new Stack<>();

    // 对已读取的表达式进行计算后的表达式。其中，此表达式可为操作数或运算符
    private Stack<Object> calculatedExp = new Stack<>();

    public CalculatorData() {
        super();
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

    public void pushToOptrs(Operator operator) {
        this.optrs.push(operator);
    }

    public void pushToExp(Symbol symbol) {
        this.exp.push(symbol);
    }

    public Operator popFromOptrs() {
        return this.optrs.pop();
    }

    public Symbol popFromExp() {
        return this.exp.pop();
    }

    public int opndBuffSize() {
        return this.opndBuff.size();
    }

    public int searchFromBuff(Symbol symbol) {
        return this.opndBuff.search(symbol);
    }

    /**
     * @lastModified 2021-8-8
     * @since 2021-8-1
     */
    public void pushSymbol(Symbol symbol) {
        if (symbol.isDigit() || symbol == DOT) {
            this.opndBuff.push(symbol);
        } else if (symbol == RIGHT_BRACKET) {
            /**
             * 执行到此处，说明遇到成对的括号，且此对括号中就只包含一个操作数。
             * 此时应该将前面的左括号弹出
             */

            this.optrs.pop();

            /**
             * 因为需要弹出的是左括号，而不是左括号右边的操作数。
             * 因此先需要将左括号右边的操作数先暂时弹出，
             * 等左括号弹出之后再弹入
             */
            var temp = this.calculatedExp.pop();
            this.calculatedExp.pop();
            this.calculatedExp.push(temp);
        } else {
            Operator operator = null;
            try {
                operator = new Operator(symbol);
            } catch (Exception exception) {
                log.error("异常：", exception);
            }
            this.optrs.push(operator);
            this.calculatedExp.push(operator);
        }

        this.exp.push(symbol);
    }

    /**
     * @return 左右括号相等时，返回 0；左括号多于右括号，返回 1；左括号小于右括号，返回 2
     */
    public int bracketMatch() {
        var antiOptrs = (Stack<Operator>) this.optrs.clone(); // antiOptrs：anti optr optr的反转
        Collections.reverse(antiOptrs);

        Stack<Symbol> brackets = new Stack<>();

        while (!antiOptrs.empty()) {
            var tempOptr = antiOptrs.pop().getSymbol();
            switch (tempOptr) {
                case LEFT_BRACKET:
                    brackets.push(tempOptr);
                    break;
                case RIGHT_BRACKET:
                    if (!brackets.empty() && LEFT_BRACKET == brackets.peek()) {
                        brackets.pop();
                    } else {
                        return 2;
                    }
                default:
                    break;
            }
        }

        // 如果最后 brackets 为空，说明左右括号相等
        if (brackets.empty()) {
            return 0;
        } else { // 如果最后 brackets 不为空，说明左括号数量多于右括号
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
     * @lastModified 2021-10-12
     */
    public Operand oneTimeCalculation() throws SyntaxException, UndefinedException {
        var optr = this.optrs.pop();
        var opndRight = this.opnds.pop();
        var opndLeft = this.opnds.pop();
        for (int i = 1; i <= 3; ++i) {
            this.calculatedExp.pop(); // 弹出两个操作数、一个运算符，共三个元素
        }

        Operand result = CalculatorData.oneTimeCalculation(opndLeft, optr, opndRight);
        this.opnds.push(result);
        this.calculatedExp.push(result);
        return result;
    }

    /**
     * 当遇到此方法不能处理的运算，均会抛出异常
     *
     * @lastModified 2021-8-8
     * @since 2021-8-5
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
                    if (opndRight.isZero()) {
                        throw new SyntaxException("错误：除数为 0");
                    }
                    return RationalNumberOperation.divide(
                            (RationalNumber) opndLeft, (RationalNumber) opndRight);

                default:
                    throw new UndefinedException("异常：不支持这种运算");
            }
        }

        throw new UndefinedException("异常：含有不支持的运算符");
    }

    /**
     * @lastModified 2021-8-8
     * @since 2021-8-1
     */
    @Override
    public Object clone() {
        CalculatorData cloned = null;
        try {
            cloned = (CalculatorData) super.clone();
        } catch (CloneNotSupportedException exception) {
            log.error("发生了非自定义异常：", exception);
        }

        cloned.opnds = (Stack<Operand>) this.opnds.clone();
        cloned.opndBuff = (Stack<Symbol>) this.opndBuff.clone();
        cloned.optrs = (Stack<Operator>) this.optrs.clone();
        cloned.exp = (Stack<Symbol>) this.exp.clone();
        cloned.calculatedExp = (Stack<Object>) this.calculatedExp.clone();

        return cloned;
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
     * @lastModified 2021-10-12
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
     * <p>
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
