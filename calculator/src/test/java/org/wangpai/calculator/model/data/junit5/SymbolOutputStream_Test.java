package org.wangpai.calculator.model.data.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.data.CharOutputStream;
import org.wangpai.calculator.model.data.SymbolOutputStream;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operand.Fraction;

import static org.junit.jupiter.api.Assertions.*;

class SymbolOutputStream_Test {
    private String str = "123456789";
    private SymbolOutputStream outputStream = new SymbolOutputStream();

    /**
     * 这个方法同时测试了被测试类的如下方法：
     * init、hasNext、next、peek、rollback、clear
     */
    @Test
    void associationTest() throws UndefinedException {
        outputStream.init(str);
        assertTrue(outputStream.hasNext());
        assertEquals(Symbol.getEnum("1") , outputStream.next());
        assertTrue(outputStream.hasNext());

        assertEquals(Symbol.getEnum("2") , outputStream.peek());
        assertEquals(Symbol.getEnum("2") , outputStream.next());

        outputStream.rollback();
        assertEquals(Symbol.getEnum("2") , outputStream.next());

        outputStream.rollback(Symbol.getEnum("-"));
        assertEquals(Symbol.getEnum("-") , outputStream.next());

        outputStream.clear();
        assertFalse(outputStream.hasNext());
        outputStream.init("+-X/");
        assertTrue(outputStream.hasNext());
        assertEquals(Symbol.getEnum("+") , outputStream.next());


        Throwable throwable = assertThrows(UndefinedException.class,
                () -> outputStream.init("abcdef"));
        assertEquals("异常：输入了未定义符号", throwable.getMessage());
    }

    @Test
    void preInitCheck_String(){
        assertNull(SymbolOutputStream.preInitCheck("123456"));

        assertEquals("123",
                SymbolOutputStream.preInitCheck("123abc456"));

        assertEquals("",
                SymbolOutputStream.preInitCheck("abc456"));
        // 如果输入给方法 preInitCheck 的字符串在一开始就有错误，则应该返回空串，而不是 null
        assertNotNull(SymbolOutputStream.preInitCheck("abc456"));
    }

}