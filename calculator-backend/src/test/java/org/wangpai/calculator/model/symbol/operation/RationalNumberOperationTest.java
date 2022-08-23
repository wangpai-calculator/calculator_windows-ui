package org.wangpai.calculator.model.symbol.operation;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.operand.Figure;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @since 2021-7-22
 */
public class RationalNumberOperationTest {
    private int firstInt = 234234;
    private int secondInt = 2341;

    RationalNumberOperationTest() {
    }

    @Test
    public void add_R() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        // 3/4 = 5/7 + 1/28
        assertEquals(new RationalNumber(3, 4),
                this.add(new RationalNumber(5, 7),
                        new RationalNumber(1, 28)));
    }

    @Test
    public void subtract_R() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        // 1/6 = 1/2 - 1/3
        assertEquals(new RationalNumber(1, 6),
                this.subtract(new RationalNumber(1, 2),
                        new RationalNumber(1, 3)));
    }

    @Test
    public void multiply_R() throws CalculatorException {
        // 1/3 = 5/6 x 2/5
        assertEquals(new RationalNumber(1, 3),
                this.multiply(new RationalNumber(5, 6),
                        new RationalNumber(2, 5)));
    }

    @Test
    public void multiply_long() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        // 5/3 = 1/6 x 10
        assertEquals(new RationalNumber(5, 3),
                this.multiply(new RationalNumber(1, 6), 10));
    }

    @Test
    public void divide_R() throws CalculatorException {
        // 5/6 = 1/3 / 2/5
        assertEquals(new RationalNumber(5, 6),
                this.divide(new RationalNumber(1, 3),
                        new RationalNumber(2, 5)));

        // 0 作除数引发异常
        Throwable throwable = assertThrows(SyntaxException.class,
                () -> this.divide(new RationalNumber(1, 3),
                        new RationalNumber(0)));
        assertEquals("错误：0 不能作除数", throwable.getMessage());
    }

    @Test
    public void getOpposite() throws CalculatorException {
        assertEquals(new RationalNumber(-this.firstInt, this.secondInt),
                RationalNumberOperation.getOpposite
                        (new RationalNumber(this.firstInt, this.secondInt)));
    }

    @Test
    public void getAbsolute() throws CalculatorException {
        assertEquals(new RationalNumber(this.firstInt, this.secondInt),
                RationalNumberOperation.getAbsolute
                        (new RationalNumber(this.firstInt, -this.secondInt)));
    }

    @Test
    public void getReciprocal() throws CalculatorException {
        assertEquals(new RationalNumber(this.secondInt, this.firstInt),
                RationalNumberOperation.getReciprocal
                        (new RationalNumber(this.firstInt, this.secondInt)));
    }

    @Test
    public void power() throws CalculatorException {
        // 8 = 2 ^ 3
        assertEquals(new RationalNumber(8),
                RationalNumberOperation.power(new RationalNumber(2), new Figure(3)));
        // 1/8 = 2 ^ (-3)
        assertEquals(new RationalNumber(1, 8),
                RationalNumberOperation.power(new RationalNumber(2), new Figure(-3)));
        // -1/8 = (-2) ^ (-3)
        assertEquals(new RationalNumber(-1, 8),
                RationalNumberOperation.power(new RationalNumber(-2), new Figure(-3)));
        // ? = (?) ^ (1)
        assertEquals(new RationalNumber(this.firstInt, this.secondInt),
                RationalNumberOperation.power(new RationalNumber(this.firstInt, this.secondInt), Figure.ONE));
        // 1 = (?) ^ (0)
        assertEquals(new RationalNumber(1),
                RationalNumberOperation.power(new RationalNumber(this.firstInt, this.secondInt), Figure.ZERO));

        // 0 的负数次方引发异常
        assertThrows(SyntaxException.class,
                () -> RationalNumberOperation.power(new RationalNumber(0), new Figure(-3)));
    }

    /*---------------模拟的原生待测方法---------------*/

    RationalNumber add(RationalNumber first, RationalNumber second)
            throws CalculatorException {
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
            throws CalculatorException {
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
            throws CalculatorException {
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
            throws CalculatorException {
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
