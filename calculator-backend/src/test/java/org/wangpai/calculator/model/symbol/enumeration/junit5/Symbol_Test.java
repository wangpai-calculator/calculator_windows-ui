package org.wangpai.calculator.model.symbol.enumeration.junit5;

import org.wangpai.calculator.model.symbol.enumeration.Symbol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @since 2021-7-30
 */
public class Symbol_Test {
    Symbol symbol = Symbol.ADD;


    @Test
    public void test_getEnum() {
        assertEquals(Symbol.ADD, Symbol.getEnum("+"));

        assertEquals(Symbol.MULTIPLY, Symbol.getEnum("*"));
        assertEquals(null, Symbol.getEnum("abc"));
    }

    @Test
    public void test_isDigit() {
        assertTrue(Symbol.ZERO.isDigit());
        assertFalse(Symbol.ADD.isDigit());
    }

    @Test
    public void test_isBracket() {
        assertTrue(Symbol.LEFT_BRACKET.isBracket());
        assertTrue(Symbol.RIGHT_BRACKET.isBracket());
        assertFalse(Symbol.ADD.isBracket());
    }

    @Test
    public void test_getUnderlyingSymbol() {
        assertEquals("+", symbol.toString());
    }

    @Test
    public void test_getOrder() {
        // lombok 的 get 方法无需测试
    }
}
