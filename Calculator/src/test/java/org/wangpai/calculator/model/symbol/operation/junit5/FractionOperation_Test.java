package org.wangpai.calculator.model.symbol.operation.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.operand.Fraction;
import org.wangpai.calculator.model.symbol.operation.FractionOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;


import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FractionOperation_Test {
    private int firstInt = 234234;
    private int secondInt = 2341;

    @Test
    void methodNavigation() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        // 交换标志为 false
        assertEquals(new Fraction(firstInt - secondInt),
                FractionOperation.methodNavigation(
                        "subtract", new Fraction(firstInt),
                        new Fraction(secondInt), false));

        // 交换标志为 true
        assertEquals(new Fraction(secondInt - firstInt),
                FractionOperation.methodNavigation(
                        "subtract", new Fraction(firstInt),
                        new Fraction(secondInt), true));
    }

    @Test
    void templateOperation() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Fraction(firstInt - secondInt),
                templateOperation("subtract",
                        new Fraction(firstInt), new Fraction(secondInt)));
    }

    @Test
    void add() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Fraction(firstInt + secondInt),
                add(new Fraction(firstInt), new Fraction(secondInt)));
    }

    @Test
    void subtract() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Fraction(firstInt - secondInt),
                subtract(new Fraction(firstInt), new Fraction(secondInt)));
    }

    @Test
    void multiply() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Fraction(firstInt * secondInt),
                multiply(new Fraction(firstInt), new Fraction(secondInt)));
    }

    @Test
    void divide() {
        Throwable throwable = assertThrows(SyntaxException.class,
                () -> divide(new Fraction(firstInt), new Fraction(secondInt)));
        assertEquals("错误：整数不支持除法运算", throwable.getMessage());
    }

    @Test
    void divideAndRemainder_Fraction() {
        var result = FractionOperation.divideAndRemainder(
                new Fraction(firstInt), new Fraction(secondInt));
        // 测试商是否正确
        assertEquals(new Fraction(firstInt / secondInt), result[0]);
        // 测试余数是否正确
        assertEquals(new Fraction(firstInt % secondInt), result[1]);
    }

    @Test
    void divideAndRemainder_long() {
        var result = FractionOperation.divideAndRemainder(
                new Fraction(firstInt), secondInt);
        // 测试商是否正确
        assertEquals(new Fraction(firstInt / secondInt), result[0]);
        // 测试余数是否正确
        assertEquals(new Fraction(firstInt % secondInt), result[1]);
    }

    @Test
    void mod_Fraction() {
        assertEquals(new Fraction(firstInt % secondInt),
                FractionOperation.mod(
                        new Fraction(firstInt), new Fraction(secondInt)));
    }

    @Test
    void mod_long() {
        assertEquals(new Fraction(firstInt % secondInt),
                FractionOperation.mod(
                        new Fraction(firstInt), secondInt));
    }

    @Test
    void modsQuotient_Fraction() {
        assertEquals(new Fraction(firstInt / secondInt),
                FractionOperation.modsQuotient(
                        new Fraction(firstInt), new Fraction(secondInt)));
    }

    @Test
    void modsQuotient_long() {
        assertEquals(new Fraction(firstInt / secondInt),
                FractionOperation.modsQuotient(
                        new Fraction(firstInt), secondInt));
    }

    @Test
    void getOpposite_Fraction() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Fraction(-firstInt),
                FractionOperation.getOpposite(new Fraction(firstInt)));
    }

    @Test
    void getOpposite_long() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Fraction(-firstInt),
                FractionOperation.getOpposite(firstInt));
    }

    @Test
    void findGcd() throws NoSuchMethodException, CalculatorException, CloneNotSupportedException, IllegalAccessException {
        assertEquals(findGcd_forTest(
                new Fraction(firstInt), new Fraction(secondInt)),
                FractionOperation.findGcd(
                        new Fraction(firstInt), new Fraction(secondInt)));
    }

    @Test
    void findLcm() throws NoSuchMethodException, IllegalAccessException, CalculatorException, InvocationTargetException, CloneNotSupportedException {
        assertEquals(findLcm_forTest(
                new Fraction(firstInt), new Fraction(secondInt)),
                FractionOperation.findLcm(new Fraction(firstInt), new Fraction(secondInt)));
    }

    @Test
    void power() throws CalculatorException {
        assertEquals(new Fraction(8),
                FractionOperation.power(2, 3));
    }




    /**
     * 求两个数的最大公约数。GCD：Greatest Common Divisor
     *
     * 注意事项：
     * > 当这两个数只有一个为 0 时，结果为另一个数的绝对值。
     * > 特别地，当这两个数均为 0 时，结果为 0。
     * > 其它情况下，结果为正数
     */
    Fraction findGcd_forTest(Fraction first, Fraction second) throws CloneNotSupportedException, NoSuchMethodException, IllegalAccessException, CalculatorException {
        if (first.isZero()) {
            return second;
        }
        if (second.isZero()) {
            return first;
        }

        var firstClone = first.clone();
        var secondClone = second.clone();

        if (firstClone.isNegative()) {
            firstClone = FractionOperation.getOpposite(firstClone);
        }
        if (secondClone.isNegative()) {
            secondClone = FractionOperation.getOpposite(secondClone);
        }

        while (!secondClone.isZero()) {
            var middleResult = FractionOperation.mod(firstClone, secondClone);
            firstClone = secondClone;
            secondClone = middleResult;
        }

        return firstClone;
    }

    /**
     * 求两个数的最小公倍数。LCM：Least Common Multiple
     *
     * 算法如下：先得出这两个数的最大公约数，
     *         然后将其中一个数除以最大公约数，得到其中一个质因子（prime factor）
     *         最后将该质因子另外一个没有除以过公约数的数相乘
     */
    Fraction findLcm_forTest(Fraction first, Fraction second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException, CloneNotSupportedException, InvocationTargetException {
        return (Fraction) Operation.multiply(first,
                FractionOperation.subtract(
                        second, findGcd_forTest(first, second)));
    }

    /*---------------模拟的原生待测方法---------------*/

    Fraction templateOperation(String methodName, Fraction first, Fraction second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        Fraction result;
        try {
            var methodToBeTested = FractionOperation.class
                    .getDeclaredMethod("templateOperation",
                            String.class, Fraction.class, Fraction.class);
            methodToBeTested.setAccessible(true);
            result = (Fraction) methodToBeTested.invoke(
                    null, methodName, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;

    }

    Fraction add(Fraction first, Fraction second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        Fraction result;
        try {
            var methodToBeTested = FractionOperation.class
                    .getDeclaredMethod("add",
                            Fraction.class, Fraction.class);
            methodToBeTested.setAccessible(true);
            result = (Fraction) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    Fraction subtract(Fraction first, Fraction second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        Fraction result;
        try {
            var methodToBeTested = FractionOperation.class
                    .getDeclaredMethod("subtract",
                            Fraction.class, Fraction.class);
            methodToBeTested.setAccessible(true);
            result = (Fraction) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    Fraction multiply(Fraction first, Fraction second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        Fraction result;
        try {
            var methodToBeTested = FractionOperation.class
                    .getDeclaredMethod("multiply",
                            Fraction.class, Fraction.class);
            methodToBeTested.setAccessible(true);
            result = (Fraction) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    Fraction divide(Fraction first, Fraction second) throws SyntaxException {
        Fraction result;
        try {
            var methodToBeTested = FractionOperation.class
                    .getDeclaredMethod("divide",
                            Fraction.class, Fraction.class);
            methodToBeTested.setAccessible(true);
            result = (Fraction) methodToBeTested.invoke(
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