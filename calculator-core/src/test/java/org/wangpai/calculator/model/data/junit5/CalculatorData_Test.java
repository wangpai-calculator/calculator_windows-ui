package org.wangpai.calculator.model.data.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.data.CalculatorData;
import org.wangpai.calculator.model.data.SymbolOutputStream;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorData_Test {
    @Test
    void toString_test() throws UndefinedException {
        var str = "123456";
        var calData = new CalculatorData();
        var outputStream = new SymbolOutputStream().init(str);
        while (outputStream.hasNext()) {
            calData.pushSymbol(outputStream.next());
        }
        assertEquals(str, calData.toString());
    }
}