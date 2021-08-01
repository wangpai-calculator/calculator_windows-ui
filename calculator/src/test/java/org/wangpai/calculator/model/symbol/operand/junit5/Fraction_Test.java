package org.wangpai.calculator.model.symbol.operand.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.model.symbol.operand.Fraction;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operation.FractionOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


class Fraction_Test {
    private final int numForTest = 1;

    @Deprecated
    @Test
    void getEnumIdentifier() {
        assertEquals("FRACTION",
                new Fraction().getEnumIdentifier());
    }

    @Test
    void getBindingOperation() {
        assertEquals(FractionOperation.class,
                new Fraction().getBindingOperation());
    }

    @Test
    void getFraction() {
        assertEquals(BigInteger.valueOf(numForTest),
                new Fraction(numForTest).getFraction());
    }

    @Test
    void valueOf() {
        assertEquals(new Fraction(numForTest),
                Fraction.valueOf(numForTest));
    }

    @Test
    void setFraction() {
        // 测试 public Fraction setFraction(BigInteger fraction)
        assertEquals(new Fraction(numForTest),
                new Fraction().setFraction(BigInteger.valueOf(numForTest)));

        //  测试 public Fraction setFraction(long num)
        assertEquals(new Fraction(numForTest),
                new Fraction().setFraction(numForTest));
    }

    @Test
    void clone_test() throws CloneNotSupportedException {
        Fraction fraction = new Fraction(numForTest);

        assertEquals(fraction, fraction.clone());
    }

    @Test
    void toString_test() {
        assertEquals(new Fraction(numForTest).getFraction().toString(),
                new Fraction(numForTest).toString());
    }

    @Test
    void equals_test() throws CloneNotSupportedException {
        var fraction = new Fraction(numForTest);

        // 自身比较返回 true
        assertTrue(fraction.equals(fraction));

        // 与 null 比较返回 false
        assertFalse(fraction.equals(null));

        // 不是类 Operand 及其子类就返回 false
        assertFalse(fraction.equals(BigInteger.ONE));


        var fractionOther = fraction.clone();
        // 符合相等意义返回 true
        assertTrue(fraction.equals(fractionOther));

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
        assertFalse(fraction.equals(illegalStubObj));
    }

    @Test
    void isZero() {
        assertTrue(new Fraction(0).isZero());
        assertFalse(new Fraction(1).isZero());
    }

    @Test
    void isNegative() {
        assertTrue(new Fraction(-1).isNegative());
        assertFalse(new Fraction(0).isNegative());
        assertFalse(new Fraction(1).isNegative());
    }
}