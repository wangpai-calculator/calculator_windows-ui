package org.wangpai.calculator.model.symbol.enumeration;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * 操作符、运算符底层的符号表示
 *
 * 这里的符号分为：
 * - 一位操作数。序号为 1-99
 * - 运算符。序号为 100+。其中：
 * + + N 元运算符：序号为 100-199
 * + + 非 N 元运算符：序号为 200-299
 * - 特殊功能符号。序号为 1000+
 *
 * 其中，运算符分为 N 元运算符（meta operator）与非 N 元运算符。
 * 例如，四则运算属于二元运算符，小数点属于非 N 元运算符
 *
 * 特殊功能符号指无特定意义的符号，这种符号的语义由使用者临时指定
 *
 * @since 2021-8-2
 */
public enum Symbol {
    /**
     * 下列枚举的此类中定义的顺序不能随意改动
     */
    ZERO("0", 0),
    ONE("1", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),

    ADD("+", 100),
    SUBTRACT("-", 101),
    MULTIPLY("X", 102,
            "x", "*"),
    DIVIDE("/", 103),

    EQUAL("=", 200),
    LEFT_BRACKET("(", 201),
    RIGHT_BRACKET(")", 202),

    DOT(".", 210),

    SPACE(" ", 1000), // 空格

    WRAP(" ", 1001), // 换行

    F1("#", 1100),

    S1("[", 1200), // 左小括号的替代显示符号
    S2("]", 1201); // 右小括号的替代显示符号

    private final String symbol;

    @Getter(AccessLevel.PUBLIC)
    private final int order;

    private final boolean hasExtend;

    // 符号外形的拓展
    private String[] extendSymbols;

    Symbol(String symbol, int order) {
        this.symbol = symbol;
        this.order = order;
        this.hasExtend = false;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    Symbol(String symbol, int order, String... symbols) {
        this.symbol = symbol;
        this.order = order;
        this.hasExtend = true;
        this.extendSymbols = symbols;
    }


    /**
     * 因为枚举具有唯一性，而 String 类型的 targetSymbol
     * 与 Symbol 类型的 aSymbol 具有多对一的关系，
     * 因此，此方法进行从 String 到 Symbol 之间的转化时，会丢失原 String 的信息
     *
     * @since 2021-7-30
     */
    public static Symbol getEnum(String targetSymbol) {
        var symbols = Symbol.values();
        for (var aSymbol : symbols) {
            if (aSymbol.symbol.equals(targetSymbol)) {
                return aSymbol;
            } else if (aSymbol.hasExtend) {
                for (var bSymbol : aSymbol.extendSymbols) {
                    if (bSymbol.equals(targetSymbol)) {
                        return aSymbol;
                    }
                }
            }
        }

        // 考虑未定义按钮，此方法不要抛出异常
        return null;
    }

    public boolean isDigit() {
        var symbolsOrder = this.getOrder();
        if (symbolsOrder >= 0 && symbolsOrder <= 9) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBracket() {
        return this == Symbol.LEFT_BRACKET || this == Symbol.RIGHT_BRACKET;
    }

    /**
     * 是否为二元运算符
     */
    public boolean isBinaryOperator() {
        var symbolsOrder = this.getOrder();
        if (symbolsOrder >= 100 && symbolsOrder <= 110) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Meta Operator：元运算符
     *
     * 注意：小数点属于非 N 元运算符
     */
    public boolean isMetaOperator() {
        if (this.isDigit()) {
            return false;
        }
        var symbolsOrder = this.getOrder();
        if (symbolsOrder >= 200 && symbolsOrder <= 299) {
            return true;
        }

        return false;
    }

    public boolean isFunctionSymbol() {
        var symbolsOrder = this.getOrder();
        if (symbolsOrder >= 1000 && symbolsOrder <= 9999) {
            return true;
        } else {
            return false;
        }
    }
}