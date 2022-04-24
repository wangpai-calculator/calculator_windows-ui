package org.wangpai.calculator.exception.junit5;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.SyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 2021-7-23
 */
public class SyntaxException_Test {
    private String msg = "异常：不符语法";

    @Test
    public void test_getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var syntaxException = SyntaxException.getInstance();

        assertEquals(SyntaxException.class, syntaxException.getClass());
        assertEquals(this.msg, syntaxException.exceptionCause());
    }

    @Test
    public void test_getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var syntaxException = SyntaxException.getInstance(this.msg);

        assertEquals(SyntaxException.class, syntaxException.getClass());
        assertEquals(this.msg, syntaxException.exceptionCause());
    }
}
