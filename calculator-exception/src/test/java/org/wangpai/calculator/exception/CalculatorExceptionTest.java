package org.wangpai.calculator.exception;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 注意：Maven 的测试插件默认只会测试如下的类方法：
 * > 类的访问权限为 public，且类名含 test
 * > 类的默认构造器的访问权限为 public
 * > 方法的访问权限为 public，且方法名的前缀为 test
 *
 * @since 2021-7-19
 */
public class CalculatorExceptionTest {
    private CalculatorException calculatorException = new CalculatorExceptionExtends("测试信息");
    private String msg = "测试信息";

    /**
     * 这个方法可以声明为 default，因为测试方法实际上是由该测试类的子类来调用的，这是因为测试类含有测试注解
     */
    public CalculatorExceptionTest() {
        super();
    }

    @Test
    public void exceptionCause() {
        /**
         * 函数 assertEquals 内部会使用类 Object 的函数 equals 来比较，所以无需担心
         */
        assertEquals(this.msg, this.calculatorException.exceptionCause());
    }

    @Test
    public void getInstance()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        var calculatorExceptionInstance = ((CalculatorExceptionExtends) this.calculatorException).getInstance(this.msg);

        assertEquals(CalculatorExceptionExtends.class, calculatorExceptionInstance.getClass());
        assertEquals(this.msg, calculatorExceptionInstance.exceptionCause());
    }

    /**
     * 这个类必须声明为 static，否则将导致：没有这个构造器异常。
     * 这个类不能声明为 private 和 default，否则将导致：非法访问异常。
     */
    protected static class CalculatorExceptionExtends extends CalculatorException {
        public CalculatorExceptionExtends() {
            super();
        }

        public CalculatorExceptionExtends(String msg) {
            super(msg);
        }

        /**
         * 因为本类是静态的，所有此函数的形参名并不会与外部类的字段相冲突
         */
        public CalculatorException getInstance(String msg)
                throws InvocationTargetException, NoSuchMethodException,
                InstantiationException, IllegalAccessException {
            return CalculatorException.getInstance(CalculatorExceptionExtends.class, msg);
        }
    }
}
