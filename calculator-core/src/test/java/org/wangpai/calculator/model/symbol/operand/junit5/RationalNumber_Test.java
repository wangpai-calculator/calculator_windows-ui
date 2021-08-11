package org.wangpai.calculator.model.symbol.operand.junit5;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.symbol.operand.Figure;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;
import org.wangpai.calculator.model.symbol.operation.Operation;
import org.wangpai.calculator.model.symbol.operation.RationalNumberOperation;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @since 2021-7-31
 */
public class RationalNumber_Test {
    private final int numeratorForTest = 10;
    private final int denominatorForTest = 20;
    private final RationalNumber rationalNumber =
            new RationalNumber(numeratorForTest, denominatorForTest);

    /**
     * 此构造器不能去掉，因为字段初始化抛出了异常
     *
     * @since 2021-8-3
     */
    public RationalNumber_Test() throws CalculatorException {
    }

    /**
     * @since 2021-8-3
     */
    @Test
    public void test_RationalNumber_Operand() {
        assertDoesNotThrow(() -> new RationalNumber((Operand) rationalNumber));

        assertThrows(UndefinedException.class,
                () -> new RationalNumber((new Operand() {
                    @Override
                    public Class<? extends Operation> getBindingOperation() {
                        return null;
                    }

                    @Override
                    public boolean isZero() {
                        return false;
                    }
                })));
    }

    @Test
    public void test_getBindingOperation() {
        assertEquals(RationalNumberOperation.class,
                new RationalNumber().getBindingOperation());
    }

    @Test
    public void test_clone() {
        var cloned = rationalNumber.clone();

        assertEquals(rationalNumber, cloned); // 意义上相等
        assertFalse(cloned == rationalNumber); // 物理上不是同一个
    }

    @Test
    public void test_isZero() {
        assertTrue(new RationalNumber(0).isZero());

        assertFalse(new RationalNumber(1).isZero());
        assertFalse(rationalNumber.isZero());
    }

    @Test
    public void test_isNegative() throws CalculatorException {
        assertTrue(new RationalNumber(-1).isNegative());
        assertFalse(new RationalNumber(0).isNegative());
        assertFalse(new RationalNumber(1).isNegative());

        assertTrue(new RationalNumber(-1,
                denominatorForTest * denominatorForTest)
                .isNegative());
        assertTrue(new RationalNumber(
                numeratorForTest * numeratorForTest,
                -1).isNegative());
    }

    @Test
    public void test_equals() {
        // 自身比较返回 true
        assertTrue(rationalNumber.equals(rationalNumber));

        // 与 null 比较返回 false
        assertFalse(rationalNumber.equals(null));

        // 不是类 Operand 及其子类就返回 false
        assertFalse(rationalNumber.equals(BigInteger.ONE));


        var rationalNumberOther = rationalNumber.clone();
        // 符合相等意义返回 true
        assertTrue(rationalNumber.equals(rationalNumberOther));

        var illegalStubObj = new Operand() {
            @Override
            public Class<? extends Operation> getBindingOperation() {
                return null;
            }

            @Override
            public boolean isZero() {
                return false;
            }
        };
        // 与非法对象比较返回 false
        assertFalse(rationalNumber.equals(illegalStubObj));
    }

    @Test
    public void test_toString() {
        assertEquals(rationalNumber.getNumerator().toString()
                        + "/" + rationalNumber.getDenominator().toString(),
                rationalNumber.toString());
    }


    @Test
    public void test_isProperFraction() throws CalculatorException {
        assertTrue(new RationalNumber(2, 8).isProperFraction());
        assertFalse(new RationalNumber(16, 8).isProperFraction());
    }

    @Test
    public void test_toDouble() throws CalculatorException {
        /**
         * 一般来说，浮点数是不能直接比较大小的，但不知为何，此处可以进行符合直觉的比较。
         * 个人猜测，这可能是将浮点数智能转化为字符串类型来比较的
         */
        assertEquals(0.25,
                new RationalNumber(1, 4).toDouble());
        assertEquals(0.5,
                new RationalNumber(8, 16).toDouble());

        assertNotEquals(0.25001,
                new RationalNumber(1, 4).toDouble());
        assertNotEquals(0.25001,
                new RationalNumber(1, 4).toDouble());
        assertNotEquals(0.33333,
                new RationalNumber(1, 3).toDouble());

        assertEquals(4,
                new RationalNumber(16, 4).toDouble());

        assertNotEquals(3.33333,
                new RationalNumber(10, 3).toDouble());
    }

    @Test
    public void test_reduceFraction() throws CalculatorException {
        // 测试分子
        assertEquals(new Figure(-10), new RationalNumber(100, -10).reduceFraction().getNumerator());
        // 测试分母
        assertEquals(new Figure(10), new RationalNumber(10, -100).reduceFraction().getDenominator());
    }

    @Test
    public void test_getNumerator() throws CalculatorException {
        assertEquals(new Figure(3),
                new RationalNumber(21, 28).getNumerator());
    }

    @Test
    public void test_getDenominator() throws CalculatorException {
        assertEquals(new Figure(4),
                new RationalNumber(21, 28).getDenominator());
    }
}
