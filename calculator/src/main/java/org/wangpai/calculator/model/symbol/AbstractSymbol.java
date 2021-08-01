package org.wangpai.calculator.model.symbol;

@Deprecated
public abstract class AbstractSymbol {
    /**
     * 判断两个数符号是否相同。
     * 本类的判断标准是，不为 null 即可
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        return true;
    };
}
