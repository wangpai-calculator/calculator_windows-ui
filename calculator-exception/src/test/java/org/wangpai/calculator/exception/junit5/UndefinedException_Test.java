package org.wangpai.calculator.exception.junit5;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.UndefinedException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @since 2021-7-19
 */
public class UndefinedException_Test {
    private String msg = "错误：发生了未定义异常";

    @Test
    public void test_getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var undefinedInputException = UndefinedException.getInstance();

        assertEquals(UndefinedException.class, undefinedInputException.getClass());
        assertEquals(this.msg, undefinedInputException.exceptionCause());

    }

    @Test
    public void test_getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var undefinedInputException = UndefinedException.getInstance(this.msg);

        assertEquals(UndefinedException.class, undefinedInputException.getClass());
        assertEquals(this.msg, undefinedInputException.exceptionCause());
    }
}
