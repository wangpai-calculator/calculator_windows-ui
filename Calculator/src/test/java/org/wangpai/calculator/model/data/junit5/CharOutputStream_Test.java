package org.wangpai.calculator.model.data.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.model.data.CharOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@Deprecated
class CharOutputStream_Test {
    private String str = "123456789";
    private CharOutputStream outputStream = new CharOutputStream();

    /**
     * 这个方法同时测试了被测试类的如下方法：
     * init、hasNext、next、peek、rollback、clear
     */
    @Test
    void associationTest() {
        outputStream.init(str);
        assertTrue(outputStream.hasNext());
        assertEquals('1' , outputStream.next());
        assertTrue(outputStream.hasNext());

        assertEquals('2' , outputStream.peek());
        assertEquals('2' , outputStream.next());

        outputStream.rollback();
        assertEquals('2' , outputStream.next());

        outputStream.rollback('a');
        assertEquals('a' , outputStream.next());

        outputStream.clear();
        assertFalse(outputStream.hasNext());
        outputStream.init("China");
        assertTrue(outputStream.hasNext());
        assertEquals('C' , outputStream.next());
    }

}