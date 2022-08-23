package org.wangpai.calculator.model.symbol.enumeration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @since 2021-7-30
 */
public class SymbolTest {
    Symbol symbol = Symbol.ADD;


    @Test
    public void getEnum() {
        assertEquals(Symbol.ADD, Symbol.getEnum("+"));

        assertEquals(Symbol.MULTIPLY, Symbol.getEnum("*"));
        assertEquals(null, Symbol.getEnum("abc"));
    }

    @Test
    public void isDigit() {
        assertTrue(Symbol.ZERO.isDigit());
        assertFalse(Symbol.ADD.isDigit());
    }

    @Test
    public void isBracket() {
        assertTrue(Symbol.LEFT_BRACKET.isBracket());
        assertTrue(Symbol.RIGHT_BRACKET.isBracket());
        assertFalse(Symbol.ADD.isBracket());
    }

    @Test
    public void getUnderlyingSymbol() {
        assertEquals("+", this.symbol.toString());
    }

    @Test
    public void getOrder() {
        // lombok 的 get 方法无需测试
    }
}
