package org.wangpai.calculator.model.symbol.operand.junit5;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operand.Decimal;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;

import org.junit.jupiter.api.Test;

import static org.wangpai.calculator.model.symbol.enumeration.Symbol.ADD;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.DOT;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.FIVE;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.FOUR;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.ONE;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.SUBTRACT;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.THREE;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.TWO;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.ZERO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Decimal_Test {
    Symbol[] zero = {ZERO}; // 0
    Symbol[] positive = {ONE, TWO, THREE}; // 正数
    Symbol[] negative = {SUBTRACT, ONE, TWO, THREE}; // 负数
    Symbol[] properFraction = {ZERO, DOT, ONE, TWO, THREE}; // 真分数
    Symbol[] decimal = {SUBTRACT, ONE, DOT, TWO, THREE}; // 小数

    Symbol[] wrongFirst = {ADD, ONE, TWO, THREE}; // 第一个为加号，错误
    Symbol[] doubleSubtract = {SUBTRACT, ONE, SUBTRACT, TWO, THREE}; // 有两个减号，错误
    Symbol[] innerSubtract = {ONE, SUBTRACT, TWO, THREE}; // 中间有减号，错误
    Symbol[] doublePoint = {DOT, ONE, DOT, TWO, THREE}; // 有两个减号，错误
    Symbol[] beginDot = {DOT, ONE, TWO, DOT, THREE}; // 小数点位置错误

    /**
     * @since 2021-8-2
     */
    @Test
    public void test_arrayIsDecimal() {
        assertTrue(Decimal.arrayIsDecimal(zero));
        assertTrue(Decimal.arrayIsDecimal(positive));
        assertTrue(Decimal.arrayIsDecimal(negative));
        assertTrue(Decimal.arrayIsDecimal(properFraction));
        assertTrue(Decimal.arrayIsDecimal(decimal));

        assertFalse(Decimal.arrayIsDecimal(wrongFirst));
        assertFalse(Decimal.arrayIsDecimal(doubleSubtract));
        assertFalse(Decimal.arrayIsDecimal(innerSubtract));
        assertFalse(Decimal.arrayIsDecimal(doublePoint));
        assertFalse(Decimal.arrayIsDecimal(beginDot));
        assertFalse(Decimal.arrayIsDecimal(null));
    }

    @Test
    public void test_toRationalNumber() throws CalculatorException {
        Symbol[] lessThanZero = {ZERO, DOT, FIVE}; // 0.5
        Symbol[] largerThanZero = {ONE, DOT, FIVE}; // 1.5
        Symbol[] lessThanZeroNegative = {SUBTRACT, ZERO, DOT, FIVE}; // -0.5
        Symbol[] largerThanZeroNegative = {SUBTRACT, ONE, DOT, FIVE}; // -1.5

        assertEquals(new RationalNumber(0),
                new Decimal(zero).toRationalNumber());
        assertEquals(new RationalNumber(1, 2),
                new Decimal(lessThanZero).toRationalNumber());
        assertEquals(new RationalNumber(3, 2),
                new Decimal(largerThanZero).toRationalNumber());
        assertEquals(new RationalNumber(-1, 2),
                new Decimal(lessThanZeroNegative).toRationalNumber());
        assertEquals(new RationalNumber(-3, 2),
                new Decimal(largerThanZeroNegative).toRationalNumber());
    }

    public static void main(String[] args) {
        Symbol[] symbols = {ONE, DOT, FOUR}; // 小数

        Decimal decimal = new Decimal(symbols);

        var rn = decimal.toRationalNumber();

        System.out.println(rn);
    }
}
