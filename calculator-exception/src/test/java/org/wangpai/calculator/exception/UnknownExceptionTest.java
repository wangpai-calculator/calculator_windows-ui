package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 2021-7-19
 */
public class UnknownExceptionTest {
    private String msg = "错误：发生了未知异常";

    @Test
    public void getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var unknownException = UnknownException.getInstance();

        assertEquals(UnknownException.class, unknownException.getClass());
        assertEquals(this.msg, unknownException.exceptionCause());
    }

    @Test
    public void getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var unknownException = UnknownException.getInstance(this.msg);

        assertEquals(UnknownException.class, unknownException.getClass());
        assertEquals(this.msg, unknownException.exceptionCause());
    }
}
