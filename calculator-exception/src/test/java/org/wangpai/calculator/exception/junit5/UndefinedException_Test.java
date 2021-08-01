package org.wangpai.calculator.exception.junit5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.wangpai.calculator.exception.UndefinedException;

public class UndefinedException_Test {
    private String msg = "错误：发生了未定义异常";

    protected UndefinedException_Test() {
        super();
    }

    @Test
    void getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var undefinedInputException = UndefinedException.getInstance();

        assertEquals(UndefinedException.class, undefinedInputException.getClass());
        assertEquals(this.msg, undefinedInputException.exceptionCause());

    }

    @Test
    void getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var undefinedInputException = UndefinedException.getInstance(this.msg);

        assertEquals(UndefinedException.class, undefinedInputException.getClass());
        assertEquals(this.msg, undefinedInputException.exceptionCause());
    }
}