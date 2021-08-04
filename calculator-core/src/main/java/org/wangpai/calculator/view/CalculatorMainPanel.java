package org.wangpai.calculator.view;

import org.wangpai.calculator.controller.*;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.view.control.ButtonGroup;
import org.wangpai.calculator.view.control.Gbc;
import org.wangpai.calculator.view.input.InputBox;
import org.wangpai.calculator.view.output.PromptMsgBox;
import org.wangpai.calculator.view.output.ResultBox;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * @since 2021-8-1
 */
public final class CalculatorMainPanel extends JPanel implements TerminalController, MiddleController {
    private JPanel buttonGroup;
    private InputBox inputPanel;
    private ResultBox resultPanel;
    private PromptMsgBox promptPanel;
    private GridBagLayout gb;
    private MiddleController upperController;

    protected CalculatorMainPanel() {
        super();
    }

    public static CalculatorMainPanel create() {
        var mainPanel = new CalculatorMainPanel();
        mainPanel.gb = new GridBagLayout();
        mainPanel.setLayout(mainPanel.gb);

        mainPanel.setBackground(Color.WHITE);

        mainPanel.inputPanel = InputBox.create(mainPanel);
        mainPanel.gb.setConstraints(mainPanel.inputPanel,
                new Gbc(0, 0, 4, 2)
                        .setWeight(400, 100)
                        .setInsets(5) // 控制组件之间的间隙
                        .setAnchor(Gbc.NORTHWEST)
                        .setFill(Gbc.BOTH)); // 此值如果不设置，则组件会萎缩
        mainPanel.add(mainPanel.inputPanel);

        mainPanel.buttonGroup = ButtonGroup.create(mainPanel);
        mainPanel.gb.setConstraints(mainPanel.buttonGroup,
                new Gbc(0, 10, 4, 2)
                        .setWeight(100, 100)
                        .setInsets(5) // 控制组件之间的间隙
                        .setAnchor(Gbc.SOUTHWEST)
                        .setFill(Gbc.BOTH)); // 此值如果不设置，则组件会萎缩
        mainPanel.add(mainPanel.buttonGroup);

        mainPanel.resultPanel = ResultBox.create(mainPanel);
        mainPanel.gb.setConstraints(mainPanel.resultPanel,
                new Gbc(4, 0, 15, 18)
                        .setWeight(800, 800)
                        .setIpad(200,0) // 额外增加控件的组内大小
                        .setInsets(5) // 控制组件之间的间隙
                        .setAnchor(Gbc.CENTER)
                        .setFill(Gbc.BOTH)); // 此值如果不设置，则组件会萎缩
        mainPanel.add(mainPanel.resultPanel);

        mainPanel.promptPanel = PromptMsgBox.create(mainPanel);
        mainPanel.gb.setConstraints(mainPanel.promptPanel,
                new Gbc(20, 0, 10, 18)
                        .setWeight(800, 200)
                        .setIpad(50,0) // 额外增加控件的组内大小
                        .setInsets(5) // 控制组件之间的间隙
                        .setAnchor(Gbc.EAST)
                        .setFill(Gbc.BOTH)); // 此值如果不设置，则组件会萎缩
        mainPanel.add(mainPanel.promptPanel);

        return mainPanel;
    }

    private void send2InputBox(Url url, String str){
        /**
         * 介于可读性及因反射的危险性带来的复杂性，此处不要使用反射。
         * 因反射的危险性带来的复杂性指的是：
         * 反射调用方法时，对应方法可能不存在，因此还需要编写相应事先检验函数，
         * 这实际上并不能减少代码量
         */
        switch (url.getFirstLevelDirectory()) {
            case "addcursorstr":
                this.inputPanel.addCursorStr(str);
                break;
            case "leftshift":
                this.inputPanel.leftShift();
                break;
            case "rightshift":
                this.inputPanel.rightShift();
                break;
            case "deletecursorchar":
                this.inputPanel.deleteCursorChar();
                break;
            case "settext":
                this.inputPanel.setText(str);
                break;
            case "cleanallcontent":
                this.inputPanel.cleanAllContent(str);
                break;
        }
    }

    private void send2PromptMsgBox(Url url, String str){
        /**
         * 介于可读性及因反射的危险性带来的复杂性，此处不要使用反射。
         * 因反射的危险性带来的复杂性指的是：
         * 反射调用方法时，对应方法可能不存在，因此还需要编写相应事先检验函数，
         * 这实际上并不能减少代码量
         */
        switch (url.getFirstLevelDirectory()) {
            case "append":
                this.promptPanel.append(str);
                break;
            case "settext":
                this.promptPanel.setText(str);
                break;
            case "cleanallcontent":
                this.promptPanel.cleanAllContent(str);
                break;
        }
    }

    private void send2ResultBox(Url url, String str){
        /**
         * 介于可读性及因反射的危险性带来的复杂性，此处不要使用反射。
         * 因反射的危险性带来的复杂性指的是：
         * 反射调用方法时，对应方法可能不存在，因此还需要编写相应事先检验函数，
         * 这实际上并不能减少代码量
         */
        switch (url.getFirstLevelDirectory()) {
            case "append":
                this.resultPanel.append(str);
                break;
            case "settext":
                this.resultPanel.setText(str);
                break;
            case "cleanallcontent":
                this.resultPanel.cleanAllContent(str);
                break;
        }
    }

    private void receive(Url url, String str) {
        switch (url.getFirstLevelDirectory()) {
            case "inputbox":
                this.send2InputBox(url.generateLowerUrl(), str);
                break;
            case "promptmsgbox":
                this.send2PromptMsgBox(url.generateLowerUrl(), str);
                break;
            case "resultbox":
                this.send2ResultBox(url.generateLowerUrl(), str);
                break;
        }
    }

    /**
     * 所有消息发送事件的起点
     */
    @Override
    public void send(Url url, Object data) {
        if (this.upperController == null) {
            this.upperController = new Dispatcher();
        }

        switch (url.getFirstLevelDirectory()) {
            case "view":
                this.receive(url.generateLowerUrl(), data);
                break;
            case "service":
                this.sendUp(url, data, null); // 此处的 sendUp的第三个参数不必要
        }
    }

    /**
     * 所有消息发送事件的终点
     */
    @Override
    public void receive(Url url, Object data) {
        if (data instanceof String) {
            this.receive(url, (String) data);
        }
        // 当 instanceof 的左边为 null 时，结果也是 false
        if (data instanceof CalculatorException) {
            var exceptionMsg = ((CalculatorException) data).getExceptionMsg();
            var exceptionData = ((CalculatorException) data).getData();
            var msg = new StringBuffer();
            if (exceptionMsg instanceof String) {
                msg.append(exceptionMsg)
                        .append(System.lineSeparator());
            }
            if (exceptionData instanceof String) {
                msg.append(System.lineSeparator())
                        .append("下面是自动纠正的结果：（供复制）")
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(((String) exceptionData).trim())
                        .append(System.lineSeparator());
            } else if (exceptionData instanceof Stack) {
                msg.append(System.lineSeparator())
                        .append("下面是自动纠正的结果：（供复制）")
                        .append(System.lineSeparator())
                        .append(System.lineSeparator());
                for (var symbol : (Stack<Symbol>) exceptionData) {
                    msg.append(symbol);
                }
                msg.append(System.lineSeparator());
            }

            this.receive(url, msg.toString());
        } else {
            // 敬请期待
        }
    }

    @Override
    public void sendUp(Url url, Object data, MiddleController lowerController) {
        upperController.sendUp(url, data, this);
    }

    @Override
    public void sendDown(Url url, Object data, MiddleController upperController) {
        receive(url, data); // 注意：此处不使用 url.generateLowerUrl()
    }
}
