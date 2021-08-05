package org.wangpai.calculator.model.symbol.operation;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.tool.ExceptionTool;
import org.wangpai.calculator.model.symbol.operand.Operand;


import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

/**
 * 这个类的所有方法都应该是 static 方法
 *
 * @since 2021-8-1
 */
public abstract class Operation {
    /**
     * 这类方法禁止子类定义同签名方法。原因是，这类方法内部使用反射，
     * 容易导致在动态调用方法时找不到目标方法。
     * 如果在子类也定义了这种方法，将会使用编译器的静态检查失效，
     * 加剧调用非法方法的风险
     *
     * @deprecated 2021-8-5
     */
    @Deprecated
    protected final static Operand add(Operand first, Operand second)
            throws CalculatorException {
        return callBestOperation("add", first, second);
    }

    /**
     * @deprecated 2021-8-5
     */
    @Deprecated
    protected final static Operand subtract(Operand first, Operand second)
            throws CalculatorException {
        return callBestOperation("subtract", first, second);
    }

    /**
     * @deprecated 2021-8-5
     */
    @Deprecated
    protected final static Operand multiply(Operand first, Operand second)
            throws CalculatorException {
        return callBestOperation("multiply", first, second);
    }

    /**
     * @deprecated 2021-8-5
     */
    @Deprecated
    protected final static Operand divide(Operand first, Operand second)
            throws CalculatorException {
        return callBestOperation("divide", first, second);
    }

    /**
     * 最好 first 为子类，second 为超类。因为这样能减少判断次数
     *
     * 该方法要求子类必须提供一个最佳函数。
     * 最佳函数指它的第一个形参正好是这两个形参中的子类对象，而第二个形参类型固定为 Operand 类型
     *
     * 这个方法的完成的工作是：
     * - 1. 检查形参是否有误。
     * - 2. 将形参按照继承关系进行排列，然后调用一个最佳函数
     *
     * @deprecated 2021-8-5
     */
    @Deprecated
    protected final static Operand callBestOperation(String methodName, Operand first, Operand second)
            throws CalculatorException {
        preliminaryCheck(first, second);

        var inheritanceArray = sortByInheritance(first, second);
        var subObj = (Operand) inheritanceArray[0];
        var superObj = (Operand) inheritanceArray[1];
        boolean hasSwap = (boolean) inheritanceArray[2];
        var operationClass = subObj.getBindingOperation();

        Operand result = null;

        try {
            /**
             * 注意：此处的 boolean.class 不能写成 Boolean.class
             */
            result = (Operand) operationClass
                    .getMethod("methodNavigation",
                            String.class, subObj.getClass(),
                            Operand.class, boolean.class)
                    .invoke(null, methodName, subObj, superObj, hasSwap);
        } catch (InvocationTargetException exception) {
            Throwable realException = exception.getTargetException();
            if (realException instanceof CalculatorException) {
                throw (CalculatorException) realException;
            } else {
                ExceptionTool.pkgException(realException);
            }
        } catch (Exception exception) {
            ExceptionTool.pkgException(exception);
        }

        return result;
    }

    /**
     * 对操作数的初步检查。
     * 此处的检查仅为判断 Operand 是否为 null 和
     * 操作数的类型是否均正确
     */
    private final static void preliminaryCheck(Operand... operands)
            throws SyntaxException {
        var nullPointerRecords = new HashSet<Integer>();

        final var operandNum = operands.length;

        for (int order = 0; order < operandNum; ++order) {
            if (operands[order] == null) {
                nullPointerRecords.add(order + 1);
            }
        }

        boolean hasException = false;

        if (!nullPointerRecords.isEmpty()) {
            hasException = true;
        }

        if (hasException) {
            StringBuilder nullPointerInfo = new StringBuilder();
            nullPointerInfo.append("错误：")
                    .append("第")
                    .append(generateExceptionString(nullPointerRecords))
                    .append("个操作数为 null。");
            throw new SyntaxException(nullPointerInfo.toString());
        }
    }

    /**
     * 建议第一个形参为子类，第二个形参为超类。因为这样能减少运算量
     *
     * 如果形参之间没有直接或间接的继承关系，且这两者不是同一个类，则会抛出异常
     *
     * 这里之所以形参只有两个，是因为两个以上的寻找子类对象的代码较复杂。
     * 它要求形参之间两两均有继承或同类关系，至少需要比较 C(2,n) 次
     *
     * @return 返回数组的元素依次为子类、基类、是否对形参进行了交换
     */
    private static Object[] sortByInheritance(Operand first, Operand second) throws SyntaxException {
        Class<?> subClass = first.getClass();
        Class<?> superClass = second.getClass();
        Operand subObj = first;
        Operand superObj = second;
        boolean hasSwap = false;
        /**
         * A.class.isAssignableFrom(B.class) 的含义是，
         * 如果 A 是 B 的基类或 A、B 类型相同，则返回 true。否则返回 false
         */
        if (!superClass.isAssignableFrom(subClass)) {
            if (!subClass.isAssignableFrom(superClass)) {
                /**
                 * 如果反过来进行子类判断也失败，这说明这两个形参之间没有继承关系
                 */
                throw new SyntaxException("错误：这两个操作数之间没有继承关系");
            }
            subObj = second;
            superObj = first;
            hasSwap = true;
        }
        return new Object[]{subObj, superObj, hasSwap};
    }

    private static StringBuilder generateExceptionString(HashSet<Integer> exceptionRecords) {
        StringBuilder exceptionInfo = new StringBuilder();
        int order = 0;

        for (var exceptionOrder : exceptionRecords) {
            if (order == 0) {
                exceptionInfo.append(" ");
            } else {
                exceptionInfo.append("、");
            }
            exceptionInfo.append(exceptionOrder)
                    .append(" ");
            ++order;
        }
        return exceptionInfo;
    }
}
