package org.wangpai.calculator.view.base;

import org.wangpai.calculator.view.control.Gbc;

import lombok.SneakyThrows;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollButton;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Insets;


/**
 * @since 2021-8-6
 * <p>
 * 此类的子类还需要进行的设置为：(其中的参数供参考)
 * this.textArea.setFont(new Font("Dialog", 0, 18));
 * this.textArea.setEditable(false);
 */
public abstract class TextBox extends JPanel {
    public static final int TEXT_ROWS = 10;
    public static final int TEXT_COLUMNS = 40;

    protected JTextArea textArea;
    protected JScrollPane scrollPane;
    protected GridBagLayout gb;

    /**
     * @since 2021-8-6
     */
    protected TextBox() {
        this(TEXT_ROWS, TEXT_COLUMNS);
    }

    /**
     * @since 2021-8-6
     */
    protected TextBox(int rows, int columns) {
        super();
        this.textArea = new JTextArea(rows, columns);
        this.textArea.setEnabled(true);
        this.textArea.setLineWrap(true);
        int insets = 10;
        // 设置内边距：文字与文本边界之间的间距
        this.textArea.setMargin(new Insets(insets, insets, insets, insets));

        this.scrollPane = new JScrollPane(this.textArea);
        // 设置滚动条阴影区域的颜色
        this.scrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
        // 设置滚动条上升与下降按钮的背景颜色
        this.setBarColor(Color.WHITE);

        this.gb = new GridBagLayout();
        this.gb.setConstraints(this.scrollPane,
                new Gbc(0, 0)
                        .setWeight(100, 200) // Weight 在此处可以随意取值，但是不能不设置
                        .setFill(Gbc.BOTH)); // 此值如果不设置，则组件会萎缩
        this.setLayout(this.gb);

        this.add(this.scrollPane);
    }

    /**
     * 这个方法需要子类重写且不能在此提前写好，
     * 因为不同子类与此方法的需求是不同的
     *
     * 子类的额外的需求可能有：
     * > 将滚动条拨到最下
     * > 将焦点转移到本文本框
     *
     * @since 2021-8-6
     */
    public TextBox setText(String msg) {
        this.textArea.setText(msg);
        return this;
    }

    /**
     * 这个方法需要子类重写且不能在此提前写好，
     * 因为不同子类与此方法的需求是不同的
     *
     * 子类的额外的需求可能有：
     * > 将滚动条拨到最下
     * > 将焦点转移到本文本框
     *
     * @since 2021-8-6
     */
    public TextBox append(String msg) {
        this.textArea.append(msg);
        return this;
    }

    /**
     * @since 2021-8-6
     */
    public String getText() {
        return this.textArea.getText();
    }

    /**
     * @since 2021-8-6
     */
    public TextBox cleanAllContent(String msg) {
        return this.setText(null);
    }

    /**
     * @since 2021-8-6
     */
    public int getCaretPosition() {
        return this.textArea.getCaretPosition();
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
     */
    public TextBox addCursorStr(String str) {
        var caretPosition = this.getCaretPosition();
        var originText = this.getText();
        var result = originText.substring(0, caretPosition)
                + str
                + originText.substring(caretPosition);
        return this.setText(result).setCaretPosition(caretPosition + str.length());
    }

    /**
     * 删除光标所在位置之前最近的一个字符
     *
     * @since 2021-8-5
     */
    public TextBox deleteCursorChar() {
        var caretPosition = this.getCaretPosition();
        this.requestFocus();

        // 当光标在开头时，什么也不干
        if (caretPosition - 1 == -1) {
            return this;
        }

        var originText = this.getText();
        var result = originText.substring(0, caretPosition - 1)
                + originText.substring(caretPosition);
        return this.setText(result).setCaretPosition(caretPosition - 1);
    }

    /**
     * 删除选中文本。如果没有选中文本，不会发生任何效果，也不会引发异常
     * <p>
     * 算法：
     * 1. 获得选中文本的首尾坐标。
     * 2. 根据此坐标截取文本框中选中文本左边和右边的文本。
     * 3. 将截取到的这两部分文本拼接并设置在文本框中
     * 4. 将光标设置为选中文本的首部坐标
     *
     * @since 2021-8-5
     */
    @SneakyThrows
    public TextBox deleteSelectedText() {
        var coordinate = this.getSelectedCoordinate();
        var frontText = this.textArea.getText(0, coordinate[0]);
        var endText = this.textArea.getText(coordinate[1],
                this.textArea.getText().length() - coordinate[1]);
        return this.setText(frontText + endText).setCaretPosition(coordinate[0]);
    }

    /**
     * 删除选中文本并用指它字符替换。如果没有选中文本，不会发生任何效果，也不会引发异常
     * <p>
     * 算法：
     * 1. 获得选中文本的首尾坐标。
     * 2. 根据此坐标截取文本框中选中文本左边和右边的文本。
     * 3. 将截取到的这两部分文本中间加上指定文本合并并设置在文本框中
     * 4. 将光标设置在指定文本的尾端
     *
     * @since 2021-8-5
     */
    @SneakyThrows
    public TextBox replaceSelectedText(String str) {
        var coordinate = this.getSelectedCoordinate();
        var frontText = this.textArea.getText(0, coordinate[0]);
        var endText = this.textArea.getText(coordinate[1],
                this.textArea.getText().length() - coordinate[1]);
        return this.setText(frontText + str + endText)
                .setCaretPosition(coordinate[0] + str.length());
    }

    /**
     * 删除选中文本或光标所在位置之前最近的一个字符
     *
     * @since 2021-8-5
     */
    public TextBox delete() {
        if (this.getSelectedText() == null) {
            return this.deleteCursorChar();
        } else {
            return this.deleteSelectedText();
        }
    }

    /**
     * 删除选中字符，并插入指它字符
     *
     * @since 2021-8-5
     */
    public TextBox insert(String str) {
        if (this.getSelectedText() == null) {
            return this.addCursorStr(str);
        } else {
            return this.replaceSelectedText(str);
        }
    }

    /**
     * @since 2021-8-6
     */
    public void selectAll() {
        this.requestFocus(); // 焦点转移到文本区
        this.textArea.selectAll();
    }

    /**
     * 当它的光标位置越界时，不会抛出异常，而是将光标定位到最前或最后
     * <p>
     * 注意：光标的范围是 [0, length]。因为光标指向文字的空隙，而空隙的数量比文字多 1
     *
     * @since 2021-8-6
     */
    public TextBox setCaretPosition(int position) {
        this.requestFocus(); // 先把焦点转移到文本区
        this.textArea.setCaretPosition(this.correctCaretPositionNum(position));
        return this;
    }

    /**
     * 因为该方法是重写方法，所以该方法的返回值只能为 void，不能为 this
     *
     * @since 2021-8-6
     */
    @Override
    public void requestFocus() {
        this.textArea.requestFocus();
    }

    /**
     * @since 2021-8-5
     */
    public String getSelectedText() {
        return this.textArea.getSelectedText();
    }

    /**
     * @return 返回选中文本的首尾坐标。
     * 起点坐标为第一个被选中的文本坐标，从 0 开始。
     * 终点坐标为第一个没有被选中的文本坐标
     * 如果没有选中文本，这两个坐标将设置为光标的坐标
     * @since 2021-8-5
     */
    public int[] getSelectedCoordinate() {
        return new int[]{this.textArea.getSelectionStart(),
                this.textArea.getSelectionEnd()};
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

    /**
     * 将滚动条设置在底部
     *
     * @since 2021-8-6
     */
    public TextBox setBarAtTheBottom() {
        this.textArea.setSelectionStart(this.textArea.getText().length());
        return this;
    }

    /**
     * 设置滚动条上升与下降按钮的背景颜色
     *
     * @since 2021-8-6
     */
    @SneakyThrows
    public TextBox setBarColor(Color color) {
        /**
         * 此处使用反射来强制更改字段值实属迫不得已，
         * 因为 swing 中没有提供更改的函数
         *
         * 很遗憾，尝试了很多办法，无法简单地将滚动条的长条设为白色。
         * 这有可能是现在的 JDK 更改了逻辑。一个以前可能有效的办法是：
         * UIManager.put("ScrollBar.thumb", Color.RED);
         */
        var ui = this.scrollPane.getVerticalScrollBar().getUI();

        /**
         * 更改上升按钮的颜色。此处字段名含“decrease”（下降）可能是当年 JDK 中的谬误
         */
        var decreaseButtonF = MetalScrollBarUI.class
                .getDeclaredField("decreaseButton");
        decreaseButtonF.setAccessible(true);
        MetalScrollButton decreaseButton = (MetalScrollButton) decreaseButtonF.get(ui);
        decreaseButton.setBackground(color);

        /**
         * 更改下降按钮的颜色
         */
        var increaseButtonF = MetalScrollBarUI.class
                .getDeclaredField("increaseButton");
        increaseButtonF.setAccessible(true);
        MetalScrollButton increaseButton = (MetalScrollButton) increaseButtonF.get(ui);
        increaseButton.setBackground(color);

        return this;
    }

}
