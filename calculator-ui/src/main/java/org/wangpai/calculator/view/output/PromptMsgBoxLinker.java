package org.wangpai.calculator.view.output;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
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
    @Override
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
     * @since 2021-9-25
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
     * @since 2021-9-16
     */
    static void linking(PromptMsgBox promptMsgBox) {
        PromptMsgBoxLinker.getLinker().bindLinker(promptMsgBox);
    }

    @Override
    public Object receive(Url url, Object data) {
        Object response = null;
        // 当 instanceof 的左边为 null 时，结果也是 false
        if (data == null) {
            response = this.receive(url);
        } else if (data instanceof String) {
            response = this.receive(url, (String) data);
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
}
