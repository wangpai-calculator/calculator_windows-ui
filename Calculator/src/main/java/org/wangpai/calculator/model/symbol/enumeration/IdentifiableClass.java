package org.wangpai.calculator.model.symbol.enumeration;


import org.wangpai.calculator.model.symbol.operand.Fraction;
import org.wangpai.calculator.model.symbol.operand.RationalNumber;

@Deprecated
public enum IdentifiableClass {
    FRACTION(Fraction.class),
    RATIONALNUMBER(RationalNumber.class);

    private Class<?> identifiableClass;

    IdentifiableClass(Class<?> operandClass) {
        this.identifiableClass = operandClass;
    }

    public static IdentifiableClass getInstance(String classIdentifier) {
        return Enum.valueOf(IdentifiableClass.class, classIdentifier);
    }

    public Class<?> getIdentifiableClass() {
        return identifiableClass;
    }
}
