package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * 此类与 SyntaxException 的区别在于，
 * SyntaxException 是进行不存在的操作，
 * 而 LogicalException 是进行存在但是被禁止的操作
 *
 * @since 2021-7-29
 */
public class LogicalException extends CalculatorException {
    public LogicalException() {
        super();
    }

    public LogicalException(String msg) {
        super(msg);
    }

    public LogicalException(String msg, Object obj) {
        super(msg, obj);
    }

    public static CalculatorException getInstance()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return SyntaxException.getInstance("异常：不符逻辑");
    }

    public static CalculatorException getInstance(String msg)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return CalculatorException.getInstance(LogicalException.class, msg);
    }
}