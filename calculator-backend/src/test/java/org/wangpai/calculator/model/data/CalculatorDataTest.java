package org.wangpai.calculator.model.data;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.UndefinedException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorDataTest {
    @Test
    public void toString_test() throws UndefinedException {
        var str = "123456";
        var calData = new CalculatorData();
        var outputStream = new SymbolOutputStream().init(str);
        while (outputStream.hasNext()) {
            calData.pushSymbol(outputStream.next());
        }
        assertEquals(str, calData.toString());
    }
}