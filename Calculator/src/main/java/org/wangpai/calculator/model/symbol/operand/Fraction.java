package org.wangpai.calculator.model.symbol.operand;

import org.wangpai.calculator.model.symbol.operation.FractionOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;

import java.math.BigInteger;

/**
 * 整数。因为此类原本用于表示分数的分子和分母，所以导致使用了 Fraction（分数） 一词。
 *
 * 因为除法对整数有余数，因此此类不支持除法运算
 */
public class Fraction implements Operand { // FIXME 尝试将名称改为 Figure
    private BigInteger fraction;

    @Deprecated
    private boolean fieldValidity = true;

    @Deprecated
    private static final String TYPE_ERROR_MSG = "操作数类型错误";

    public Fraction() {
        super();
    }

    /**
     * 此方法默认浅拷贝
     */
    public Fraction(Fraction other) {
        super();

        this.fraction = other.fraction;
    }

    public Fraction(BigInteger num) {
        super();
        this.fraction = num;
    }

    public Fraction(long num) {
        this.fraction = BigInteger.valueOf(num);
    }

    @Override
    public String getEnumIdentifier() {
        return "FRACTION";
    }

    @Override
    public Class<? extends Operation> getBindingOperation() {
        return FractionOperation.class;
    }

    public BigInteger getFraction() {
        return fraction;
    }

    public static Fraction valueOf(long num) {
        return new Fraction(BigInteger.valueOf(num));
    }

    public Fraction setFraction(BigInteger fraction) {
        this.fraction = fraction;
        return this;
    }

    public Fraction setFraction(long num) {
        this.fraction = BigInteger.valueOf(num);
        return this;
    }

    private static BigInteger cloneBigInteger(BigInteger other) {
        return new BigInteger(other.toString());
    }

    @Override
    public Fraction clone() {
        Fraction cloned = new Fraction();
        cloned.fraction = cloneBigInteger(this.fraction);
        return cloned;
    }

    @Override
    public String toString() {
        return fraction.toString();
    }

    /**
     * 因为这个方法是重写方法，所以这个方法不能抛出异常
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof Operand)) {
            return false;
        }

        Operand result;

        try {
            /**
             * 只要此处抛出了异常，均视为相等判断失败
             */
            result = Operation.subtract(this, (Operand) other);
        } catch (Exception exception) {
            return false;
        }

        return result.isZero();
    }

    @Override
    public boolean isZero() {
        return this.fraction.equals(BigInteger.ZERO);
    }

    public boolean isNegative() {
        /**
         * 对于 BigInteger 的函数 signum 的返回值：
         *   > 1：代表正数
         *   > 0：代表 0
         *   > -1：代表 负数
         */
        if (this.fraction.signum() == -1) {
            return true;
        }

        return false;
    }
}
