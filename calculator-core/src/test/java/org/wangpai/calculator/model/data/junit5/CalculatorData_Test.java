package org.wangpai.calculator.model.data.junit5;

import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.data.CalculatorData;
import org.wangpai.calculator.model.data.SymbolOutputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorData_Test {
    @Test
    public void test_toString() throws UndefinedException {
        var str = "123456";
        var calData = new CalculatorData();
        var outputStream = new SymbolOutputStream().init(str);
        while (outputStream.hasNext()) {
            calData.pushSymbol(outputStream.next());
        }
        assertEquals(str, calData.toString());
    }
}