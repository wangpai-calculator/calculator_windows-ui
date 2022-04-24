package org.wangpai.calculator.model.symbol.operation;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UnknownException;
import org.wangpai.calculator.exception.tool.ExceptionTool;
import org.wangpai.calculator.model.symbol.operand.Figure;
import org.wangpai.calculator.model.symbol.operand.Operand;

/**
 * 此类不定义整数除法运算
 *
 * @since 2021-8-2
 */
public final class FigureOperation extends Operation {
    /*---------------加减乘---------------*/

    /**
     * 此函数将用于导航到最终的运算函数。
     * 最终的运算函数指的是每个操作数的类型均与方法签名中标明的一致的函数
     *
     * @deprecated 2021-8-5
     * 此方法为使用了反射，为废弃方法，仅基类可以调用
     */
    @Deprecated
    protected final static Figure methodNavigation(
            String methodName, Figure first,
            Operand second, boolean hasSwap)
            throws CalculatorException {
        var firstClass = first.getClass();
        if (firstClass != Figure.class) {
            throw new SyntaxException("错误：含有此运算不支持的操作数类型 " + firstClass);
        }

        Class<?> secondParaClass;
        switch (second.getClass().getSimpleName()) {
            case "Figure":
                secondParaClass = Figure.class;
                break;
            default:
                throw new SyntaxException("错误：含有此运算 " + methodName + " 不支持的操作数");
        }

        Figure result = null;

        try {
            /**
             * 此处需要调用非 public 方法，因此
             * 只能使用方法 getDeclaredMethod，而不能使用方法 getMethod
             */
            result = (Figure) (FigureOperation.class
                    .getDeclaredMethod(methodName,
                            Figure.class, secondParaClass)
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
                 * 在这种情况下，将第二个形参转化为类型 Figure 之后再次尝试调用
                 */
                result = (Figure) (FigureOperation.class
                        .getDeclaredMethod(methodName,
                                Figure.class, Figure.class)
                        .invoke(null, first, new Figure(second)));
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
                    result = FigureOperation.getOpposite(result);
                    break;
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
     * 注意：此处的 methodName 是 Figure 底层的字段类型 BigInteger 的运算函数名，
     * 不是 FigureOperation 的运算函数名
     *
     * @deprecated 2021-8-5
     * 此方法为使用了反射，为废弃方法
     */
    @Deprecated
    private static Figure templateOperation(
            String methodName, Figure first, Figure second)
            throws CalculatorException {
        var firstValue = first.getInteger();
        var secondValue = second.getInteger();
        var className = BigInteger.class;

        BigInteger result = null;

        try {
            result = (BigInteger) (className
                    .getMethod(methodName, className)
                    .invoke(firstValue, secondValue));
        } catch (InvocationTargetException exception) {
            Throwable realException = exception.getTargetException();
            if (realException instanceof CalculatorException) {
                throw (CalculatorException) realException;
            } else {
                ExceptionTool.pkgException(exception);
            }
        } catch (Exception exception) {
            ExceptionTool.pkgException(exception);
        }

        return new Figure(result);
    }

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
    public final static Figure getOpposite(Figure rationalNumber) {
        return multiply(rationalNumber, new Figure(-1));
    }

    /**
     * @since before 2021-8-5
     */
    public final static Figure getOpposite(long num) {
        return getOpposite(new Figure(num));
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
     * 整数的乘方
     *
     * @since before 2021-8-5
     */
    public static Figure power(Figure base, Figure exponent)
            throws SyntaxException {
        if (base.isZero()) {
            throw new SyntaxException("错误：0 不能作为乘方的底数");
        }

        return new Figure(base.getInteger().pow(exponent.getInteger().intValue()));
    }

    public static Figure power(long base, long exponent)
            throws SyntaxException {
        return FigureOperation.power(new Figure(base), new Figure(exponent));
    }

}
