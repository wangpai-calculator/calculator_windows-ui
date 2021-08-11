package org.wangpai.calculator.view.control;

import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.view.CalculatorMainPanel;

import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 2021-8-1
 */
@Scope("singleton")
@Component("buttonGroup")
public final class ButtonGroup extends JPanel implements InitializingBean {
    @Resource(name = "calculatorMainPanel")
    private CalculatorMainPanel parentPanel;

    private GridBagLayout gb;
    private JButton[][] buttons;

    // 功能键
    private List<JButton> functionButtons;

    // 有实际意义的按钮，非功能键
    private List<JButton> practicalButton;

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

    /**
     * 因为构造器被执行将优先于依赖注入发生，
     * 所以构造器中不能使用任何需依赖注入的字段
     *
     * Spring 的注入不受访问权限的限制，
     * 因此这里可以使用 protected
     *
     * @since 2021-8-7
     */
    protected ButtonGroup() {
        super();
    }


    /**
     * Bean 的初始化方法
     *
     * @since 2021-8-7
     */
    @Override
    public void afterPropertiesSet() {
        this.gb = new GridBagLayout();
        this.setLayout(this.gb);

        final int rowLength = labels.length;
        final int columnLength = labels[0].length;
        this.buttons = new JButton[rowLength][columnLength];
        this.functionButtons = new ArrayList<>();
        this.practicalButton = new ArrayList<>();

        for (int row = 0; row < rowLength; ++row) {
            for (int column = 0; column < columnLength; ++column) {
                var label = labels[row][column];
                var button = new JButton(label);
                this.buttons[row][column] = button;

                if (row == 0) {
                    this.functionButtons.add(button);
                }

                var symbolEnum = Symbol.getEnum(label);
                if (symbolEnum != null) {
                    this.practicalButton.add(button);
                }

                button.setFocusPainted(false); // 取消焦点按钮的虚线边框
                button.setBackground(Color.WHITE);
                button.setFont(new Font("Dialog", 0, 22));

                this.gb.setConstraints(button,
                        new Gbc(column, row)
                                .setWeight(100, 100) // Weight 在此处可以随意取值，但是不能不设置
                                .setFill(Gbc.BOTH)); // 此值如果不设置，则组件会萎缩
                this.add(button);
            }
        }

        for (var button : this.functionButtons) {
            button.addActionListener(new ActionListener() {
                @SneakyThrows // 此处不能简写为 lambda 表达式
                @Override
                public void actionPerformed(ActionEvent event) {
                    String str = event.getActionCommand();
                    switch (str) {
                        case "❮":
                            parentPanel.send(new Url("/view/inputbox/leftshift"), str);
                            break;
                        case "❯":
                            parentPanel.send(new Url("/view/inputbox/rightshift"), str);
                            break;
                        case "☑":
                            parentPanel.send(new Url("/view/inputbox/selectall"), str);
                            break;
                        case "☒":
                            parentPanel.send(new Url("/view/inputbox/delete"), str);
                            break;
                    }
                }
            });
        } // for-each

        for (var button : this.practicalButton) {
            button.addActionListener(new ActionListener() {
                @SneakyThrows // 此处不能简写为 lambda 表达式
                @Override
                public void actionPerformed(ActionEvent event) {
                    String str = event.getActionCommand();
                    parentPanel.send(new Url("/view/inputbox/insert"), str);
                }
            });
        }// for-each
    }
}
