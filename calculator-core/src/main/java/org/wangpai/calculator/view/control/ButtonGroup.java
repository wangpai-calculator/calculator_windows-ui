package org.wangpai.calculator.view.control;

import lombok.SneakyThrows;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.view.CalculatorMainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 2021-8-1
 */
public final class ButtonGroup extends JPanel {
    private CalculatorMainPanel parentPanel;

    /**
     * 不要将这些字段在类对象被创建时就进行含 new 的初始化，
     * 因为这样可能导致生成子类不需要的数据
     */
    private GridBagLayout gb;
    private JButton[][] buttons;

    // 功能键
    private List<JButton> functionButtons;

    // 有实际意义的按钮，非功能键
    private List<JButton> practicalButton;

    public ButtonGroup() {
        super();
    }

    public static JPanel create(CalculatorMainPanel mainPanel) {
        /**
         * 规定：第一排按钮为功能键
         */
        final String[][] labels = new String[][]{
                {"❮", "❯", "☑", "☒"},
                {Symbol.SEVEN.toString(),
                        Symbol.EIGHT.toString(),
                        Symbol.NINE.toString(),
                        Symbol.DIVIDE.toString()},
                {Symbol.FOUR.toString(),
                        Symbol.FIVE.toString(),
                        Symbol.SIX.toString(),
                        Symbol.MULTIPLY.toString()},
                {Symbol.ONE.toString(),
                        Symbol.TWO.toString(),
                        Symbol.THREE.toString(),
                        Symbol.SUBTRACT.toString()},
                {Symbol.ZERO.toString(),
                        Symbol.DOT.toString(),
                        "null",
                        Symbol.ADD.toString()},
                {Symbol.LEFT_BRACKET.toString(),
                        Symbol.RIGHT_BRACKET.toString(),
                        "null",
                        Symbol.EQUAL.toString()}};

        final int rowLength = labels.length;
        final int columnLength = labels[0].length;

        var buttonGroup = new ButtonGroup();
        buttonGroup.gb = new GridBagLayout();
        buttonGroup.setLayout(buttonGroup.gb);
        buttonGroup.parentPanel = mainPanel;
        buttonGroup.buttons = new JButton[rowLength][columnLength];
        buttonGroup.functionButtons = new ArrayList<>();
        buttonGroup.practicalButton = new ArrayList<>();

        for (int row = 0; row < rowLength; ++row) {
            for (int column = 0; column < columnLength; ++column) {
                var label = labels[row][column];
                var button = new JButton(label);
                buttonGroup.buttons[row][column] = button;

                if (row == 0) {
                    buttonGroup.functionButtons.add(button);
                }

                var symbolEnum = Symbol.getEnum(label);
                if (symbolEnum != null) {
                    buttonGroup.practicalButton.add(button);
                }

                button.setFocusPainted(false); // 取消焦点按钮的虚线边框
                button.setBackground(Color.WHITE);
                button.setFont(new Font("Dialog", 0, 22));

                buttonGroup.gb.setConstraints(button,
                        new Gbc(column, row)
                                .setWeight(100, 100) // Weight 在此处可以随意取值，但是不能不设置
                                .setFill(Gbc.BOTH)); // 此值如果不设置，则组件会萎缩
                buttonGroup.add(button);
            }
        }

        for (var button : buttonGroup.functionButtons) {
            button.addActionListener(new ActionListener() {
                @SneakyThrows // 此处不能简写为 lambda 表达式
                @Override
                public void actionPerformed(ActionEvent event) {
                    String str = event.getActionCommand();
                    switch (str) {
                        case "❮":
                            buttonGroup.parentPanel.send(new Url("/view/inputbox/leftshift"), str);
                            break;
                        case "❯":
                            buttonGroup.parentPanel.send(new Url("/view/inputbox/rightshift"), str);
                            break;
                        case "☑":
                            buttonGroup.parentPanel.send(new Url("/view/inputbox/selectall"), str);
                            break;
                        case "☒":
                            buttonGroup.parentPanel.send(new Url("/view/inputbox/delete"), str);
                            break;
                    }
                    System.out.println(event.getActionCommand());
                }
            });
        } // for-each

        for (var button : buttonGroup.practicalButton) {
            button.addActionListener(new ActionListener() {
                @SneakyThrows // 此处不能简写为 lambda 表达式
                @Override
                public void actionPerformed(ActionEvent event) {
                    String str = event.getActionCommand();
                    buttonGroup.parentPanel.send(new Url("/view/inputbox/insert"), str);
                    System.out.println(event.getActionCommand());
                }
            });
        }// for-each

        return buttonGroup;
    }
}
