package org.wangpai.calculator.model.data;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.exception.UndefinedException;

/**
 * @since 2021-8-1
 */
@Slf4j
public abstract class OutputStream<T> implements Cloneable {
    /**
     * 为了减少子类设计的工作量，
     * 不要将此字段的类型改为 List<T>
     */
    protected ArrayList<T> outputStream;

    @Getter(AccessLevel.PUBLIC)
    protected int length;

    /**
     * 指向当前最近一个没有输出的字符。
     * 序号越小的元素越先输出
     */
    @Getter(AccessLevel.PUBLIC)
    protected int index;

    public OutputStream() {
        super();
    }

    /**
     * @since 2021-8-9
     */
    public OutputStream(OutputStream<T> other) {
        super();
        this.init(other);
    }

    /**
     * @since 2021-8-9
     */
    public OutputStream(ArrayList<T> other) {
        super();
        this.init(other);
    }

    /**
     * @since 2021-8-5
     */
    public abstract OutputStream<T> resetIndex();

    /**
     * @lastModified 2021-8-9
     * @since 2021-8-5
     */
    @Override
    protected Object clone() {
        OutputStream<T> cloned = null;
        try {
            cloned = (OutputStream<T>) super.clone();
        } catch (CloneNotSupportedException exception) {
            log.error("发生了非自定义异常：", exception);
        }
        cloned.outputStream = (ArrayList<T>) this.outputStream.clone();
        cloned.length = this.length;
        cloned.index = this.index;
        return cloned;
    }

    /**
     * 此方法会带来危险，因此不对外提供
     *
     * @lastModified 2021-8-9
     * @since 2021-8-5
     */
    @Deprecated
    protected abstract OutputStream<T> init(Object obj) throws CalculatorException;

    public abstract OutputStream<T> init(OutputStream<T> other);

    public abstract OutputStream<T> init(ArrayList<T> other);

    public abstract OutputStream<T> init(List<T> other);

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

    /**
     * 获取已输出部分的流
     *
     * @since 2021-8-9
     */
    public OutputStream<T> getRead() {
        return ((OutputStream<T>) this.clone()).init(this.outputStream.subList(0, this.index));

    }

    /**
     * 获取未输出部分的流
     *
     * @since 2021-8-9
     */
    public OutputStream<T> getRest() {
        return ((OutputStream<T>) this.clone()).init(this.outputStream.subList(this.index, this.length));

    }

    public OutputStream<T> clear() {
        this.outputStream = null;
        this.index = -1;
        this.length = 0;

        return this;
    }

    /**
     * @since 2021-8-9
     */
    public List<T> toList() {
        return (List<T>) this.outputStream.clone();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var output : this.outputStream) {
            sb.append(output.toString());
        }

        return sb.toString();
    }
}
