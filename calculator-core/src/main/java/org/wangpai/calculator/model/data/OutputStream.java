package org.wangpai.calculator.model.data;

import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.UndefinedException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;

/**
 * @since 2021-8-1
 */
public abstract class OutputStream<T> implements Cloneable{
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
     * @since 2021-8-5
     */
    public abstract OutputStream<T> resetIndex();

    /**
     * @since 2021-8-5
     */
    @SneakyThrows
    @Override
    protected Object clone() {
        var cloned = (OutputStream<T>) super.clone();
        cloned.outputStream = (ArrayList<T>) this.outputStream.clone();
        cloned.length = this.length;
        cloned.index = this.index;
        cloned.originInput = this.originInput;
        return cloned;
    }

    /**
     * 这个方法需要抛出异常，且需要子类重写
     *
     * 注意：此方法自带清空以往数据的副作用
     * <p>
     * @since 2021-8-5
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

    /**
     * 将 rollbackedData 退回流中
     *
     * @since before 2021-8-5
     */
    public OutputStream<T> rollback(T rollbackedData) throws UndefinedException {
        if (rollbackedData == null) {
            throw new UndefinedException("异常：输入了未定义符号");
        }
        this.outputStream.set(--this.index, rollbackedData);
        return this;
    }

    /**
     * 将流重置为刚刚初始化后的状态
     *
     * @since 2021-8-5
     */
    public OutputStream<T> backToInit() {
        this.resetIndex();
        return this;
    }

    public OutputStream<T> clear() {
        this.outputStream = null;
        this.index = -1;
        this.length = 0;

        return this;
    }
}
