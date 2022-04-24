package org.wangpai.calculator.model.symbol.operator;

import lombok.AccessLevel;
import lombok.Getter;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;

/**
 * @since 2021-7-28
 */
public class Operator {
    @Getter(AccessLevel.PUBLIC)
    protected Symbol symbol;

    public Operator() {
        super();
    }

    public Operator(Symbol operator) throws SyntaxException {
        super();

        if (operator.isDigit()) {
            throw new SyntaxException("异常：误将操作数当成操作符");
        }
        this.symbol = operator;
    }

    /**
     * 相等方法的预检查。每个 equals 方法都要调用此方法，且要首先调用此方法
     */
    public boolean preEqualsCheck(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        return true;
    }

    public boolean equals(Operator other) {
        if (!preEqualsCheck(other)) {
            return false;
        }

        return this.symbol == other.symbol;
    }

    public boolean equals(Symbol other) {
        if (!preEqualsCheck(other)) {
            return false;
        }

        try {
            return this.equals(new Operator(other));
        } catch (CalculatorException exception) {
            // 如果上面的 try 块引发了异常，直接视为不相等
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!preEqualsCheck(other)) {
            return false;
        }

        if (other.getClass() == Symbol.class) {
            return this.equals((Symbol) other);
        }
        if (other instanceof Operator) {
            return this.equals((Operator) other);
        }

        return false;
    }

    /**
     * @since 2021-7-28
     * @lastModified 2021-8-9
     */
    @Override
    public String toString() {
        return this.symbol.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
