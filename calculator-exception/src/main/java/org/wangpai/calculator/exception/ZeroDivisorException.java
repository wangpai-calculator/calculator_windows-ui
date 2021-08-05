package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * @since 2021-7-9
 */
public class ZeroDivisorException extends CalculatorException {
    public ZeroDivisorException() {
        super();
    }

    public ZeroDivisorException(String msg) {
        super(msg);
    }

    public ZeroDivisorException(String msg, Object obj) {
        super(msg, obj);
    }

    public static CalculatorException getInstance()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return ZeroDivisorException.getInstance("异常：发生了 0 除");
    }

    public static CalculatorException getInstance(String msg)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return CalculatorException.getInstance(ZeroDivisorException.class, msg);
    }
}
