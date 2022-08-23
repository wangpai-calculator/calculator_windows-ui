package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 2021-9-27
 */
public class ConflictExceptionTest {
    private String msg = "异常：引发冲突";

    @Test
    public void getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var conflictException = ConflictException.getInstance();

        assertEquals(ConflictException.class, conflictException.getClass());
        assertEquals(this.msg, conflictException.exceptionCause());
    }

    @Test
    public void getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var conflictException = ConflictException.getInstance(this.msg);

        assertEquals(ConflictException.class, conflictException.getClass());
        assertEquals(this.msg, conflictException.exceptionCause());
    }
}
