package org.wangpai.calculator.model.symbol.operand;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.UndefinedException;

/**
 * @since 2021-8-1
 */
public interface Operand extends Cloneable {
    /**
     * 此方法不能依赖于方法 equals，否则会造成循环依赖
     */
    boolean isZero();

    /**
     * 此方法需子类按需实现
     *
     * @since 2021-8-5
     */
    default boolean isNegative() throws CalculatorException {
        throw new UndefinedException("异常：不支持此运算");
    }

}
