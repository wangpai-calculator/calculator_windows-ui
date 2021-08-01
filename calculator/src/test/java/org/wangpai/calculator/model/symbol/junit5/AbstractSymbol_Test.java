package org.wangpai.calculator.model.symbol.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.model.symbol.AbstractSymbol;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractSymbol_Test {
    /**
     * 此方法必须加后缀 _test，因为类 Object 也有一个方法 equals
     */
    @Test
    void equals_test() {
        var abstractSymbol = new AbstractSymbol() {
            // 原抽象类 AbstractSymbol 自己已经实现了所有的方法，所以这里不需要提供任何方法实现
        };

        var abstractSymbolOther = new AbstractSymbol() {
            // 原抽象类 AbstractSymbol 自己已经实现了所有的方法，所以这里不需要提供任何方法实现
        };

        // 自身比较返回 true
        assertTrue(abstractSymbol.equals(abstractSymbol));

        // 与 null 比较返回 false
        assertFalse(abstractSymbol.equals(null));

        // 只要类不同就返回 false
        assertFalse(abstractSymbol.equals(abstractSymbolOther));
    }
}