package org.wangpai.calculator.model.data;

@Deprecated
public class CharOutputStream {
    private char[] charStream;
    private int length;
    private int index; // 指向当前最近一个没有输出的字符

    public CharOutputStream() {
        super();
    }

    /**
     * 注意：此方法 init 自带清空以往数据的副作用
     */
    public CharOutputStream init(String str) {
        this.init(str.toCharArray());

        return this;
    }

    public CharOutputStream init(char[] charArray) {
        this.charStream = charArray;
        this.index = 0;
        this.length = this.charStream.length;

        return this;
    }

    public char next() {
        return this.charStream[this.index++];
    }

    public boolean hasNext() {
        if (this.length <= 0) {
            return false;
        }

        return this.index < this.length;
    }

    public char peek() {
        return this.charStream[this.index];
    }

    public CharOutputStream rollback() {
        --this.index;
        return this;
    }

    public CharOutputStream rollback(char rollbackChar) {
        this.charStream[--this.index] = rollbackChar;
        return this;
    }

    public CharOutputStream clear() {
        this.charStream = null;
        this.index = -1;
        this.length = 0;

        return this;
    }

    @Deprecated
    public CharOutputStream reset(String str) {
        return this.init(str);
    }

}

