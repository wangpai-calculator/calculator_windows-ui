package org.wangpai.calculator.model.data;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.UndefinedException;
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
public class SymbolOutputStreamTest {
    private String str = "123456789";
    private SymbolOutputStream outputStream = new SymbolOutputStream();

    /**
     * 联合测试
     * <p>
     * 这个方法同时测试了被测试类的如下方法：
     * init、hasNext、next、peek、rollback、clear
     */
    @Test
    public void associationTest() throws UndefinedException {
        this.outputStream.init(this.str);
        assertTrue(this.outputStream.hasNext());
        assertEquals(Symbol.getEnum("1"), this.outputStream.next());
        assertTrue(this.outputStream.hasNext());

        assertEquals(Symbol.getEnum("2"), this.outputStream.peek());
        assertEquals(Symbol.getEnum("2"), this.outputStream.next());

        this.outputStream.rollback();
        assertEquals(Symbol.getEnum("2"), this.outputStream.next());

        this.outputStream.rollback(Symbol.getEnum("-"));
        assertEquals(Symbol.getEnum("-"), this.outputStream.next());

        this.outputStream.clear();
        assertFalse(this.outputStream.hasNext());
        this.outputStream.init("+-X/");
        assertTrue(this.outputStream.hasNext());
        assertEquals(Symbol.getEnum("+"), this.outputStream.next());


        Throwable throwable = assertThrows(UndefinedException.class,
                () -> this.outputStream.init("abcdef"));
        assertEquals("异常：输入了未定义符号", throwable.getMessage());
    }

    @Test
    public void getRead() throws UndefinedException {
        this.outputStream.init(this.str);

        // 测试初始情况
        assertEquals("", this.outputStream.getRead().toString());

        // 测试中间状态下的情况
        int readSymbolNum = this.str.length() / 2;
        for (int times = 1; times <= readSymbolNum; ++times) {
            this.outputStream.next();
        }
        assertEquals(this.str.substring(0, readSymbolNum), this.outputStream.getRead().toString());

        // 测试流已读完下的情况
        this.outputStream.resetIndex();
        while (this.outputStream.hasNext()) {
            this.outputStream.next();
        }
        assertEquals(this.str, this.outputStream.getRead().toString());
    }

    @Test
    public void getRest() throws UndefinedException {
        this.outputStream.init(this.str);

        // 测试初始情况
        assertEquals(this.str, this.outputStream.getRest().toString());

        // 测试中间状态下的情况
        int readSymbolNum = this.str.length() / 2;
        for (int times = 1; times <= readSymbolNum; ++times) {
            this.outputStream.next();
        }
        assertEquals(this.str.substring(readSymbolNum), this.outputStream.getRest().toString());

        // 测试流已读完下的情况
        this.outputStream.resetIndex();
        while (this.outputStream.hasNext()) {
            this.outputStream.next();
        }
        assertEquals("", this.outputStream.getRest().toString());
    }

    @Test
    public void toArray() throws UndefinedException {
        SymbolOutputStream symbolOutputStream = new SymbolOutputStream();
        symbolOutputStream.init("123");

        assertArrayEquals(new Symbol[]{ONE, TWO, THREE},
                symbolOutputStream.toArray());
    }

    @Test
    public void toString_test() throws UndefinedException {
        this.outputStream.init(this.str);

        assertEquals(this.str, this.outputStream.toString());
    }

    @Test
    public void preInitCheck_String() {
        assertNull(SymbolOutputStream.preInitCheck("123456"));

        assertEquals("123",
                SymbolOutputStream.preInitCheck("123abc456"));

        assertEquals("",
                SymbolOutputStream.preInitCheck("abc456"));
        // 如果输入给方法 preInitCheck 的字符串在一开始就有错误，则应该返回空串，而不是 null
        assertNotNull(SymbolOutputStream.preInitCheck("abc456"));
    }
}
