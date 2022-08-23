package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 2021-7-19
 */
public class UndefinedExceptionTest {
    private String msg = "错误：发生了未定义异常";

    @Test
    public void getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var undefinedInputException = UndefinedException.getInstance();

        assertEquals(UndefinedException.class, undefinedInputException.getClass());
        assertEquals(this.msg, undefinedInputException.exceptionCause());

    }

    @Test
    public void getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var undefinedInputException = UndefinedException.getInstance(this.msg);

        assertEquals(UndefinedException.class, undefinedInputException.getClass());
        assertEquals(this.msg, undefinedInputException.exceptionCause());
    }
}
