package org.wangpai.calculator.model.symbol.operand;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UnknownException;
import org.wangpai.calculator.exception.tool.ExceptionTool;
import org.wangpai.calculator.model.symbol.operation.FractionOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;
import org.wangpai.calculator.model.symbol.operation.RationalNumberOperation;

public class RationalNumber extends Fraction {
    private Fraction numerator; // 分子
    private Fraction denominator; // 分母

    @Deprecated
    private boolean fieldValidity = true;

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
    public RationalNumber(Fraction numerator, Fraction denominator)
            throws CalculatorException {
        super();
        if (denominator.isZero()) {
            throw new SyntaxException("错误：0 不能作分母");
        }
        this.numerator = numerator.clone();
        this.denominator = denominator.clone();

        this.reduceFraction();
    }

    public RationalNumber(Fraction numerator) {
        super();
        this.numerator = numerator.clone();
        this.denominator = new Fraction(1);
    }

    public RationalNumber(long numerator, long denominator)
            throws CalculatorException {
        super();
        if (denominator == 0) {
            throw new SyntaxException("错误：0 不能作分母");
        }
        this.numerator = new Fraction(numerator);
        this.denominator = new Fraction(denominator);

        this.reduceFraction();
    }

    public RationalNumber(long numerator) {
        this(new Fraction(numerator));
    }

    @Deprecated
    @Override
    public String getEnumIdentifier() {
        return "RATIONALNUMBER";
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
        if (this.denominator.equals(new Fraction(1))) {
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
            return ((double) this.numerator.getFraction().longValueExact())
                    / this.denominator.getFraction().longValueExact();
        } else {
            return this.numerator.getFraction().longValueExact()
                    / ((double) this.denominator.getFraction().longValueExact());
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
            } else if (this.denominator.equals(new Fraction(1))) {
                return false;
            }

            if (((Fraction) FractionOperation.subtract(
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
            Fraction commonDivisor = FractionOperation.findGcd(this.numerator, this.denominator);
            this.numerator = FractionOperation.modsQuotient(this.numerator, commonDivisor);
            this.denominator = FractionOperation.modsQuotient(this.denominator, commonDivisor);

            if (this.denominator.isNegative()) {
                this.denominator = FractionOperation.getOpposite(this.denominator);
                this.numerator = FractionOperation.getOpposite(this.numerator);
            }
        } catch (Exception exception) {
            ExceptionTool.pkgException(exception);
        }

        return this;
    }

    public Fraction getNumerator() {
        return numerator;
    }

    public Fraction getDenominator() {
        return denominator;
    }
}
