package org.wangpai.calculator.view.output;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javafx.application.Platform;
import java.util.Stack;
import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.TextBoxLinker;

/**
 * @since 2021-8-1
 */
@Lazy
@Scope("singleton")
@Component("promptMsgBox")
@Slf4j
public class PromptMsgBoxLinker extends TextBoxLinker {
    @Qualifier("calculatorMainFace")
    @Autowired
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
    public Object receive(Url url, Object data) throws CalculatorException {
        Object response = null;
        // 当 instanceof 的左边为 null 时，结果也是 false
        if (data == null) {
            response = this.receive(url);
        } else if (data instanceof String) {
            response = this.receive(url, (String) data);
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

            response = this.receive(url, msg.toString());
        } else if (data instanceof PromptMsgBoxState) {
            response = this.receive(url, (PromptMsgBoxState) data);
        } else {
            // 敬请期待
        }
        return response;
    }

    /**
     * @since 2021-10-12
     */
    public Object receive(Url url) {
        Object response = null;
        switch (url.getFirstLevelDirectory()) {
            case "cleanAllContent":
                Platform.runLater(() -> {
                    this.promptMsgBox.cleanAllContent();
                });
                break;
            case "getState":
                response = this.promptMsgBox.getState();
                break;

            default:
                log.error("错误：使用了未定义的 Url");
                break;
        }

        return response;
    }

    public Object receive(Url url, String str) {
        Platform.runLater(() -> {
            switch (url.getFirstLevelDirectory()) {
                case "append":
                    this.promptMsgBox.append(str);
                    break;
                case "setText":
                    this.promptMsgBox.setText(str);
                    break;

                default:
                    log.error("错误：使用了未定义的 Url");
                    break;
            }
        });
        return null;
    }

    /**
     * @since 2021-10-12
     */
    public Object receive(Url url, PromptMsgBoxState state) {
        Platform.runLater(() -> {
            switch (url.getFirstLevelDirectory()) {
                case "setState":
                    this.promptMsgBox.setState(state);
                    break;

                default:
                    log.error("错误：使用了未定义的 Url");
                    break;
            }
        });

        return null;
    }
}
