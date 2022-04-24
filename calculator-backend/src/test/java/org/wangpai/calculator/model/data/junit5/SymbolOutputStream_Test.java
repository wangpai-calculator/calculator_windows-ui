package org.wangpai.calculator.model.data.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.data.SymbolOutputStream;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.ONE;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.THREE;
import static org.wangpai.calculator.model.symbol.enumeration.Symbol.TWO;

/**
 * @since 2021-7-29
 */
public class SymbolOutputStream_Test {
    private String str = "123456789";
    private SymbolOutputStream outputStream = new SymbolOutputStream();

    /**
     * 联合测试
     * <p>
     * 这个方法同时测试了被测试类的如下方法：
     * init、hasNext、next、peek、rollback、clear
     */
    @Test
    public void test_associationTest() throws UndefinedException {
        outputStream.init(str);
        assertTrue(outputStream.hasNext());
        assertEquals(Symbol.getEnum("1"), outputStream.next());
        assertTrue(outputStream.hasNext());

        assertEquals(Symbol.getEnum("2"), outputStream.peek());
        assertEquals(Symbol.getEnum("2"), outputStream.next());

        outputStream.rollback();
        assertEquals(Symbol.getEnum("2"), outputStream.next());

        outputStream.rollback(Symbol.getEnum("-"));
        assertEquals(Symbol.getEnum("-"), outputStream.next());

        outputStream.clear();
        assertFalse(outputStream.hasNext());
        outputStream.init("+-X/");
        assertTrue(outputStream.hasNext());
        assertEquals(Symbol.getEnum("+"), outputStream.next());


        Throwable throwable = assertThrows(UndefinedException.class,
                () -> outputStream.init("abcdef"));
        assertEquals("异常：输入了未定义符号", throwable.getMessage());
    }

    @Test
    public void test_getRead() throws UndefinedException {
        outputStream.init(str);

        // 测试初始情况
        assertEquals("", outputStream.getRead().toString());

        // 测试中间状态下的情况
        int readSymbolNum = str.length() / 2;
        for (int times = 1; times <= readSymbolNum; ++times) {
            outputStream.next();
        }
        assertEquals(str.substring(0, readSymbolNum), outputStream.getRead().toString());

        // 测试流已读完下的情况
        outputStream.resetIndex();
        while (outputStream.hasNext()) {
            outputStream.next();
        }
        assertEquals(str, outputStream.getRead().toString());
    }

    @Test
    public void test_getRest() throws UndefinedException {
        outputStream.init(str);

        // 测试初始情况
        assertEquals(str, outputStream.getRest().toString());

        // 测试中间状态下的情况
        int readSymbolNum = str.length() / 2;
        for (int times = 1; times <= readSymbolNum; ++times) {
            outputStream.next();
        }
        assertEquals(str.substring(readSymbolNum), outputStream.getRest().toString());

        // 测试流已读完下的情况
        outputStream.resetIndex();
        while (outputStream.hasNext()) {
            outputStream.next();
        }
        assertEquals("", outputStream.getRest().toString());
    }

    @Test
    public void test_toArray() throws UndefinedException {
        SymbolOutputStream symbolOutputStream = new SymbolOutputStream();
        symbolOutputStream.init("123");

        assertArrayEquals(new Symbol[]{ONE, TWO, THREE},
                symbolOutputStream.toArray());
    }

    @Test
    public void test_toString() throws UndefinedException {
        outputStream.init(str);

        assertEquals(str, outputStream.toString());
    }

    @Test
    public void test_preInitCheck_String() {
        assertNull(SymbolOutputStream.preInitCheck("123456"));

        assertEquals("123",
                SymbolOutputStream.preInitCheck("123abc456"));

        assertEquals("",
                SymbolOutputStream.preInitCheck("abc456"));
        // 如果输入给方法 preInitCheck 的字符串在一开始就有错误，则应该返回空串，而不是 null
        assertNotNull(SymbolOutputStream.preInitCheck("abc456"));
    }
}
