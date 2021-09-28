package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * @since 2021-9-27
 */
public class ConflictException extends CalculatorException {
    public ConflictException() {
        super();
    }

    public ConflictException(String msg) {
        super(msg);
    }

    public ConflictException(String msg, Object obj) {
        super(msg, obj);
    }

    public static CalculatorException getInstance()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return ConflictException.getInstance("异常：引发冲突");
    }

    public static CalculatorException getInstance(String msg)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return CalculatorException.getInstance(ConflictException.class, msg);
    }
}