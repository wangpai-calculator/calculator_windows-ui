package org.wangpai.calculator.model.symbol.operation;

import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.operand.Figure;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;

/**
 * @since 2021-8-1
 */
@Slf4j
public final class RationalNumberOperation {
    /*---------------加减乘除基本运算---------------*/

    /**
     * 算法如下：
     * 先得出两个分母的最大公约数，然后将这两个分母分别除以最大公约数，得到两个质因子（prime factor）
     * 最终的分子为：第一个分子乘以第二个分母的质因子，第二个分子乘以第一个分母的质因子，然后把得到的结果相加
     * 最终的分母为：其中一个分母乘以另一个分母的质因子（也即这两个分母的最小公倍数）
     *
     * @since 2021-8-5
     * @lastModified 2021-10-12
     */
    public static RationalNumber add(RationalNumber first, RationalNumber second) {
        final var firstNumerator = first.getNumerator();
        final var firstDenominator = first.getDenominator();
        final var secondNumerator = second.getNumerator();
        final var secondDenominator = second.getDenominator();

        Figure gcd = FigureOperation.findGcd(firstDenominator, secondDenominator);

        Figure firstPrimeFactor = FigureOperation.modsQuotient(firstDenominator, gcd);
        Figure secondPrimeFactor = FigureOperation.modsQuotient(secondDenominator, gcd);

        RationalNumber result = null;
        try {
            result = new RationalNumber(
                    FigureOperation.add(
                            FigureOperation.multiply(firstNumerator, secondPrimeFactor),
                            FigureOperation.multiply(secondNumerator, firstPrimeFactor)),
                    FigureOperation.multiply(firstDenominator, secondPrimeFactor))
                    .reduceFraction();
        } catch (Exception exception) {
            log.error("异常：", exception);
        }

        return result;
    }

    /**
     * 算法：第一个数加上第二个数的相反数
     *
     * @since before 2021-8-5
     */
    public static RationalNumber subtract(RationalNumber first, RationalNumber second) {
        return RationalNumberOperation.add(first, getOpposite(second));
    }

    /**
     * 算法：两个数的分子、分母分别相乘
     *
     * @since 2021-8-5
     * @lastModified 2021-10-12
     */
    public static RationalNumber multiply(RationalNumber first, RationalNumber second) {
        RationalNumber result = null;
        try {
            result = new RationalNumber(
                    FigureOperation.multiply(first.getNumerator(), second.getNumerator()),
                    FigureOperation.multiply(first.getDenominator(), second.getDenominator()))
                    .reduceFraction();
        } catch (Exception exception) {
            log.error("异常：", exception);

        }
        return result;
    }

    /**
     * 算法：将第二个整数与第一个有理数的分子相乘
     *
     * @since 2021-8-5
     * @lastModified 2021-10-12
     */
    public static RationalNumber multiply(RationalNumber first, Figure second) {
        RationalNumber result = null;
        try {
            result = new RationalNumber(
                    FigureOperation.multiply(first.getNumerator(), second),
                    first.getDenominator())
                    .reduceFraction();
        } catch (Exception exception) {
            log.error("异常：", exception);

        }
        return result;
    }

    /**
     * @since before 2021-8-5
     */
    public static RationalNumber multiply(RationalNumber first, long second) {
        return RationalNumberOperation.multiply(first, new Figure(second));
    }

    /**
     * 算法：两个整数直接相乘
     *
     * @since before 2021-8-5
     */
    public static RationalNumber multiply(Figure first, Figure second) {
        return new RationalNumber(FigureOperation.multiply(first, second));
    }

    /**
     * 算法：第一个数乘以第二个数的倒数
     *
     * @since before 2021-8-5
     */
    public static RationalNumber divide(RationalNumber first, RationalNumber second)
            throws SyntaxException {
        if (second.isZero()) {
            throw new SyntaxException("错误：0 不能作除数");
        }
        return RationalNumberOperation.multiply(first, getReciprocal(second));
    }

    /**
     * @since before 2021-8-5
     */
    public static RationalNumber divide(Figure first, Figure second)
            throws SyntaxException {
        if (second.isZero()) {
            throw new SyntaxException("错误：0 不能作除数");
        }
        return RationalNumberOperation.multiply(new RationalNumber(first),
                RationalNumberOperation.getReciprocal(new RationalNumber(second)));
    }

    /****************加减乘除基本运算****************/

    /**
     * 求相反数
     *
     * @since 2021-8-5
     * @lastModified 2021-10-12
     */
    public static RationalNumber getOpposite(RationalNumber rationalNumber) {
        RationalNumber result = null;
        try {
            result = new RationalNumber(
                    FigureOperation.getOpposite(rationalNumber.getNumerator()),
                    rationalNumber.getDenominator().clone());
        } catch (Exception exception) {
            log.error("异常：", exception);
        }
        return result;
    }

    /**
     * 求绝对值
     *
     * @since 2022-8-23
     */
    public static RationalNumber getAbsolute(RationalNumber rationalNumber)
            throws SyntaxException {
        return new RationalNumber(FigureOperation.getAbsolute(rationalNumber.getNumerator()),
                FigureOperation.getAbsolute(rationalNumber.getDenominator()));
    }

    /**
     * 求倒数
     *
     * @since before 2021-8-5
     */
    public static RationalNumber getReciprocal(RationalNumber rationalNumber)
            throws SyntaxException {
        if (rationalNumber.isZero()) {
            throw new SyntaxException("错误：0 没有倒数");
        }

        try {
            return new RationalNumber(
                    rationalNumber.getDenominator().clone(),
                    rationalNumber.getNumerator().clone());
        } catch (SyntaxException ignored) {
            return null; // 仅用于占位
        }
    }

    /**
     * 占位空方法
     *
     * @since before 2021-8-5
     */
    public static RationalNumber power(RationalNumber base, RationalNumber exponent)
            throws SyntaxException {
        throw new SyntaxException("错误：不支持指数为有理数的乘方"); // 原因是，当指数为有理数时，其结果为实数，不一定为有理数
    }

    /**
     * 有数数的整数乘方。注意：指数 exponent 不能太大
     *
     * @since 2022-8-23
     */
    public static RationalNumber power(RationalNumber base, Figure exponent)
            throws SyntaxException {
        if (base.isZero() && exponent.isZero()) {
            throw new SyntaxException("错误：不能计算 0 的 0 次方");
        }
        if (base.isZero() && exponent.isNegative()) {
            throw new SyntaxException("错误：不能计算 0 的负数次方");
        }
        if (exponent.isZero()) {
            return new RationalNumber(1);
        }

        boolean needReciprocal = false;
        if (exponent.isNegative()) {
            needReciprocal = true;
        }
        Figure multiplyTimes = FigureOperation.getAbsolute(exponent);
        RationalNumber result = new RationalNumber(
                FigureOperation.power(base.getNumerator(), multiplyTimes),
                FigureOperation.power(base.getDenominator(), multiplyTimes));

        // 另一种实现
//        for (Figure multiplyTimes = FigureOperation.getAbsolute(exponent);
//             !multiplyTimes.isZero();
//             multiplyTimes = FigureOperation.subtract(multiplyTimes, Figure.ONE)) {
//            result = RationalNumberOperation.multiply(result, base);
//        }

        if (needReciprocal) {
            return getReciprocal(result);
        } else {
            return result;
        }
    }
}
