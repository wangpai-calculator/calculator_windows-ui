package org.wangpai.calculator.view.base;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.model.universal.Function;
import org.wangpai.calculator.model.universal.Multithreading;

/**
 * 关于 JavaFX 的 TextArea 的文本区光标知识介绍：
 * 光标只能位于每个字符的间隙，它的范围是 [0, length]。length 为文本区中文本的长度。
 * 注意：空隙的数量比文字多 1。文本区中所有的坐标都指的是光标的坐标。
 * 特别地，当光标在开头时，光标位置为 0。
 * 当选中文本时，选中文本的坐标范围为 第一个选中字符的左空隙 至 最后一个选中字符的右空隙
 */

/**
 * @since 2021年9月25日
 */
@Slf4j
public abstract class TextBox implements FxComponent {
    protected TerminalController controller;

    private TextArea textArea;

    /**
     * 焦点优先权。此值为 true 的文本框有资格在执行完方法之后，将焦点转移到自身
     */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private boolean focusPriority;

    protected TextBox() {
        super();
    }

    /**
     * 这个方法必须要在使用本类的其它方法之前调用
     *
     * @since 2021年9月25日
     */
    protected void initSuperTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * @since 2021-9-25
     */
    @Override
    public void setLinker(SpringLinker linker) {
        if (linker instanceof TerminalController) {
            this.controller = (TerminalController) linker;
        }
    }

    /**
     * 此方法不会返回 null
     *
     * @since 2021-8-6
     */
    public String getText() {
        if (this.textArea.getText() == null) {
            return "";
        }
        return this.textArea.getText();
    }

    /**
     * 这个方法不能在此提前写好，需要子类重写，
     * 因为不同子类与此方法的需求是不同的。
     * 且为了起到示例的作用，也不要设为抽象方法。
     *
     * 子类的额外的需求可能有：
     * > 将滚动条拨到最下
     * > 将焦点转移到本文本框
     *
     * 算法如下：
     * （此算法是利用了方法 this.textArea.appendText 不会使 redo、undo 失效的特性）
     * 1. 清除文本中的所有内容
     * 2. 之后，追加新的内容
     *
     * @since 2021-8-6
     */
    public TextBox setText(String msg) {
        this.cleanAllContent().append(msg);
        if (this.isFocusPriority()) {
            this.requestFocus();
        }
        return this;
    }

    /**
     * 原始的 setText 方法。
     * 此方法会令 redo 与 undo 失效，慎用此方法
     *
     * @since 2021-9-27
     */
    @Deprecated
    public final TextBox setTextOriginally(String msg) {
        this.textArea.setText(msg);
        return this;
    }

    /**
     * 这个方法需要子类重写且不能在此提前写好，
     * 因为不同子类与此方法的需求是不同的。
     * 且为了起到示例的作用，也不要设为抽象方法。
     *
     * 子类的额外的需求可能有：
     * > 将滚动条拨到最下
     * > 将焦点转移到本文本框
     *
     * 此方法不会使 redo 与 undo 失效
     *
     * @since 2021-8-6
     */
    public TextBox append(String msg) {
        this.textArea.appendText(msg);
        if (this.isFocusPriority()) {
            this.requestFocus();
        }
        return this;
    }

    /**
     * 删除选中字符，并插入指定字符
     *
     * @since 2021-8-5
     */
    public TextBox insert(String str) {
        this.replaceSelectedText(str);
        if (this.isFocusPriority()) {
            this.requestFocus();
        }
        return this;
    }

    /**
     * @since 2021-9-27
     */
    public TextBox undo() {
        this.textArea.undo();
        if (this.isFocusPriority()) {
            this.requestFocus();
        }
        return this;
    }

    /**
     * @since 2021-9-27
     */
    public TextBox redo() {
        this.textArea.redo();
        if (this.isFocusPriority()) {
            this.requestFocus();
        }
        return this;
    }

    /**
     * @since 2021年9月26日
     */
    public TextBox requestFocus() {
        this.textArea.requestFocus();
        return this;
    }

    /**
     * 此方法不会返回 null。使用前应注意判空：this.getSelectedText().equals("")
     *
     * @since 2021-8-5
     * @lastModified 2021年9月26日
     */
    public String getSelectedText() {
        if (this.textArea.getSelectedText() == null) {
            return "";
        }
        return this.textArea.getSelectedText();
    }

    /**
     * @since 2021-8-6
     */
    public int getCaretPosition() {
        return this.textArea.getCaretPosition();
    }

    /**
     * 当它的光标位置越界时，不会抛出异常，而是将光标定位到最前或最后
     *
     * 注意：光标的范围是 [0, length]。因为光标指向文字的空隙，而空隙的数量比文字多 1
     *
     * @since 2021-8-6
     * @lastModified 2021年9月26日
     */
    public TextBox setCaretPosition(int position) {
        this.requestFocus(); // 先把焦点转移到文本区
        this.textArea.positionCaret(this.correctCaretPositionNum(position));
        return this;
    }

    /**
     * @since 2021-8-6
     */
    public TextBox leftShift() {
        return this.setCaretPosition(this.getCaretPosition() - 1);
    }

    /**
     * @since 2021-8-6
     */
    public TextBox rightShift() {
        return this.setCaretPosition(this.getCaretPosition() + 1);
    }

    /**
     * 在光标后添加字符
     *
     * @since 2021-8-5
     * @lastModified 2021-9-27
     */
    public TextBox addCursorStr(String str) {
        this.textArea.insertText(this.getCaretPosition(), str);
        return this;
    }

    /**
     * @since 2021-9-27
     */
    public TextBox replaceText(int front, int end, String str) {
        this.textArea.replaceText(front, end, str);
        return this;
    }

    /**
     * 删除选中文本并用指定字符替换。如果没有选中文本，相当于直接插入，不会引发异常
     *
     * @since 2021-8-5
     * @lastModified 2021-9-27
     */
    public TextBox replaceSelectedText(String str) {
        this.textArea.replaceSelection(str);
        return this;
    }

    /**
     * @return 返回选中文本的首尾坐标。
     * 起点坐标为第一个被选中的文本坐标，从 0 开始。
     * 终点坐标为第一个没有被选中的文本坐标
     * 如果没有选中文本，这两个坐标将设置为光标的坐标
     *
     * @since 2021-8-5
     */
    @Deprecated
    public int[] getSelectedCoordinate() {
        var coordinate = this.textArea.getSelection();
        return new int[]{coordinate.getStart(),
                coordinate.getEnd()};
    }

    /**
     * 为避免循环依赖，此方法的实现不能依赖本类的方法
     *
     * @since 2021-8-6
     * @lastModified 2021-9-27
     */
    public TextBox cleanAllContent() {
        this.deleteRangeText(0, this.textArea.getText().length());
        return this;
    }

    /**
     * @since 2021-8-6
     */
    public void selectAll() {
        this.textArea.selectAll();
        this.requestFocus(); // 焦点转移到文本区
    }

    /**
     * 删除选中文本或光标所在位置之前最近的一个字符
     *
     * @since 2021-8-5
     */
    public TextBox delete() {
        this.requestFocus();
        if (this.getSelectedText().equals("")) {
            return this.deleteCursorChar();
        } else {
            return this.deleteSelectedText();
        }
    }

    /**
     * @since 2021-9-27
     */
    public TextBox deleteRangeText(int front, int end) {
        this.textArea.deleteText(front, end);
        return this;
    }

    /**
     * 删除光标所在位置之前最近的一个字符
     *
     * @since 2021-8-5
     */
    public TextBox deleteCursorChar() {
        this.requestFocus();
        var caretPosition = this.getCaretPosition();

        // 当光标在开头时，什么也不干
        if (caretPosition == 0) {
            return this;
        }

        return this.deleteRangeText(caretPosition - 1, caretPosition);
    }

    /**
     * 删除选中文本。如果没有选中文本，不会发生任何效果，也不会引发异常
     *
     * @since 2021-8-5
     */
    public TextBox deleteSelectedText() {
        return this.replaceSelectedText("");
    }

    /**
     * @since 2021-9-26
     */
    private String getFrontText(int gapOrder) {
        return this.textArea.getText(0, gapOrder);
    }

    /**
     * @since 2021-9-26
     */
    private String getEndText(int gapOrder) {
        return this.textArea.getText(gapOrder, this.textArea.getText().length());
    }

    /**
     * 将滚动条设置在底部
     *
     * @since 2021-8-6
     * @lastModified 2021-9-26
     */
    public TextBox setBarAtTheBottom() {
        var textArea = this.textArea;
        /**
         * 因为 JavaFX 的 Bug，将滑条置底的方法在滑条第一次出现时不会起作用，第二次才会起作用。
         * 因此，将滑条置底的方法用 Platform.runLater 来包装，这样此方法就相当于在第二次才会调用。
         */
        Multithreading.execute(new Function() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception exception) {
                    log.error("发生了非自定义异常：", exception);
                }
                Platform.runLater(() -> {
                    textArea.setScrollTop(Double.MAX_VALUE);
                });
            }
        });

        return this;
    }

    /**
     * 对传入错误的光标值进行纠正。
     * 当传入光标位置越界时，不会抛出异常，而是将光标值定位到最前或最后
     *
     * @since 2021-8-6
     */
    private int correctCaretPositionNum(int position) {
        int textLength = this.getText() == null ? 0 : this.getText().length();

        if (textLength == 0) {
            return 0; // 当文本区没有字符时，什么也不干
        }
        if (position <= 0) { // 如果光标不在起点处
            return 0;
        }
        if (position >= textLength) { // 如果光标不在起点处
            return textLength;
        }

        return position;
    }

    /**
     * 去除 str 中的空格、换行等占位无意义符号
     *
     * @since 2021-8-6
     */
    public static String removeUselessChar(String str) {
        return str.replaceAll("[\\s\\t\\n\\r]", "");
    }
}
