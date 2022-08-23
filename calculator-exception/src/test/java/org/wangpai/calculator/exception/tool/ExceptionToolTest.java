package org.wangpai.calculator.exception.tool;

import org.junit.jupiter.api.Test;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UnknownException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @since 2021-7-19
 */
public class ExceptionToolTest {
    private String msg = "错误：整数不支持除法运算";

    @Test
    public void pkgException() {
        Throwable throwable = assertThrows(UnknownException.class,
                () -> ExceptionTool.pkgException(new SyntaxException(this.msg)));
        assertEquals("错误：发生了 " + SyntaxException.class.getName() + " 异常",
                throwable.getMessage());

        var innerException = throwable.getCause();
        assertEquals(SyntaxException.class, innerException.getClass());
        assertEquals(this.msg, innerException.getMessage());
    }

    @Test
    public void pkgException_str() {
        Throwable throwable = assertThrows(UnknownException.class,
                () -> ExceptionTool.pkgException(new SyntaxException(this.msg), this.msg));
        assertEquals("错误：发生了 " + SyntaxException.class.getName()
                        + " 异常" + "。" + this.msg,
                throwable.getMessage());

        var innerException = throwable.getCause();
        assertEquals(SyntaxException.class, innerException.getClass());
        assertEquals(this.msg, innerException.getMessage());
    }
}
