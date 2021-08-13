package org.wangpai.calculator.model.symbol.operand.junit5;

import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.symbol.operand.Figure;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;
import org.wangpai.calculator.model.symbol.operation.FigureOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @since 2021-7-22
 */
public class Figure_Test {
    private final int numForTest = 1;

    /**
     * @since 2021-8-3
     */
    @Test
    public void test_Figure_Operand() {
        assertDoesNotThrow(() -> new Figure((Operand) new Figure(numForTest)));

        assertThrows(UndefinedException.class,
                () -> new Figure((Operand) new RationalNumber(1, 1)));
    }

    @Test
    public void test_getBindingOperation() {
        assertEquals(FigureOperation.class,
                new Figure().getBindingOperation());
    }

    @Test
    public void test_getFraction() {
        assertEquals(BigInteger.valueOf(numForTest),
                new Figure(numForTest).getInteger());
    }

    @Test
    public void test_valueOf() {
        assertEquals(new Figure(numForTest),
                Figure.valueOf(numForTest));
    }

    @Test
    public void test_setFigure() {
        // 测试 public Fraction setFraction(BigInteger fraction)
        assertEquals(new Figure(numForTest),
                new Figure().setInteger(BigInteger.valueOf(numForTest)));

        //  测试 public Fraction setFraction(long num)
        assertEquals(new Figure(numForTest),
                new Figure().setInteger(numForTest));
    }

    @Test
    public void test_clone() throws CloneNotSupportedException {
        var figure = new Figure(numForTest);
        var cloned = figure.clone();

        assertEquals(figure, cloned); // 意义上相等
        assertFalse(cloned == figure); // 物理上不是同一个
    }

    @Test
    public void test_toString() {
        assertEquals(new Figure(numForTest).getInteger().toString(),
                new Figure(numForTest).toString());
    }

    @Test
    public void test_equals() throws CloneNotSupportedException {
        var figure = new Figure(numForTest);

        // 自身比较返回 true
        assertTrue(figure.equals(figure));

        // 与 null 比较返回 false
        assertFalse(figure.equals(null));

        // 不是类 Operand 及其子类就返回 false
        assertFalse(figure.equals(BigInteger.ONE));

        var figureOther = figure.clone();
        // 符合相等意义返回 true
        assertTrue(figure.equals(figureOther));

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
        assertFalse(figure.equals(illegalStubObj));
    }

    @Test
    public void test_isZero() {
        assertTrue(new Figure(0).isZero());
        assertFalse(new Figure(1).isZero());
    }

    @Test
    public void test_isNegative() {
        assertTrue(new Figure(-1).isNegative());
        assertFalse(new Figure(0).isNegative());
        assertFalse(new Figure(1).isNegative());
    }
}
