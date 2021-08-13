package org.wangpai.calculator.view.output;

import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.TextBoxLinker;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Stack;

/**
 * @since 2021-8-1
 */
@Lazy
@Scope("singleton")
@Component("promptMsgBox")
public class PromptMsgBoxLinker extends TextBoxLinker {
    @Resource(name = "calculatorMainFace")
    private MiddleController upperController;

    private PromptMsgBox promptMsgBox;

    /**
     * 因为构造器被执行将优先于依赖注入发生，
     * 所以构造器中不能使用任何需依赖注入的字段
     *
     * Spring 的注入不受访问权限的限制，
     * 因此这里可以使用 protected
     *
     * @since 2021-8-7
     */
    protected PromptMsgBoxLinker() {
        super();
    }

    /**
     * @since 2021-9-26
     */
    @Override
    protected MiddleController getUpperController() {
        return this.upperController;
    }

    /**
     * Bean 的初始化方法
     *
     * @since 2021-8-7
     */
    public void afterPropertiesSet() {
        // 敬请期待
    }

    /**
     * 此方法为包可见
     *
     * @since 2021年9月25日
     */
    static PromptMsgBoxLinker getLinker() {
        return CentralDatabase.getSpringContext()
                .getBean("promptMsgBox", PromptMsgBoxLinker.class);
    }

    /**
     * 此方法为包可见
     *
     * @since 2021年9月25日
     */
    void bindLinker(PromptMsgBox promptMsgBox) {
        this.promptMsgBox = promptMsgBox;
        this.promptMsgBox.setLinker(this);
    }

    /**
     * 此方法为包可见
     * 此方法必须要先于本类的其它方法被调用
     * 使用本方法可以免除使用方法 bindLinker、getLinker
     *
     * @since 2021年9月26日
     */
    static void linking(PromptMsgBox promptMsgBox) {
        PromptMsgBoxLinker.getLinker().bindLinker(promptMsgBox);
    }

    @Override
    public void receive(Url url, Object data) {
        // 当 instanceof 的左边为 null 时，结果也是 false
        if (data instanceof String) {
            this.receive(url, (String) data);
        } else if (data instanceof CalculatorException) {
            var exceptionMsg = ((CalculatorException) data).getExceptionMsg();
            var exceptionData = ((CalculatorException) data).getData();
            var msg = new StringBuilder();
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
    public void receive(Url url, String str) {
        switch (url.getFirstLevelDirectory()) {
            case "append":
                this.promptMsgBox.append(str);
                break;
            case "setText":
                this.promptMsgBox.setText(str);
                break;
            case "cleanAllContent":
                this.promptMsgBox.cleanAllContent();
                break;
        }
    }
}
