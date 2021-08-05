package org.wangpai.calculator.model.symbol.operand;

import lombok.AccessLevel;
import lombok.Getter;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.symbol.operation.FigureOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;

import java.math.BigInteger;

/**
 * 整数
 *
 * 因为除法对整数有余数，因此此类不支持除法运算
 *
 * @since 2021-7-22
 */
public class Figure implements Operand {
    /**
     * 由于 lombok 生成的 set 方法返回 void，且不兼容重载，
     * 因此这里不使用注解 @Setter，而手动编写该方法
     */
    @Getter(AccessLevel.PUBLIC)
    private BigInteger integer;

    public Figure() {
        super();
    }

    /**
     * 此方法使用的是深克隆
     *
     * @since 2021-8-3
     */
    public Figure(Figure other) {
        this(other.integer);
    }

    /**
     * 此方法使用的是深克隆
     *
     * @since 2021-8-3
     */
    public Figure(BigInteger num) {
        super();
        this.integer = Figure.cloneBigInteger(num);
    }

    public Figure(long num) {
        this.integer = BigInteger.valueOf(num);
    }

    /**
     * 此方法使用的是深克隆
     *
     * @since 2021-8-3
     */
    @Deprecated
    public Figure(Operand other) throws UndefinedException {
        super();

        /**
         * 因为 Java 规定构造器 this(...) 调用必须位于第一行，
         * 所以这里只能先构造一个临时的变量，然后将其拷贝至 this
         */
        Figure temp;

        /**
         * 由于 Operand 与普通的类没有继承关系（无法将普通的类型强制转换为 Operand 类型），
         * 所以 other 不可能为类型 long、BigInteger
         */
        switch (other.getClass().getSimpleName()) {
            case "Figure":
                temp = new Figure((Figure) other);
                break;

            default:
                throw new UndefinedException("异常：不支持此类的初始化");
        }

        // 将 temp 浅拷贝至 this
        this.integer = temp.integer;
    }

    @Override
    @Deprecated
    public Class<? extends Operation> getBindingOperation() {
        return FigureOperation.class;
    }

    public static Figure valueOf(long num) {
        return new Figure(BigInteger.valueOf(num));
    }

    public Figure setInteger(BigInteger integer) {
        this.integer = integer;
        return this;
    }

    public Figure setInteger(long num) {
        return this.setInteger(BigInteger.valueOf(num));
    }

    private static BigInteger cloneBigInteger(BigInteger other) {
        return new BigInteger(other.toString());
    }

    @Override
    public Figure clone() {
        Figure cloned = new Figure();
        cloned.integer = cloneBigInteger(this.integer);
        return cloned;
    }

    @Override
    public String toString() {
        return integer.toString();
    }

    /**
     * 因为这个方法是重写方法，所以这个方法不能抛出异常
     *
     * 注意：other 不可能为基本类型
     *
     * @since 2021-8-5
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (other instanceof Operand) {
            if (other instanceof Figure) {
                return this.equals((Figure) other);
            }
        }

        return false;
    }

    /**
     * 算法：相减结果为 0 即为相等
     *
     * @since 2021-8-5
     */
    public boolean equals(Figure other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        try {
            return FigureOperation.subtract(this, other).isZero();
        } catch (Exception exception) {
            return false;  // 只要此处抛出了异常，均视为相等判断失败
        }
    }


    @Override
    public boolean isZero() {
        return this.integer.equals(BigInteger.ZERO);
    }

    public boolean isNegative() {
        /**
         * 对于 BigInteger 的函数 signum 的返回值：
         *   > 1：代表正数
         *   > 0：代表 0
         *   > -1：代表 负数
         */
        if (this.integer.signum() == -1) {
            return true;
        }

        return false;
    }
}
