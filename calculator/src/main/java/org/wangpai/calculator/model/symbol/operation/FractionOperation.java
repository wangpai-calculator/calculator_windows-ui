package org.wangpai.calculator.model.symbol.operation;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UnknownException;
import org.wangpai.calculator.exception.tool.ExceptionTool;
import org.wangpai.calculator.model.symbol.operand.Fraction;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;


import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

/**
 * 此类不定义整数除法运算
 */
public class FractionOperation extends Operation {
    /*---------------加减乘---------------*/

    /**
     * 此函数将用于导航到最终的运算函数。
     * 最终的运算函数指的是每个操作数的类型均与方法签名中标明的一致的函数
     */
    public final static Fraction methodNavigation(
            String methodName, Fraction first,
            Operand second, boolean hasSwap)
            throws CalculatorException {
        var firstClass = first.getClass();
        if (firstClass != Fraction.class) {
            throw new SyntaxException("错误：含有此运算不支持的操作数类型 " + firstClass);
        }

        Class<?> secondParaClass;
        switch (second.getClass().getSimpleName()) {
            case "Fraction":
                secondParaClass = Fraction.class;
                break;
            default:
                throw new SyntaxException("错误：含有此运算 " + methodName + " 不支持的操作数");
        }

        Fraction result;

        try {
            /**
             * 此处需要调用非 public 方法，因此
             * 只能使用方法 getDeclaredMethod，而不能使用方法 getMethod
             */
            result = (Fraction) (FractionOperation.class
                    .getDeclaredMethod(methodName,
                            Fraction.class, secondParaClass)
                    .invoke(null, first, second));
        } catch (InvocationTargetException exception) {
            Throwable realException = exception.getTargetException();
            if (realException instanceof CalculatorException) {
                throw (CalculatorException) realException;
            } else {
                throw new UnknownException("错误：发生了未知异常。");
            }
        } catch (NoSuchMethodException exception) {
            throw (UnknownException)
                    new UnknownException("错误：发生了 NoSuchMethodException 异常")
                            .initCause(exception);
        } catch (IllegalAccessException exception) {
            throw (UnknownException)
                    new UnknownException("错误：发生了 IllegalAccessException 异常")
                            .initCause(exception);
        }

        /**
         * 如果在执行这个方法之前，操作数被交换过，就执行相应的补充运算
         */
        if (hasSwap) {
            switch (methodName) {
                case "subtract":
                    result = FractionOperation.getOpposite(result);
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
     * 注意：此处的 methodName 是 Fraction 底层的字段类型 BigInteger 的运算函数名，
     * 不是 FractionOperation 的运算函数名
     */
    private static Fraction templateOperation(
            String methodName, Fraction first, Fraction second)
            throws CalculatorException {
        var firstValue = first.getFraction();
        var secondValue = second.getFraction();
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

        return new Fraction(result);
    }

    /**
     * add 的最终的运算函数
     */
    protected static Fraction add(Fraction first, Fraction second)
            throws CalculatorException {
        return templateOperation("add", first, second);
    }

    /**
     * subtract 的最终的运算函数
     */
    protected static Fraction subtract(Fraction first, Fraction second)
            throws CalculatorException {
        return templateOperation("subtract", first, second);
    }

    /**
     * multiply 的最终的运算函数
     */
    protected static Fraction multiply(Fraction first, Fraction second)
            throws CalculatorException {
        return templateOperation("multiply", first, second);
    }


    /**
     * 占位空方法
     */
    protected final static Fraction divide(
            Fraction first, Fraction second) throws SyntaxException {
        throw new SyntaxException("错误：整数不支持除法运算");
    }

    /****************加减乘****************/

    /**
     * @return 返回的数组中，0 号元素代表商，1 号元素代表余数
     */
    public static Fraction[] divideAndRemainder(Fraction first, Fraction second) {
        BigInteger[] quotientAndRemainder = first.getFraction()
                .divideAndRemainder(second.getFraction());
        Fraction[] result = new Fraction[quotientAndRemainder.length];
        for (int index = 0; index < quotientAndRemainder.length; ++index) {
            result[index] = new Fraction(quotientAndRemainder[index]);
        }
        return result;

    }

    public static Fraction[] divideAndRemainder(Fraction first, long second) {
        return divideAndRemainder(first, new Fraction(second));
    }

    /**
     * @return 返回求余数运算得到的余数
     */
    public static Fraction mod(Fraction first, Fraction second) {
        final int REMAINDER_INDEX = 1;
        return divideAndRemainder(first, second)[REMAINDER_INDEX];
    }

    public static Fraction mod(Fraction first, long second) {
        return mod(first, new Fraction(second));
    }

    /**
     * modsQuotient：mod's Quotient 求余数的商
     *
     * @return 返回求余数运算得到的商
     */
    public static Fraction modsQuotient(Fraction first, Fraction second) {
        final int QUOTIENT_INDEX = 0;
        return divideAndRemainder(first, second)[QUOTIENT_INDEX];
    }

    public static Fraction modsQuotient(Fraction first, long second) {
        return modsQuotient(first, new Fraction(second));
    }

    /**
     * 求相反数
     */
    public final static Fraction getOpposite(Fraction rationalNumber)
            throws CalculatorException {
        return multiply(rationalNumber, new Fraction(-1));
    }

    public final static Fraction getOpposite(long num)
            throws CalculatorException {
        return getOpposite(new Fraction(num));
    }


    /**
     * 求两个数的最大公约数。GCD：Greatest Common Divisor
     *
     * 注意事项：
     * > 当这两个数只有一个为 0 时，结果为另一个数的绝对值。
     * > 特别地，当这两个数均为 0 时，结果为 0。
     * > 其它情况下，结果为正数
     */
    public static Fraction findGcd(Fraction first, Fraction second) {
        /** // 这是我之前自己编写的实现，后来发现 BigInteger 中已经有了这种方法了。
         *
         * 注意：如果这两个数都是负数，则公约数也为负数。
         *      如果只有一个为负，则公约数有可能为负，有可能为正
        var firstClone = first.clone();
        var secondClone = second.clone();
        while (!secondClone.isZero()) {
            var middleResult = mod(firstClone, secondClone);
            firstClone = second;
            secondClone = middleResult;
        }

        return firstClone;
         */

        return new Fraction(first.clone().getFraction()
                .gcd(second.clone().getFraction()));
    }

    /**
     * 求两个数的最小公倍数。LCM：Least Common Multiple
     *
     * 算法如下：先得出这两个数的最大公约数，
     *         然后将其中一个数除以最大公约数，得到其中一个质因子（prime factor）
     *         最后将该质因子另外一个没有除以过公约数的数相乘
     */
    public static Fraction findLcm(Fraction first, Fraction second)
            throws CalculatorException {
        return multiply(first, subtract(second, findGcd(first, second)));
    }


    /**
     * 整数的乘方
     */
    public static Fraction power(Fraction base, Fraction exponent)
            throws CalculatorException {
        if (base.isZero()) {
            throw new SyntaxException("错误：0 不能作为乘方的底数");
        }

        return new Fraction(base.getFraction().pow(exponent.getFraction().intValue()));
    }

    public static Fraction power(long base, long exponent)
            throws CalculatorException {
        return FractionOperation.power(new Fraction(base), new Fraction(exponent));
    }

}
