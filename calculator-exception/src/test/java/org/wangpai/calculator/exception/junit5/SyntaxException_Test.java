package org.wangpai.calculator.exception.junit5;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.InvocationTargetException;

import org.wangpai.calculator.exception.SyntaxException;

/**
 * @since 2021-7-23
 */
public class SyntaxException_Test {
    private String msg = "异常：不符语法";

     SyntaxException_Test() {
        super();
    }

    @Test
    void getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var syntaxException = SyntaxException.getInstance();

        assertEquals(SyntaxException.class, syntaxException.getClass());
        assertEquals(this.msg, syntaxException.exceptionCause());
    }

    @Test
    void getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var syntaxException = SyntaxException.getInstance(this.msg);

        assertEquals(SyntaxException.class, syntaxException.getClass());
        assertEquals(this.msg, syntaxException.exceptionCause());
    }
}