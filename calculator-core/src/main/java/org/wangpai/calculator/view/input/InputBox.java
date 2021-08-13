package org.wangpai.calculator.view.input;

import org.springframework.context.annotation.Lazy;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.view.CalculatorMainPanel;
import org.wangpai.calculator.view.base.TextBox;

import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Font;


/**
 * @since 2021-7-24
 */
@Lazy
@Scope("singleton")
@Component("inputBox")
public final class InputBox extends TextBox implements InitializingBean {
    @Resource(name = "calculatorMainPanel")
    private CalculatorMainPanel parentPanel;

    /**
     * 因为构造器被执行将优先于依赖注入发生，
     * 所以构造器中不能使用任何需依赖注入的字段
     *
     * Spring 的注入不受访问权限的限制，
     * 因此这里可以使用 protected
     *
     * @since 2021-8-7
     */
    protected InputBox() {
        super();
    }

    /**
     * Bean 的初始化方法
     *
     * @since 2021-8-7
     */
    @Override
    public void afterPropertiesSet() {
        super.textArea.setFont(new Font("Dialog", 0, 22));

        super.textArea.getDocument().addDocumentListener(new DocumentListener() {
            TerminalController controller = parentPanel;

            @SneakyThrows
            @Override
            public void insertUpdate(DocumentEvent e) {
                controller.send(new Url("/service/expression"),
                        InputBox.removeUselessChar(textArea.getText()));
            }

            @SneakyThrows
            @Override
            public void removeUpdate(DocumentEvent e) {
                controller.send(new Url("/service/expression"),
                        InputBox.removeUselessChar(textArea.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // 空方法
            }
        });
    }

    /**
     * @since 2021-8-7
     */
    @Override
    public InputBox setText(String msg) {
        super.setText(msg);
        this.requestFocus(); // 焦点转移到文本区
        return this;
    }

    /**
     * @since 2021-8-7
     */
    @Override
    public InputBox append(String msg) {
        super.append(msg);
        this.requestFocus(); // 焦点转移到文本区
        return this;
    }
}
