package org.wangpai.calculator.view.output;

import org.wangpai.calculator.view.CalculatorMainPanel;
import org.wangpai.calculator.view.control.Gbc;

import javax.swing.*;
import java.awt.*;

public final class PromptMsgBox extends JPanel {
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

    protected PromptMsgBox() {
        super();
    }

    public static PromptMsgBox create(CalculatorMainPanel mainPanel) {
        var promptPanel = new PromptMsgBox();

        promptPanel.parentPanel = mainPanel;
        promptPanel.textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        promptPanel.textArea.setFont(new Font("Dialog", 0, 18));
        promptPanel.textArea.setEnabled(true);
        promptPanel.textArea.setLineWrap(true);
        promptPanel.textArea.setEditable(false);
        int insets = 10;
        // 设置内边距：文字与文本边界之间的间距
        promptPanel.textArea.setMargin(new Insets(insets, insets, insets, insets));
        promptPanel.scrollPane = new JScrollPane(promptPanel.textArea);

        promptPanel.gb = new GridBagLayout();
        promptPanel.gb.setConstraints(promptPanel.scrollPane,
                new Gbc(0, 0)
                        .setWeight(400, 800) // Weight 在此处可以随意取值，但是不能不设置
                        .setFill(Gbc.BOTH));

        promptPanel.setLayout(promptPanel.gb);
        promptPanel.add(promptPanel.scrollPane);

        return promptPanel;
    }

    public String getText() {
        return this.textArea.getText();
    }

    public PromptMsgBox setText(String msg) {
        this.textArea.setText(msg);
        this.setBarAtTheBottom();
        return this;
    }

    public PromptMsgBox append(String msg) {
        this.textArea.append(msg);
        this.setBarAtTheBottom();
        return this;
    }

    /**
     * 将滚动条设置在底部
     */
    public PromptMsgBox setBarAtTheBottom(){
        this.textArea.setSelectionStart(this.textArea.getText().length());
        return this;
    }

    public PromptMsgBox cleanAllContent(String msg) {
        return this.setText(null);
    }
}
