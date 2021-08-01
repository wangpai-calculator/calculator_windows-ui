package org.wangpai.calculator.view.output;

import org.wangpai.calculator.view.CalculatorMainPanel;
import org.wangpai.calculator.view.control.Gbc;

import javax.swing.*;
import java.awt.*;

public final class ResultBox extends JPanel {
    public static final int TEXT_ROWS = 10;
    public static final int TEXT_COLUMNS = 40;

    private CalculatorMainPanel parentPanel;

    /**
     * 不要将这些字段在类对象被创建时就进行含 new 的初始化，
     * 因为这样可能导致生成子类不需要的数据
     */
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private GridBagLayout gb;

    protected ResultBox() {
        super();
    }

    public static ResultBox create(CalculatorMainPanel mainPanel) {
        var resultPanel = new ResultBox();

        resultPanel.parentPanel = mainPanel;
        resultPanel.textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        resultPanel.textArea.setFont(new Font("Dialog", 0, 18));
        resultPanel.textArea.setEnabled(true);
        resultPanel.textArea.setLineWrap(true);
        resultPanel.textArea.setEditable(false);

        int insets = 10;
        // 设置内边距：文字与文本边界之间的间距
        resultPanel.textArea.setMargin(new Insets(insets, insets, insets, insets));

        resultPanel.scrollPane = new JScrollPane(resultPanel.textArea);

        resultPanel.gb = new GridBagLayout();
        resultPanel.gb.setConstraints(resultPanel.scrollPane,
                new Gbc(0, 0)
                        .setWeight(400, 800) // Weight 在此处可以随意取值，但是不能不设置
                        .setFill(Gbc.BOTH));

        resultPanel.setLayout(resultPanel.gb);
        resultPanel.add(resultPanel.scrollPane);

        return resultPanel;
    }

    /**
     * 将滚动条设置在底部
     */
    public ResultBox setBarAtTheBottom(){
        this.textArea.setSelectionStart(this.textArea.getText().length());
        return this;
    }

    public String getText() {
        return this.textArea.getText();
    }

    public ResultBox setText(String msg) {
        this.textArea.setText(msg);
        this.setBarAtTheBottom();
        return this;
    }

    public ResultBox append(String msg) {
        this.textArea.append(msg);
        this.setBarAtTheBottom();
        return this;
    }

    public ResultBox cleanAllContent(String msg) {
        return this.setText(null);
    }

}
