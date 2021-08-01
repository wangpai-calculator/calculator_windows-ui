package org.wangpai.calculator.model.symbol.operation.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;
import org.wangpai.calculator.model.symbol.operation.RationalNumberOperation;


import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class RationalNumberOperation_Test {
    private int firstInt = 234234;
    private int secondInt = 2341;
//    private RationalNumber firstR =
//            new RationalNumber(34,456);
//    private RationalNumber secondR =
//            new RationalNumber(45,5236);

    RationalNumberOperation_Test() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
    }

    @Test
    void methodNavigation() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
        // 交换标志为 false
        assertEquals(new RationalNumber(1, secondInt),
                RationalNumberOperation.methodNavigation(
                        "divide", new RationalNumber(1, 1),
                        new RationalNumber(secondInt, 1), false));

        // 交换标志为 true
        assertEquals(new RationalNumber(firstInt, 1),
                RationalNumberOperation.methodNavigation(
                        "divide", new RationalNumber(1, 1),
                        new RationalNumber(firstInt, 1), true));
    }

    @Test
    void add_R() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
        // 3/4 = 5/7 + 1/28
        assertEquals(new RationalNumber(3,4),
                add(new RationalNumber(5,7),
                        new RationalNumber(1,28)));
    }

    @Test
    void subtract_R() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
        // 1/6 = 1/2 - 1/3
        assertEquals(new RationalNumber(1, 6),
                subtract(new RationalNumber(1, 2),
                        new RationalNumber(1, 3)));
    }

    @Test
    void multiply_R() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
        // 1/3 = 5/6 x 2/5
        assertEquals(new RationalNumber(1, 3),
                multiply(new RationalNumber(5, 6),
                        new RationalNumber(2, 5)));
    }

    @Test
    void multiply_long() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
        // 5/3 = 1/6 x 10
        assertEquals(new RationalNumber(5, 3),
                multiply(new RationalNumber(1, 6), 10));
    }

    @Test
    void divide_R() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
        // 5/6 = 1/3 / 2/5
        assertEquals(new RationalNumber(5, 6),
                divide(new RationalNumber(1, 3),
                        new RationalNumber(2, 5)));

        // 0 作除数引发异常
        Throwable throwable = assertThrows(SyntaxException.class,
                () -> divide(new RationalNumber(1, 3),
                        new RationalNumber(0)));
        assertEquals("错误：0 不能作除数", throwable.getMessage());
    }

    @Test
    void getOpposite() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
        assertEquals(new RationalNumber(-firstInt, secondInt),
                RationalNumberOperation.getOpposite
                        (new RationalNumber(firstInt, secondInt)));
    }

    @Test
    void getReciprocal() throws NoSuchMethodException, IllegalAccessException, CloneNotSupportedException, CalculatorException {
        assertEquals(new RationalNumber(secondInt, firstInt),
                RationalNumberOperation.getReciprocal
                        (new RationalNumber(firstInt, secondInt)));
    }

    /*---------------模拟的原生待测方法---------------*/

    RationalNumber add(RationalNumber first, RationalNumber second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        RationalNumber result;
        try {
            var methodToBeTested = RationalNumberOperation.class
                    .getDeclaredMethod("add",
                            RationalNumber.class, RationalNumber.class);
            methodToBeTested.setAccessible(true);
            result = (RationalNumber) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    RationalNumber subtract(RationalNumber first, RationalNumber second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        RationalNumber result;
        try {
            var methodToBeTested = RationalNumberOperation.class
                    .getDeclaredMethod("subtract",
                            RationalNumber.class, RationalNumber.class);
            methodToBeTested.setAccessible(true);
            result = (RationalNumber) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    RationalNumber multiply(RationalNumber first, RationalNumber second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        RationalNumber result;
        try {
            var methodToBeTested = RationalNumberOperation.class
                    .getDeclaredMethod("multiply",
                            RationalNumber.class, RationalNumber.class);
            methodToBeTested.setAccessible(true);
            result = (RationalNumber) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    RationalNumber multiply(RationalNumber first, long second)
            throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        RationalNumber result;
        try {
            var methodToBeTested = RationalNumberOperation.class
                    .getDeclaredMethod("multiply",
                            RationalNumber.class, long.class);
            methodToBeTested.setAccessible(true);
            result = (RationalNumber) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    RationalNumber divide(RationalNumber first, RationalNumber second) throws CalculatorException {
        RationalNumber result;
        try {
            var methodToBeTested = RationalNumberOperation.class
                    .getDeclaredMethod("divide",
                            RationalNumber.class, RationalNumber.class);
            methodToBeTested.setAccessible(true);
            result = (RationalNumber) methodToBeTested.invoke(
                    null, first, second);
        } catch (NoSuchMethodException | InvocationTargetException
                | IllegalAccessException exception) {
            Throwable realException = exception.getCause();
            throw (CalculatorException) realException;
        }

        return result;
    }

    /****************模拟的原生待测方法****************/


}