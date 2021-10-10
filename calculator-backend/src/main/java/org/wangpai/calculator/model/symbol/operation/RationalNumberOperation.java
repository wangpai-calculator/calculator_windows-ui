package org.wangpai.calculator.model.symbol.operation;

import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UnknownException;
import org.wangpai.calculator.exception.tool.ExceptionTool;
import org.wangpai.calculator.model.symbol.operand.Figure;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;

import java.lang.reflect.InvocationTargetException;

/**
 * @since 2021-8-1
 */
@Slf4j
public class RationalNumberOperation extends Operation {
    /*---------------加减乘除基本运算---------------*/

    /**
     * 此函数将用于导航到最终的运算函数。
     * 最终的运算函数指的是每个操作数的类型均与方法签名中标明的一致的函数
     *
     * @deprecated 2021-8-5
     * 此方法为使用了反射，为废弃方法，仅基类可以调用
     */
    @Deprecated
    protected final static RationalNumber methodNavigation(
            String methodName, RationalNumber first,
            Operand second, boolean hasSwap)
            throws CalculatorException {
        var firstClass = first.getClass();
        if (firstClass != RationalNumber.class) {
            throw new SyntaxException("错误：含有此运算不支持的操作数类型 " + firstClass);
        }

        Class<?> secondParaClass;
        switch (second.getClass().getSimpleName()) {
            case "RationalNumber":
                secondParaClass = RationalNumber.class;
                break;
            case "Figure":
                secondParaClass = Figure.class;
                break;
            default:
                throw new SyntaxException("错误：含有此运算 " + methodName + " 不支持的操作数");
        }

        RationalNumber result = null;

        try {
            /**
             * 此处需要调用非 public 方法，因此
             * 只能使用方法 getDeclaredMethod，而不能使用方法 getMethod
             */
            result = (RationalNumber) (RationalNumberOperation.class
                    .getDeclaredMethod(methodName,
                            RationalNumber.class, secondParaClass)
                    .invoke(null, first, second));
        } catch (InvocationTargetException exception) {
            Throwable realException = exception.getTargetException();
            if (realException instanceof CalculatorException) {
                throw (CalculatorException) realException;
            } else {
                throw new UnknownException("错误：发生了未知异常。");
            }
        } catch (NoSuchMethodException noSuchMethodException) {
            try {
                /**
                 * 如果上一个 try 块抛出此异常，有可能是因为这里没有给出与第二个形参相同类型的方法。
                 * 在这种情况下，将第二个形参转化为类型 RationalNumber 之后再次尝试调用
                 */
                result = (RationalNumber) (RationalNumberOperation.class
                        .getDeclaredMethod(methodName,
                                RationalNumber.class, RationalNumber.class)
                        .invoke(null, first, new RationalNumber(second)));
            } catch (Exception exception) {
                ExceptionTool.pkgException(exception);
            }
        } catch (Exception exception) {
            ExceptionTool.pkgException(exception);
        }

        /**
         * 如果在执行这个方法之前，操作数被交换过，就执行相应的补充运算
         */
        if (hasSwap) {
            switch (methodName) {
                case "subtract":
                    result = RationalNumberOperation.getOpposite(result);
                    break;
                case "divide":
                    result = RationalNumberOperation.getReciprocal(result);
                    break;
                /**
                 * 加法、乘法不需要进行补充运算
                 */
                case "add":
                case "multiply":
                    break;
                default:
                    throw new SyntaxException("错误：不支持此运算 " + methodName);
            }
        }

        return result;
    }

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
        throw new SyntaxException("错误：有理数不支持乘方");
    }

}
