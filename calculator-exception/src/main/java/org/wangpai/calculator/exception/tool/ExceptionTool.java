package org.wangpai.calculator.exception.tool;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.UnknownException;

/**
 * @since 2021-7-19
 */
public class ExceptionTool {
    public static CalculatorException pkgException(Throwable exception) throws UnknownException {
        return pkgException(exception, null);
    }

    public static CalculatorException pkgException(Throwable exception, String str) throws UnknownException {
        String msg = "";
        if (str != null && str != "") {
            msg = "。" + str;
        }
        throw (UnknownException)
                new UnknownException("错误：发生了 " + exception.getClass().getName() + " 异常" + msg)
                        .initCause(exception);
    }

}
