package org.wangpai.calculator.exception.junit5;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.ConflictException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 2021-9-27
 */
public class ConflictException_Test {
    private String msg = "异常：引发冲突";

    @Test
    public void test_getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var conflictException = ConflictException.getInstance();

        assertEquals(ConflictException.class, conflictException.getClass());
        assertEquals(this.msg, conflictException.exceptionCause());
    }

    @Test
    public void test_getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var conflictException = ConflictException.getInstance(this.msg);

        assertEquals(ConflictException.class, conflictException.getClass());
        assertEquals(this.msg, conflictException.exceptionCause());
    }
}
