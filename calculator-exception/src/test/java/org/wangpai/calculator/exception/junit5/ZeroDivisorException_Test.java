package org.wangpai.calculator.exception.junit5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.wangpai.calculator.exception.ZeroDivisorException;

/**
 * @since 2021-7-19
 */
public class ZeroDivisorException_Test {
    private String msg = "异常：发生了 0 除";

    ZeroDivisorException_Test() {
        super();
    }

    @Test
    void getInstance_void()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var zeroDivisorException = ZeroDivisorException.getInstance();

        assertEquals(ZeroDivisorException.class, zeroDivisorException.getClass());
        assertEquals(this.msg, zeroDivisorException.exceptionCause());

    }

    @Test
    void getInstance_String()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var zeroDivisorException = ZeroDivisorException.getInstance(this.msg);

        assertEquals(ZeroDivisorException.class, zeroDivisorException.getClass());
        assertEquals(this.msg, zeroDivisorException.exceptionCause());
    }
}
