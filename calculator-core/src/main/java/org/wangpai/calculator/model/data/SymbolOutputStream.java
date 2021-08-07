package org.wangpai.calculator.model.data;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 2021-8-1
 */
public final class SymbolOutputStream extends OutputStream<Symbol> {
    // SS：semantic segmentation 语义分割
    protected List<String> sSInfo;

    // 是否调用过了方法 super.init
    private boolean ifSuperInitCalled = false;

    public SymbolOutputStream() {
        super();
    }

    /**
     * 将 index 恢复到刚初始化之后的状态
     *
     * @since 2021-8-5
     */
    @Override
    public OutputStream<Symbol> resetIndex() {
        super.index = 0;
        return this;
    }

    /**
     * @since 2021-8-5
     */
    @Override
    protected Object clone() {
        var cloned = (SymbolOutputStream) super.clone();
        cloned.sSInfo = this.sSInfo;
        cloned.ifSuperInitCalled = this.ifSuperInitCalled;
        return super.clone();
    }

    /**
     * 将输入进行语义解析。如果一个运算符由二个及以上的元素组成，此方法会将这几个元素合并
     * <p>
     * 如果此方法检测出了 charArray 中的错误，会将具体的错误点作为异常的第二个参数抛出
     * <p>
     * 因为目前每个按钮对应的符号都由一个字符组成，所以目前这个函数不需要作额外的事情
     */
    private static List<String> tidy(char[] charArray) throws UndefinedException {
        var result = new ArrayList<String>();
        for (var ch : charArray) {
            result.add(Character.toString(ch));
        }

        if (false) { // 示意代码
            throw new UndefinedException("敬请期待", charArray);
        }

        return result;
    }

    /**
     * @return 返回值为 null，代表本方法没有检测出错误。
     * 如果检测出错误，则此方法会将前面没有错误的字符串返回。
     * 如果输入的字符串在一开始就有错误，则将返回空串（不是 null）
     */
    private static String preInitCheck(char[] charArray) {
        List<String> originSymbols = null;
        try {
            originSymbols = SymbolOutputStream.tidy(charArray);
        } catch (UndefinedException exception) {
            var data = exception.getData();
            if (data instanceof String) {
                return (String) data;
            }
        }

        // 已检测的部分
        StringBuilder detectedParts = new StringBuilder();

        for (var originSymbol : originSymbols) {
            Symbol symbol = Symbol.getEnum(originSymbol);
            if (symbol == null) {
                return detectedParts.toString();
            } else {
                detectedParts.append(originSymbol);
            }
        }

        return null;
    }

    public static String preInitCheck(String str) {
        return SymbolOutputStream.preInitCheck(str.toCharArray());
    }

    /**
     * 注意：此方法 init 自带清空以往数据的副作用
     */
    @Override
    public SymbolOutputStream init(Object obj) throws UndefinedException {
        try {
            if (!ifSuperInitCalled) {
                super.init(obj);
                this.ifSuperInitCalled = true;
            }
        } catch (CalculatorException ignored) {
            // 上面的 try 块实际上不会抛出异常
        }

        if (obj.getClass() == String.class) {
            return this.init((String) obj);
        }
        if (obj.getClass() == char[].class) {
            return this.init((char[]) obj);
        }

        return null;
    }

    public SymbolOutputStream init(String str) throws UndefinedException {
        try {
            if (!ifSuperInitCalled) {
                super.init(str);
                this.ifSuperInitCalled = true;
            }
        } catch (CalculatorException ignored) {
            // 上面的 try 块实际上不会抛出异常
        }

        this.init(str.toCharArray());

        return this;
    }

    public SymbolOutputStream init(char[] charArray) throws UndefinedException {
        try {
            if (!ifSuperInitCalled) {
                super.init(charArray);
                this.ifSuperInitCalled = true;

            }
        } catch (CalculatorException ignored) {
            // 上面的 try 块实际上不会抛出异常
        }

        var preInitCheck = SymbolOutputStream.preInitCheck(charArray);
        if (preInitCheck != null) {
            throw new UndefinedException("异常：输入了未定义符号", preInitCheck);
        }

        resetIndex();
        super.length = charArray.length;
        super.outputStream = new ArrayList<>();
        this.sSInfo = SymbolOutputStream.tidy(charArray);

        for (var originSymbol : this.sSInfo) {
            Symbol symbol = Symbol.getEnum(originSymbol);
            super.outputStream.add(symbol);
        }

        return this;
    }
}
