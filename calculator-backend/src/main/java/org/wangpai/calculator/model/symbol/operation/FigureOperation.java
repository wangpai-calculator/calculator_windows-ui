package org.wangpai.calculator.model.symbol.operation;

import java.math.BigInteger;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.operand.Figure;

/**
 * 此类不定义整数除法运算
 *
 * @since 2021-8-2
 */
public final class FigureOperation {
    /*---------------加减乘---------------*/

    /**
     * @since 2021-8-5
     */
    public static Figure add(Figure first, Figure second) {
        return new Figure(first.getInteger().add(second.getInteger()));
    }

    /**
     * @since 2021-8-5
     */
    public static Figure subtract(Figure first, Figure second) {
        return new Figure(first.getInteger().subtract(second.getInteger()));
    }

    /**
     * @since 2021-8-5
     */
    public static Figure multiply(Figure first, Figure second) {
        return new Figure(first.getInteger().multiply(second.getInteger()));
    }

    /**
     * 占位空方法
     *
     * @since before 2021-8-5
     */
    public final static Figure divide(Figure first, Figure second)
            throws SyntaxException {
        throw new SyntaxException("错误：整数不支持除法运算");
    }

    /****************加减乘****************/

    /**
     * @return 返回的数组中，0 号元素代表商，1 号元素代表余数
     */
    public static Figure[] divideAndRemainder(Figure first, Figure second) {
        BigInteger[] quotientAndRemainder = first.getInteger()
                .divideAndRemainder(second.getInteger());
        Figure[] result = new Figure[quotientAndRemainder.length];
        for (int index = 0; index < quotientAndRemainder.length; ++index) {
            result[index] = new Figure(quotientAndRemainder[index]);
        }
        return result;

    }

    public static Figure[] divideAndRemainder(Figure first, long second) {
        return divideAndRemainder(first, new Figure(second));
    }

    /**
     * @return 返回求余数运算得到的余数
     */
    public static Figure mod(Figure first, Figure second) {
        final int REMAINDER_INDEX = 1;
        return divideAndRemainder(first, second)[REMAINDER_INDEX];
    }

    public static Figure mod(Figure first, long second) {
        return mod(first, new Figure(second));
    }

    /**
     * modsQuotient：mod's Quotient 求余数的商
     *
     * @return 返回求余数运算得到的商
     */
    public static Figure modsQuotient(Figure first, Figure second) {
        final int QUOTIENT_INDEX = 0;
        return divideAndRemainder(first, second)[QUOTIENT_INDEX];
    }

    public static Figure modsQuotient(Figure first, long second) {
        return modsQuotient(first, new Figure(second));
    }

    /**
     * 求相反数
     *
     * 算法：将形参乘以 -1
     *
     * @since before 2021-8-5
     */
    public final static Figure getOpposite(Figure num) {
        return multiply(num, new Figure(-1));
    }

    /**
     * @since before 2021-8-5
     */
    public final static Figure getOpposite(long num) {
        return getOpposite(new Figure(num));
    }

    /**
     * 求绝对值
     *
     * @since 2022-8-23
     */
    public static Figure getAbsolute(Figure num) {
        if (num.isNegative()) {
            return getOpposite(num);
        } else {
            return num.clone();
        }
    }

    /**
     * 求两个数的最大公约数。GCD：Greatest Common Divisor
     *
     * 注意事项：
     * > 当这两个数只有一个为 0 时，结果为另一个数的绝对值。
     * > 特别地，当这两个数均为 0 时，结果为 0。
     * > 其它情况下，结果为正数
     *
     * @since before 2021-8-5
     */
    public static Figure findGcd(Figure first, Figure second) {
        return new Figure(first.clone().getInteger()
                .gcd(second.clone().getInteger()));
    }

    /**
     * 求两个数的最小公倍数。LCM：Least Common Multiple
     *
     * 算法如下：
     * 先得出这两个数的最大公约数，
     * 然后将其中一个数除以最大公约数，得到其中一个质因子（prime factor）
     * 最后将该质因子另外一个没有除以过公约数的数相乘
     *
     * @since before 2021-8-5
     */
    public static Figure findLcm(Figure first, Figure second) {
        return multiply(first, subtract(second, findGcd(first, second)));
    }

    /**
     * 整数的乘方。注意：指数 exponent 不能太大
     *
     * @since before 2021-8-5
     */
    public static Figure power(Figure base, Figure exponent)
            throws SyntaxException {
        if (base.isZero() && exponent.isZero()) {
            throw new SyntaxException("错误：不能计算 0 的 0 次方");
        }
        if (exponent.isNegative()) {
            throw new SyntaxException("错误：整数乘法不支持负数次方");
        }

        return new Figure(base.getInteger().pow(exponent.getInteger().intValue()));
    }

    public static Figure power(long base, long exponent)
            throws SyntaxException {
        return FigureOperation.power(new Figure(base), new Figure(exponent));
    }

}
