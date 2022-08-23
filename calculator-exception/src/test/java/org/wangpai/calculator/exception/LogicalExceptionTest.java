package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 2021-9-27
 */
public class LogicalExceptionTest {
    private String msg = "异常：不符逻辑";

    @Test
    public void getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var logicalException = LogicalException.getInstance();

        assertEquals(LogicalException.class, logicalException.getClass());
        assertEquals(this.msg, logicalException.exceptionCause());
    }

    @Test
    public void getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var logicalException = LogicalException.getInstance(this.msg);

        assertEquals(LogicalException.class, logicalException.getClass());
        assertEquals(this.msg, logicalException.exceptionCause());
    }
}