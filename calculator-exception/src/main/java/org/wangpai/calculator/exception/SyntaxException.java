package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * @since 2021-7-9
 */
public class SyntaxException extends CalculatorException {
    public SyntaxException(){
        super();
    }

    public SyntaxException(String msg) {
        super(msg);
    }

    public SyntaxException(String msg, Object obj) {
        super(msg, obj);
    }

    public static CalculatorException getInstance()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return SyntaxException.getInstance("异常：不符语法");
    }

    public static CalculatorException getInstance(String msg)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return CalculatorException.getInstance(SyntaxException.class, msg);
    }
}
