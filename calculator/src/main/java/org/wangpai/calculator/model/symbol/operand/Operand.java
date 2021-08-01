package org.wangpai.calculator.model.symbol.operand;

import org.wangpai.calculator.model.symbol.operation.Operation;

public interface Operand extends Cloneable {
    /**
     * 此方法不能声明为静态的，因为静态方法不能重写
     */
    @Deprecated
    default String getEnumIdentifier(){
        return "OPERAND";
    }

    /**
     * 此方法不能声明为静态的，因为静态方法不能重写
     */
     Class<? extends Operation> getBindingOperation();

    /**
     * 此方法不能依赖于方法 equals，否则会造成循环依赖
     */
    boolean isZero();

}
