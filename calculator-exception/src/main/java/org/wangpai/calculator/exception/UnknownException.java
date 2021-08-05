package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * @since 2021-7-29
 */
public class UnknownException extends CalculatorException {
    public UnknownException() {
        super();
    }

    public UnknownException(String msg) {
        super(msg);
    }

    public UnknownException(String msg, Object obj) {
        super(msg, obj);
    }

    public static CalculatorException getInstance()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return UnknownException.getInstance("错误：发生了未知异常");
    }

    public static CalculatorException getInstance(String msg)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return CalculatorException.getInstance(UnknownException.class, msg);
    }
}
