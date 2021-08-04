package org.wangpai.calculator.model.symbol.operand;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.exception.UnknownException;
import org.wangpai.calculator.exception.tool.ExceptionTool;
import org.wangpai.calculator.model.symbol.operation.FigureOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;
import org.wangpai.calculator.model.symbol.operation.RationalNumberOperation;

import java.math.BigInteger;

/**
 * @since 2021-8-1
 */
public class RationalNumber extends Figure {
    private Figure numerator; // 分子
    private Figure denominator; // 分母

    public RationalNumber() {
        super();
    }

    public RationalNumber(RationalNumber other) throws CalculatorException {
        super();
        this.numerator = other.numerator;
        this.denominator = other.denominator;

        this.reduceFraction();
    }

    /**
     * @param numerator   分子
     * @param denominator 分母
     */
    public RationalNumber(Figure numerator, Figure denominator)
            throws CalculatorException {
        super();
        if (denominator.isZero()) {
            throw new SyntaxException("错误：0 不能作分母");
        }
        this.numerator = numerator.clone();
        this.denominator = denominator.clone();

        this.reduceFraction();
    }

    public RationalNumber(long numerator, long denominator)
            throws CalculatorException {
        super();
        if (denominator == 0) {
            throw new SyntaxException("错误：0 不能作分母");
        }
        this.numerator = new Figure(numerator);
        this.denominator = new Figure(denominator);

        this.reduceFraction();
    }

    public RationalNumber(Figure numerator) {
        super();
        this.numerator = numerator.clone();
        this.denominator = new Figure(1);
    }

    public RationalNumber(long numerator) {
        this(new Figure(numerator));
    }

    /**
     * 此方法默认深拷贝
     *
     * @since 2021-8-3
     */
    public RationalNumber(Operand other) throws CalculatorException {
        super();

        /**
         * 因为 Java 规定构造器 this(...) 调用必须位于第一行，
         * 所以这里只能先构造一个临时的变量，然后将其拷贝至 this
         */
        RationalNumber temp;

        /**
         * 由于 Operand 与普通的类没有继承关系（无法将普通的类型强制转换为 Operand 类型），
         * 所以 other 不可能为类型 long、BigInteger
         */
        switch (other.getClass().getSimpleName()) {
            case "RationalNumber":
                temp = new RationalNumber((RationalNumber) other);
                break;
            case "Figure":
                temp = new RationalNumber((Figure) other);
                break;

            default:
                throw new UndefinedException("异常：不支持此类的初始化");
        }

        // 将 temp 浅拷贝至 this
        this.numerator = temp.numerator;
        this.denominator = new Figure(1);
    }

    @Override
    public Class<? extends Operation> getBindingOperation() {
        return RationalNumberOperation.class;
    }

    @Override
    public RationalNumber clone() {
        RationalNumber cloned = new RationalNumber();
        cloned.numerator = this.numerator.clone();
        cloned.denominator = this.denominator.clone();

        return cloned;
    }


    @Override
    public boolean isZero() {
        return this.numerator.isZero();
    }

    @Override
    public boolean isNegative() {
        try {
            this.reduceFraction();
        } catch (Exception exception) {
            // FIXME
        }

        if (this.numerator.isNegative()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public String toString() {
        if (this.denominator.equals(new Figure(1))) {
            return this.numerator.toString();
        } else {
            return this.numerator.toString() + "/" + this.denominator.toString();
        }
    }

    public double toDouble() {
        /**
         * 将分子、分母中较小的那个数转换为类型 double 来运算
         */
        if (this.isProperFraction()) {
            return ((double) this.numerator.getFigure().longValueExact())
                    / this.denominator.getFigure().longValueExact();
        } else {
            return this.numerator.getFigure().longValueExact()
                    / ((double) this.denominator.getFigure().longValueExact());
        }
    }

    /**
     * 是否是真分数
     */
    public boolean isProperFraction() {
        try {
            this.reduceFraction();
            if (this.isZero()) {
                return true;
            } else if (this.denominator.equals(new Figure(1))) {
                return false;
            }

            if (((Figure) FigureOperation.subtract(
                    this.numerator, this.denominator))
                    .isNegative()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            return false; // 此语句只用于占位
        }
    }

    /**
     * 约分。约分后，分母恒为正数
     */
    public RationalNumber reduceFraction() throws UnknownException {
        try {
            Figure commonDivisor = FigureOperation.findGcd(this.numerator, this.denominator);
            this.numerator = FigureOperation.modsQuotient(this.numerator, commonDivisor);
            this.denominator = FigureOperation.modsQuotient(this.denominator, commonDivisor);

            if (this.denominator.isNegative()) {
                this.denominator = FigureOperation.getOpposite(this.denominator);
                this.numerator = FigureOperation.getOpposite(this.numerator);
            }
        } catch (Exception exception) {
            ExceptionTool.pkgException(exception);
        }

        return this;
    }

    public Figure getNumerator() {
        return numerator;
    }

    public Figure getDenominator() {
        return denominator;
    }
}
