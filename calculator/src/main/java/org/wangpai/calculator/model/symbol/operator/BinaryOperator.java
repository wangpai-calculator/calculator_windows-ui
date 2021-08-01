package org.wangpai.calculator.model.symbol.operator;


import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;

@Deprecated
public class BinaryOperator extends Operator {
    BinaryOperator() {
        super();
    }

    BinaryOperator(Symbol operator) throws SyntaxException {
        super(operator);
    }

    @Override
    public boolean equals(Object otherOperator) {
        return super.equals(otherOperator);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
