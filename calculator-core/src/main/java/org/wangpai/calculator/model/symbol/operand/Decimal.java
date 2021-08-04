package org.wangpai.calculator.model.symbol.operand;

import lombok.SneakyThrows;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.symbol.operation.FigureOperation;
import org.wangpai.calculator.model.symbol.operation.Operation;
import org.wangpai.calculator.model.symbol.operation.RationalNumberOperation;

import static org.wangpai.calculator.model.symbol.enumeration.Symbol.*;

/**
 * 小数。可以允许负数
 *
 * @since 2021-8-2
 */
public final class Decimal extends Figure {
    /**
     * 由于此类最终要用于转换为自定义类 RationalNumber，
     * 为了便于实现这一点，这里不使用库类 BigDecimal 作为内部实现。
     */
    private Symbol[] integerPart; // 高位的序号小（高位在前）
    private Symbol[] decimalPart; // 高位的序号小（高位在前）
    private boolean sign; // 是否有负号

    public Decimal() {
        super();
    }

    public Decimal(Symbol[] decimal) {
        super();
        this.init(decimal);
    }

    /**
     * 初始化
     *
     * @param decimal 可以含小数点、负号。高位在前
     * @since 2021-8-2
     */
    public Decimal init(Symbol[] decimal) {
        if (!arrayIsDecimal(decimal)) {
            return null;
        }

        this.sign = !Decimal.arrayIsPositive(decimal);
        int pointLocation = Decimal.locatePoint(decimal);
        int digitStart = Decimal.boolean2int(this.sign);
        int integerLength;
        int decimalLength;

        if (pointLocation == -1) {
            integerLength = decimal.length - digitStart;
            decimalLength = 0;
        } else {
            integerLength = pointLocation - digitStart;
            decimalLength = decimal.length - (pointLocation + 1);
        }

        this.integerPart = new Symbol[integerLength];
        this.decimalPart = new Symbol[decimalLength];

        System.arraycopy(decimal, digitStart,
                this.integerPart, 0, integerLength);
        System.arraycopy(decimal, pointLocation + 1,
                this.decimalPart, 0, decimalLength);

        return this;
    }

    /**
     * 初始化
     *
     * @param decimal 可以含小数点、负号。高位在前
     * @since 2021-8-2
     */
    public Decimal init(char[] decimal) throws UndefinedException {
        int symbolLength = decimal.length;
        var symbols = new Symbol[symbolLength];
        for (int order = 0; order < symbolLength; ++order) {
            symbols[order] = Symbol.getEnum(String.valueOf(decimal[order]));
            if (symbols[order] == null) {
                throw new UndefinedException("异常：使用了未定义符号");
            }
        }

        return this.init(symbols);
    }

    /**
     * @since 2021-8-3
     */
    @SneakyThrows
    @Deprecated
    public RationalNumber toRationalNumber() {
        RationalNumber result = new RationalNumber(0);
        int integerLength = this.integerPart.length;
        int decimalLength = this.decimalPart.length;

        Figure digit; // 此变量在循环外定义是为了提高效率，避免变量的反复创建
        /**
         * 整数部分的计算要从最低位开始，而在 this 中整数部分是高位在前，因此要从数组尾端开始遍历
         */
        for (int order = integerLength - 1; order >= 0; --order) {
            digit = new Figure(Integer.valueOf(this.integerPart[order].toString()));
            // FIXME
            // 下面表达式指的是：result = result + digit * pow(10, digit 对应的整数的位数 -1)
            result = (RationalNumber) RationalNumberOperation.add(result,
                    RationalNumberOperation.multiply(digit,
                            FigureOperation.power(10, integerLength - order - 1)));
        }

        /**
         * 小数部分的计算要从最高位开始，而在 this 中小数部分是高位在前，因此要从数组首端开始遍历
         */
        for (int order = 0; order < decimalLength; ++order) {
            digit = new Figure(Integer.valueOf(this.decimalPart[order].toString()));
            // FIXME
            // 下面表达式指的是：result = result + digit / pow(10, digit 对应的小数的位数)
            result = (RationalNumber) RationalNumberOperation.add(result,
                    RationalNumberOperation.divide(digit,
                            FigureOperation.power(10, order + 1)));
        }

        if (this.sign) {
            result = RationalNumberOperation.getOpposite(result);
        }

        return result;
    }


    /**
     * 判断形参是否是合法的小数
     *
     * @param decimal 可以含小数点、负号。高位在前
     * @since 2021-8-2
     */
    public static boolean arrayIsDecimal(Symbol[] decimal) {
        if (decimal == null) {
            return false;
        }
        var begin = decimal[0];
        var length = decimal.length;

        if (begin == ZERO && length > 1) { // 如果 decimal 不是 0
            if (length == 2) {
                return false;
            }
            if (decimal[1] != DOT) {
                return false;
            }
        }
        if (!(begin.isDigit() || begin == SUBTRACT)) { // 如果第一个不是数字也不是负号，返回 false
            return false;
        }

        int pointNum = 0;
        Symbol bit; // 此变量在循环外定义是为了提高效率，避免变量的反复创建
        for (int order = 1; order < length; ++order) {
            bit = decimal[order];
            if (bit.isDigit()) {
                continue;
            }
            if (bit == DOT) {
                ++pointNum;
                if (pointNum > 1) { // 如果发现小数点超过一个，返回 false
                    return false;
                } else {
                    continue;
                }
            }
            return false;
        }

        return true; // 如果前面没有返回 false，此处返回 true
    }

    /**
     * 调用该方法之前，要保证形参是符合语法的小数
     *
     * @param decimal 可以含小数点、负号。高位在前
     * @since 2021-8-2
     */
    public static boolean arrayIsPositive(Symbol[] decimal) {
        if (decimal[0] == SUBTRACT) { // 如果第一个不是数字也不是负号，返回 false
            return false;
        } else {
            return true;
        }
    }

    /**
     * 调用该方法之前，要保证形参是符合语法的小数
     *
     * 定位小数点的位置。如果没有发现小数点，将返回 -1
     *
     * @since 2021-8-2
     */
    public static int locatePoint(Symbol[] decimal) {
        for (int order = 0; order < decimal.length; ++order) {
            if (decimal[order] == DOT) {
                return order;
            }
        }
        return -1;
    }

    public static Symbol[] symbolArray2charArray(char[] charArray) throws UndefinedException {
        int symbolLength = charArray.length;
        var symbols = new Symbol[symbolLength];
        for (int order = 0; order < symbolLength; ++order) {
            var symbol = Symbol.getEnum(Character.toString(charArray[order]));
            if (symbol == null) {
                throw new UndefinedException("错误：检测到未定义符号");
            }
        }

        return symbols;
    }

    /**
     * 将 boolean 转化为 int
     *
     * @since 2021-8-2
     */
    public static int boolean2int(boolean bool) {
        return bool ? 1 : 0;
    }

}
