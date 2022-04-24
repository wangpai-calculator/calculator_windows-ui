package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @since 2021-7-9
 */
public abstract class CalculatorException extends Exception {
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PRIVATE)
    private String exceptionMsg;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private Object data;

    /**
     * 因为本类是抽象类，所以此构造器可以声明为 protected，
     * 但它的非抽象子类的构造器只能声明为 public
     */
    protected CalculatorException() {
        super();
    }

    protected CalculatorException(String msg) {
        this(msg, null);
    }

    protected CalculatorException(String msg, Object obj) {
        super(msg);
        this.setExceptionMsg(msg);
        this.setData(obj);
    }

    public String exceptionCause() {
        return this.getExceptionMsg();
    }

    /**
     * 这个方法必须为静态的
     * 不要在这个方法中捕获该方法标签中列出的异常。将异常对外散布会更好
     *
     * @param calculatorExceptionClass 要创建的子类类型
     * @param msg 用于设置 this.exceptionMsg
     * @return 返回创建的子类对象
     *
     * @since 2021年7月7日
     */
    protected final static CalculatorException getInstance(
            Class<? extends CalculatorException> calculatorExceptionClass, String msg)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        CalculatorException calculatorException = calculatorExceptionClass.getConstructor(String.class).newInstance(msg);
        calculatorException.setExceptionMsg(msg);
        return calculatorException;
    }
}
