package org.wangpai.calculator.model.symbol.enumeration.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @since 2021-7-30
 */
class Symbol_Test {
    Symbol symbol = Symbol.ADD;


    @Test
    void getEnum() {
        assertEquals(Symbol.ADD, Symbol.getEnum("+"));

        assertEquals(Symbol.MULTIPLY, Symbol.getEnum("*"));
        assertEquals(null, Symbol.getEnum("abc"));
    }

    @Test
    void isDigit() {
        assertTrue(Symbol.ZERO.isDigit());
        assertFalse(Symbol.ADD.isDigit());
    }

    @Test
    void isBracket() {
        assertTrue(Symbol.LEFT_BRACKET.isBracket());
        assertTrue(Symbol.RIGHT_BRACKET.isBracket());
        assertFalse(Symbol.ADD.isBracket());
    }

    @Test
    void getUnderlyingSymbol() {
        assertEquals("+", symbol.toString());
    }

    @Test
    void getOrder() {
        assertEquals(symbol.ordinal(), symbol.getOrder());
    }

}