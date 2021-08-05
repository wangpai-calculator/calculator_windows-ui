package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * 当进行的操作、使用的符号未定义或不支持时，使用该异常
 *
 * @since 2021-7-29
 */
public class UndefinedException extends CalculatorException {
    public UndefinedException() {
        super();
    }

    public UndefinedException(String msg) {
        super(msg);
    }

    public UndefinedException(String msg, Object obj) {
        super(msg, obj);
    }

    public static CalculatorException getInstance()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return UndefinedException.getInstance("错误：发生了未定义异常");
    }

    public static CalculatorException getInstance(String msg)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return CalculatorException.getInstance(UndefinedException.class, msg);
    }
}
