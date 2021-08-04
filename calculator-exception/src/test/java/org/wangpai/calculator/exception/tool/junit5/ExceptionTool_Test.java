package org.wangpai.calculator.exception.tool.junit5;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UnknownException;
import org.wangpai.calculator.exception.tool.ExceptionTool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @since 2021-7-19
 */
class ExceptionTool_Test {
    private String msg="错误：整数不支持除法运算";
    @Test
    void pkgException() {
        Throwable throwable = assertThrows(UnknownException.class,
                () -> ExceptionTool.pkgException(new SyntaxException(msg)));
        assertEquals("错误：发生了 " + SyntaxException.class.getName() + " 异常",
                throwable.getMessage());

        var innerException = throwable.getCause();
        assertEquals(SyntaxException.class, innerException.getClass());
        assertEquals(msg, innerException.getMessage());
    }

    @Test
    void pkgException_str() {
        Throwable throwable = assertThrows(UnknownException.class,
                () -> ExceptionTool.pkgException(new SyntaxException(msg), msg));
        assertEquals("错误：发生了 " + SyntaxException.class.getName()
                        + " 异常" + "。" + msg,
                throwable.getMessage());

        var innerException = throwable.getCause();
        assertEquals(SyntaxException.class, innerException.getClass());
        assertEquals(msg, innerException.getMessage());

    }
}