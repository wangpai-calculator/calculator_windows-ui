package org.wangpai.calculator.model.symbol.operand.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.model.symbol.operand.Operand;
import org.wangpai.calculator.model.symbol.operation.Operation;


import static org.junit.jupiter.api.Assertions.assertEquals;

class Operand_Test {
    @Test
    void getEnumIdentifier() {
        var operand =new Operand(){
            @Override
            public String getEnumIdentifier() {
                return this.getEnumIdentifier();
            }

            @Override
            public Class<? extends Operation> getBindingOperation() {
                return null;
            }

            @Override
            public boolean isZero() {
                return false;
            }
        };

        assertEquals("OPERAND", operand.getEnumIdentifier());
    }

    @Test
    void getBindingOperation() {
        // 原类的方法为抽象方法，因此此方法为空。
    }

    @Test
    void isZero() {
        // 原类的方法为抽象方法，因此此方法为空。
    }
}