package org.wangpai.calculator.model.symbol.operation;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.operand.Figure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @since 2021-7-30
 */
public class FigureOperationTest {
    private int firstInt = 234234;
    private int secondInt = 2341;

    @Test
    public void templateOperation() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(this.firstInt - this.secondInt),
                this.templateOperation("subtract",
                        new Figure(this.firstInt), new Figure(this.secondInt)));
    }

    @Test
    public void add() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(this.firstInt + this.secondInt),
                this.add(new Figure(this.firstInt), new Figure(this.secondInt)));
    }

    @Test
    public void subtract() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(this.firstInt - this.secondInt),
                this.subtract(new Figure(this.firstInt), new Figure(this.secondInt)));
    }

    @Test
    public void multiply() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(this.firstInt * this.secondInt),
                this.multiply(new Figure(this.firstInt), new Figure(this.secondInt)));
    }

    @Test
    public void divide() {
        Throwable throwable = assertThrows(SyntaxException.class,
                () -> this.divide(new Figure(this.firstInt), new Figure(this.secondInt)));
        assertEquals("错误：整数不支持除法运算", throwable.getMessage());
    }

    @Test
    public void divideAndRemainder_Fraction() {
        var result = FigureOperation.divideAndRemainder(
                new Figure(this.firstInt), new Figure(this.secondInt));
        // 测试商是否正确
        assertEquals(new Figure(this.firstInt / this.secondInt), result[0]);
        // 测试余数是否正确
        assertEquals(new Figure(this.firstInt % this.secondInt), result[1]);
    }

    @Test
    public void divideAndRemainder_long() {
        var result = FigureOperation.divideAndRemainder(
                new Figure(this.firstInt), this.secondInt);
        // 测试商是否正确
        assertEquals(new Figure(this.firstInt / this.secondInt), result[0]);
        // 测试余数是否正确
        assertEquals(new Figure(this.firstInt % this.secondInt), result[1]);
    }

    @Test
    public void mod_Fraction() {
        assertEquals(new Figure(this.firstInt % this.secondInt),
                FigureOperation.mod(
                        new Figure(this.firstInt), new Figure(this.secondInt)));
    }

    @Test
    public void mod_long() {
        assertEquals(new Figure(this.firstInt % this.secondInt),
                FigureOperation.mod(
                        new Figure(this.firstInt), this.secondInt));
    }

    @Test
    public void modsQuotient_Fraction() {
        assertEquals(new Figure(this.firstInt / this.secondInt),
                FigureOperation.modsQuotient(
                        new Figure(this.firstInt), new Figure(this.secondInt)));
    }

    @Test
    public void modsQuotient_long() {
        assertEquals(new Figure(this.firstInt / this.secondInt),
                FigureOperation.modsQuotient(
                        new Figure(this.firstInt), this.secondInt));
    }

    @Test
    public void getOpposite_Fraction() throws NoSuchMethodException, IllegalAccessException, CalculatorException {
        assertEquals(new Figure(-this.firstInt),
                FigureOperation.getOpposite(new Figure(this.firstInt)));
    }

    @Test
    public void getAbsolute() {
        assertEquals(new Figure(this.firstInt),
                FigureOperation.getAbsolute(new Figure(-this.firstInt)));
    }

    @Test
    public void getOpposite_long() {
        assertEquals(new Figure(-this.firstInt),
                FigureOperation.getOpposite(this.firstInt));
    }

    @Test
    public void findGcd() {
        assertEquals(this.findGcd_forTest(
                        new Figure(this.firstInt), new Figure(this.secondInt)),
                FigureOperation.findGcd(
                        new Figure(this.firstInt), new Figure(this.secondInt)));
    }

    @Test
    public void findLcm() throws CalculatorException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, CloneNotSupportedException {
        assertEquals(this.findLcm_forTest(
                        new Figure(this.firstInt), new Figure(this.secondInt)),
                FigureOperation.findLcm(new Figure(this.firstInt), new Figure(this.secondInt)));
    }

    @Test
    public void power() throws CalculatorException {
        // 8 = 2 ^ 3
        assertEquals(new Figure(8),
                FigureOperation.power(2, 3));
        // ? = (?) ^ (1)
        assertEquals(new Figure(this.firstInt),
                FigureOperation.power(new Figure(this.firstInt), Figure.ONE));
        // 1 = (?) ^ (0)
        assertEquals(Figure.ONE,
                FigureOperation.power(new Figure(this.firstInt), Figure.ZERO));
    }

    /**
     * 求两个数的最大公约数。GCD：Greatest Common Divisor
     * <p>
     * 注意事项：
     * > 当这两个数只有一个为 0 时，结果为另一个数的绝对值。
     * > 特别地，当这两个数均为 0 时，结果为 0。
     * > 其它情况下，结果为正数
     */
    Figure findGcd_forTest(Figure first, Figure second) {
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
                        second, this.findGcd_forTest(first, second)));
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
