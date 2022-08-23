package org.wangpai.calculator.model.symbol.operand;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.UndefinedException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @since 2021-7-22
 */
public class FigureTest {
    private final int numForTest = 1;

    /**
     * @since 2021-8-3
     */
    @Test
    public void Figure_Operand() {
        assertDoesNotThrow(() -> new Figure((Operand) new Figure(this.numForTest)));

        assertThrows(UndefinedException.class,
                () -> new Figure((Operand) new RationalNumber(1, 1)));
    }

    @Test
    public void getFraction() {
        assertEquals(BigInteger.valueOf(this.numForTest),
                new Figure(this.numForTest).getInteger());
    }

    @Test
    public void valueOf() {
        assertEquals(new Figure(this.numForTest),
                Figure.valueOf(this.numForTest));
    }

    @Test
    public void setFigure() {
        // 测试 public Fraction setFraction(BigInteger fraction)
        assertEquals(new Figure(this.numForTest),
                new Figure().setInteger(BigInteger.valueOf(this.numForTest)));

        //  测试 public Fraction setFraction(long num)
        assertEquals(new Figure(this.numForTest),
                new Figure().setInteger(this.numForTest));
    }

    @Test
    public void clone_test() throws CloneNotSupportedException {
        var figure = new Figure(this.numForTest);
        var cloned = figure.clone();

        assertEquals(figure, cloned); // 意义上相等
        assertFalse(cloned == figure); // 物理上不是同一个
    }

    @Test
    public void toString_test() {
        assertEquals(new Figure(this.numForTest).getInteger().toString(),
                new Figure(this.numForTest).toString());
    }

    @Test
    public void equals() throws CloneNotSupportedException {
        var figure = new Figure(this.numForTest);

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
            public boolean isZero() {
                return false;
            }
        };
        // 与非法对象比较返回 false
        assertFalse(figure.equals(illegalStubObj));
    }

    @Test
    public void isZero() {
        assertTrue(new Figure(0).isZero());
        assertFalse(new Figure(1).isZero());
    }

    @Test
    public void isNegative() {
        assertTrue(new Figure(-1).isNegative());
        assertFalse(new Figure(0).isNegative());
        assertFalse(new Figure(1).isNegative());
    }
}
