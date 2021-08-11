package org.wangpai.calculator.model.symbol.operation.junit5;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.operand.Figure;
import org.wangpai.calculator.model.symbol.operation.FigureOperation;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @since 2021-7-30
 */
public class FigureOperation_Test {
    private int firstInt = 234234;
    private int secondInt = 2341;

    @Test
    public void test_templateOperation() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(firstInt - secondInt),
                templateOperation("subtract",
                        new Figure(firstInt), new Figure(secondInt)));
    }

    @Test
    public void test_add() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(firstInt + secondInt),
                add(new Figure(firstInt), new Figure(secondInt)));
    }

    @Test
    public void test_subtract() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(firstInt - secondInt),
                subtract(new Figure(firstInt), new Figure(secondInt)));
    }

    @Test
    public void test_multiply() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(firstInt * secondInt),
                multiply(new Figure(firstInt), new Figure(secondInt)));
    }

    @Test
    public void test_divide() {
        Throwable throwable = assertThrows(SyntaxException.class,
                () -> divide(new Figure(firstInt), new Figure(secondInt)));
        assertEquals("错误：整数不支持除法运算", throwable.getMessage());
    }

    @Test
    public void test_divideAndRemainder_Fraction() {
        var result = FigureOperation.divideAndRemainder(
                new Figure(firstInt), new Figure(secondInt));
        // 测试商是否正确
        assertEquals(new Figure(firstInt / secondInt), result[0]);
        // 测试余数是否正确
        assertEquals(new Figure(firstInt % secondInt), result[1]);
    }

    @Test
    public void test_divideAndRemainder_long() {
        var result = FigureOperation.divideAndRemainder(
                new Figure(firstInt), secondInt);
        // 测试商是否正确
        assertEquals(new Figure(firstInt / secondInt), result[0]);
        // 测试余数是否正确
        assertEquals(new Figure(firstInt % secondInt), result[1]);
    }

    @Test
    public void test_mod_Fraction() {
        assertEquals(new Figure(firstInt % secondInt),
                FigureOperation.mod(
                        new Figure(firstInt), new Figure(secondInt)));
    }

    @Test
    public void test_mod_long() {
        assertEquals(new Figure(firstInt % secondInt),
                FigureOperation.mod(
                        new Figure(firstInt), secondInt));
    }

    @Test
    public void test_modsQuotient_Fraction() {
        assertEquals(new Figure(firstInt / secondInt),
                FigureOperation.modsQuotient(
                        new Figure(firstInt), new Figure(secondInt)));
    }

    @Test
    public void test_modsQuotient_long() {
        assertEquals(new Figure(firstInt / secondInt),
                FigureOperation.modsQuotient(
                        new Figure(firstInt), secondInt));
    }

    @Test
    public void test_getOpposite_Fraction() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(-firstInt),
                FigureOperation.getOpposite(new Figure(firstInt)));
    }

    @Test
    public void test_getOpposite_long() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(-firstInt),
                FigureOperation.getOpposite(firstInt));
    }

    @Test
    public void test_findGcd() throws NoSuchMethodException, CalculatorException, CloneNotSupportedException, IllegalAccessException {
        assertEquals(findGcd_forTest(
                new Figure(firstInt), new Figure(secondInt)),
                FigureOperation.findGcd(
                        new Figure(firstInt), new Figure(secondInt)));
    }

    @Test
    public void test_findLcm() throws NoSuchMethodException, IllegalAccessException, CalculatorException, InvocationTargetException, CloneNotSupportedException {
        assertEquals(findLcm_forTest(
                new Figure(firstInt), new Figure(secondInt)),
                FigureOperation.findLcm(new Figure(firstInt), new Figure(secondInt)));
    }

    @Test
    public void test_power() throws CalculatorException {
        assertEquals(new Figure(8),
                FigureOperation.power(2, 3));
    }


    /**
     * 求两个数的最大公约数。GCD：Greatest Common Divisor
     * <p>
     * 注意事项：
     * > 当这两个数只有一个为 0 时，结果为另一个数的绝对值。
     * > 特别地，当这两个数均为 0 时，结果为 0。
     * > 其它情况下，结果为正数
     */
    Figure findGcd_forTest(Figure first, Figure second) throws CloneNotSupportedException, NoSuchMethodException, IllegalAccessException, CalculatorException {
        if (first.isZero()) {
            return second;
        }
        if (second.isZero()) {
            return first;
        }

        var firstClone = first.clone();
        var secondClone = second.clone();

        if (firstClone.isNegative()) {
            firstClone = FigureOperation.getOpposite(firstClone);
        }
        if (secondClone.isNegative()) {
            secondClone = FigureOperation.getOpposite(secondClone);
        }

        while (!secondClone.isZero()) {
            var middleResult = FigureOperation.mod(firstClone, secondClone);
            firstClone = secondClone;
            secondClone = middleResult;
        }

        return firstClone;
    }

    /**
     * 求两个数的最小公倍数。LCM：Least Common Multiple
     * <p>
     * 算法如下：先得出这两个数的最大公约数，
     * 然后将其中一个数除以最大公约数，得到其中一个质因子（prime factor）
     * 最后将该质因子另外一个没有除以过公约数的数相乘
     */
    Figure findLcm_forTest(Figure first, Figure second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException, CloneNotSupportedException, InvocationTargetException {
        return (Figure) FigureOperation.multiply(first,
                FigureOperation.subtract(
                        second, findGcd_forTest(first, second)));
    }

    /*---------------模拟的原生待测方法---------------*/

    Figure templateOperation(String methodName, Figure first, Figure second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        Figure result;
        try {
            var methodToBeTested = FigureOperation.class
                    .getDeclaredMethod("templateOperation",
                            String.class, Figure.class, Figure.class);
            methodToBeTested.setAccessible(true);
            result = (Figure) methodToBeTested.invoke(
                    null, methodName, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;

    }

    Figure add(Figure first, Figure second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        Figure result;
        try {
            var methodToBeTested = FigureOperation.class
                    .getDeclaredMethod("add",
                            Figure.class, Figure.class);
            methodToBeTested.setAccessible(true);
            result = (Figure) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    Figure subtract(Figure first, Figure second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        Figure result;
        try {
            var methodToBeTested = FigureOperation.class
                    .getDeclaredMethod("subtract",
                            Figure.class, Figure.class);
            methodToBeTested.setAccessible(true);
            result = (Figure) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    Figure multiply(Figure first, Figure second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        Figure result;
        try {
            var methodToBeTested = FigureOperation.class
                    .getDeclaredMethod("multiply",
                            Figure.class, Figure.class);
            methodToBeTested.setAccessible(true);
            result = (Figure) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    Figure divide(Figure first, Figure second) throws SyntaxException {
        Figure result;
        try {
            var methodToBeTested = FigureOperation.class
                    .getDeclaredMethod("divide",
                            Figure.class, Figure.class);
            methodToBeTested.setAccessible(true);
            result = (Figure) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (SyntaxException) realException;
        }

        return result;
    }

    /****************模拟的原生待测方法****************/

}
