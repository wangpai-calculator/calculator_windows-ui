package org.wangpai.calculator.view.input;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.TerminalLinker;

/**
 * @since 2021-7-24
 */
@Lazy
@Scope("singleton")
@Component("inputBox")
@Slf4j
public final class InputBoxLinker extends TerminalLinker {
    @Qualifier("calculatorMainFace")
    @Autowired
    private MiddleController upperController;

    private InputBox inputBox;

    /**
     * 因为构造器被执行将优先于依赖注入发生，
     * 所以构造器中不能使用任何需依赖注入的字段
     *
     * Spring 的注入不受访问权限的限制，
     * 因此这里可以使用 protected
     *
     * @since 2021-8-7
     */
    protected InputBoxLinker() {
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
    @Override
    public void afterPropertiesSet() {
        // 此方法无需实现
    }

    /**
     * 此方法为包可见
     *
     * @since 2021年9月25日
     */
    static InputBoxLinker getLinker() {
        return CentralDatabase.getSpringContext()
                .getBean("inputBox", InputBoxLinker.class);
    }

    /**
     * 此方法为包可见
     *
     * @since 2021年9月25日
     */
    void bindLinker(InputBox inputBox) {
        this.inputBox = inputBox;
        this.inputBox.setLinker(this);
    }

    /**
     * 此方法为包可见
     * 此方法必须要先于本类的其它方法被调用
     * 使用本方法可以免除使用方法 bindLinker、getLinker
     *
     * @since 2021年9月26日
     */
    static void linking(InputBox inputBox) {
        InputBoxLinker.getLinker().bindLinker(inputBox);
    }

    @Override
    public Object receive(Url url, Object data) throws CalculatorException {
        Object response = null;
        if (data instanceof String) {
            response = this.receive(url, (String) data);
        } else {
            // 待子类酌情实现
        }
        return response;
    }

    public Object receive(Url url, String str) {
        Platform.runLater(() -> {
            switch (url.getFirstLevelDirectory()) {
                case "insert":
                    inputBox.insert(str);
                    break;
                case "leftShift":
                    inputBox.leftShift();
                    break;
                case "rightShift":
                    inputBox.rightShift();
                    break;
                case "delete":
                    inputBox.delete();
                    break;
                case "selectAll":
                    inputBox.selectAll();
                    break;
                case "setText":
                    inputBox.setText(str);
                    break;
                case "cleanAllContent":
                    inputBox.cleanAllContent();
                    break;
                case "undo":
                    inputBox.undo();
                    break;
                case "redo":
                    inputBox.redo();
                    break;
                case "focus":
                    inputBox.requestFocus();
                    break;

                default:
                    log.error("错误：使用了未定义的 Url");
                    break;
            }
        });
        return null;
    }
}
