package org.wangpai.calculator.model.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.SyntaxException;
import org.wangpai.calculator.exception.UndefinedException;

import java.util.ArrayList;

/**
 * @since 2021-8-1
 */
public abstract class OutputStream<T> {
    protected ArrayList<T> outputStream;
    protected int length;

    /**
     * 指向当前最近一个没有输出的字符。
     * 序号越小的元素越先输出
     */
    protected int index;

    @Getter(AccessLevel.PUBLIC)
    protected Object originInput;

    public OutputStream() {
        super();
    }

    /**
     * 注意：此方法 init 自带清空以往数据的副作用
     */
    public OutputStream<T> init(Object obj) throws CalculatorException {
        this.originInput = obj;

        return this;
    }

    public T next() {
        return this.outputStream.get(this.index++);
    }

    public boolean hasNext() {
        if (this.length <= 0) {
            return false;
        }

        return this.index < this.length;
    }

    public T peek() {
        return this.outputStream.get(this.index);
    }

    public OutputStream<T> rollback() {
        --this.index;
        return this;
    }

    public OutputStream<T> rollback(T rollbackChar) throws UndefinedException {
        if (rollbackChar == null) {
            throw new UndefinedException("异常：输入了未定义符号");
        }
        this.outputStream.set(--this.index, rollbackChar);
        return this;
    }

    public OutputStream<T> clear() {
        this.outputStream = null;
        this.index = -1;
        this.length = 0;

        return this;
    }
}
