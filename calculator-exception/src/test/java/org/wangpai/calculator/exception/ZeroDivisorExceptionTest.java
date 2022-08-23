package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 2021-7-19
 */
public class ZeroDivisorExceptionTest {
    private String msg = "异常：发生了 0 除";

    @Test
    public void getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var zeroDivisorException = ZeroDivisorException.getInstance();

        assertEquals(ZeroDivisorException.class, zeroDivisorException.getClass());
        assertEquals(this.msg, zeroDivisorException.exceptionCause());
    }

    @Test
    public void getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var zeroDivisorException = ZeroDivisorException.getInstance(this.msg);

        assertEquals(ZeroDivisorException.class, zeroDivisorException.getClass());
        assertEquals(this.msg, zeroDivisorException.exceptionCause());
    }
}
