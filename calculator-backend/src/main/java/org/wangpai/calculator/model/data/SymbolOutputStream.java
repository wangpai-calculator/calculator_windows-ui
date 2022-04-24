package org.wangpai.calculator.model.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import org.wangpai.calculator.exception.UndefinedException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;

/**
 * @since 2021-8-1
 */
public final class SymbolOutputStream extends OutputStream<Symbol> {
    /**
     * SS：semantic segmentation 语义分割
     * <p>
     * 注意：此字段的形参不要改为“List<Symbol []>”
     * <p>
     * 如果本对象是由特殊的方法初始化的，则此字段可能为 null
     */
    protected List<String> sSInfo;

    public SymbolOutputStream() {
        super();
    }

    /**
     * 尽量不要使用此方法进行初始化
     * <p>
     * 注意：此方法 init 自带清空 this 以往数据的副作用
     *
     * @lastModified 2021-8-9
     * @since 2021-8-1
     */
    @Deprecated
    @Override
    protected SymbolOutputStream init(Object obj) throws UndefinedException {
        /**
         * 由于 Java 语法不允许类型带泛型，所以此方法不提供带泛型变量的类型的初始化
         */
        if (obj.getClass() == SymbolOutputStream.class) {
            return this.init((SymbolOutputStream) obj);
        }
        if (obj.getClass() == String.class) {
            return this.init((String) obj);
        }
        if (obj.getClass() == char[].class) {
            return this.init((char[]) obj);
        }

        throw new UndefinedException("异常：无法使用此数据进行初始化");
    }

    /**
     * @since 2021-8-9
     */
    public SymbolOutputStream init(SymbolOutputStream other) {
        this.init((OutputStream<Symbol>) other);
        return this;
    }

    /**
     * @since 2021-8-9
     */
    @Override
    public SymbolOutputStream init(OutputStream<Symbol> other) {
        if (other instanceof SymbolOutputStream) {
            super.outputStream = (ArrayList<Symbol>) other.outputStream.clone();
            super.length = other.length;
            super.index = other.index;

            this.sSInfo = ((SymbolOutputStream) other).sSInfo;
        } else {
            this.init(other.outputStream);
        }
        return this;
    }

    /**
     * @lastModified 2021-8-8
     * @since 2021-8-1
     */
    public SymbolOutputStream init(String str) throws UndefinedException {
        return this.init(str.toCharArray());
    }

    public SymbolOutputStream init(Symbol[] symbols) {
        this.sSInfo = null;
        super.length = symbols.length;
        super.outputStream = new ArrayList<>();
        resetIndex();

        super.outputStream = new ArrayList<>(Arrays.asList(symbols));

        return this;
    }

    public SymbolOutputStream init(char[] charArray) throws UndefinedException {
        var preInitCheck = SymbolOutputStream.preInitCheck(charArray);
        if (preInitCheck != null) {
            throw new UndefinedException("异常：输入了未定义符号", preInitCheck);
        }

        this.sSInfo = SymbolOutputStream.tidy(charArray);
        super.length = this.sSInfo.size();
        super.outputStream = new ArrayList<>(super.length);
        resetIndex();

        for (var originSymbol : this.sSInfo) {
            Symbol symbol = Symbol.getEnum(originSymbol);
            super.outputStream.add(symbol);
        }

        return this;
    }

    /**
     * @since 2021-8-9
     */
    @Override
    public SymbolOutputStream init(List<Symbol> other) {

        return this.init(new ArrayList<>(other));
    }

    /**
     * @since 2021-8-9
     */
    @Override
    public SymbolOutputStream init(ArrayList<Symbol> other) {
        return this.init(other.toArray(Symbol[]::new));
    }

    /**
     * 注意：对于 Stack，其栈底的序号为 0，入栈、出栈操作均是在栈顶进行的
     *
     * @since 2021-8-8
     */
    public SymbolOutputStream init(Stack<Symbol> symbols) {
        return this.init((List) Arrays.asList(symbols));
    }

    /**
     * 将 index 恢复到刚初始化之后的状态
     *
     * @since 2021-8-5
     * @lastModified 2021-8-9
     */
    @Override
    public SymbolOutputStream resetIndex() {
        super.index = 0;
        return this;
    }

    /**
     * @lastModified 2021-8-9
     * @since 2021-8-5
     */
    @Override
    protected Object clone() {
        var cloned = (SymbolOutputStream) super.clone();
        cloned.sSInfo = this.sSInfo;
        return super.clone();
    }

    /**
     * 将输入进行语义解析。如果一个运算符由二个及以上的元素组成，此方法会将这几个元素合并
     * <p>
     * 如果此方法检测出了 symbols 中的错误，会将具体的错误点作为异常的第二个参数抛出
     * <p>
     * 因为目前每个按钮对应的符号都由一个字符组成，所以目前这个函数不需要作额外的事情
     *
     * @since 2021-8-1
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

    /**
     * 由于泛型，此方法无法设置在超类中
     *
     * @since 2021-8-9
     */
    public Symbol[] toArray() {
        return this.outputStream.toArray(Symbol[]::new);
    }

    public static String preInitCheck(String str) {
        return SymbolOutputStream.preInitCheck(str.toCharArray());
    }
}
