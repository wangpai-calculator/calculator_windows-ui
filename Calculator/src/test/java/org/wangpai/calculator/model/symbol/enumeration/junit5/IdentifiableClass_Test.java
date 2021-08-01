package org.wangpai.calculator.model.symbol.enumeration.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.model.symbol.enumeration.IdentifiableClass;
import org.wangpai.calculator.model.symbol.operand.Fraction;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Deprecated
class IdentifiableClass_Test {
    @Test
    void getInstance() {
        assertEquals(IdentifiableClass.FRACTION,
                IdentifiableClass.getInstance(new Fraction().getEnumIdentifier()));
    }

    @Test
    void getIdentifiableClass() {
        assertEquals(Fraction.class,
                IdentifiableClass.FRACTION.getIdentifiableClass());
    }
}