package org.wangpai.calculator.view.output;

import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.TextBoxLinker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javafx.application.Platform;

/**
 * @since 2021-7-24
 */
@Lazy
@Scope("singleton")
@Component("resultBox")
public class ResultBoxLinker extends TextBoxLinker {
    @Qualifier("calculatorMainFace")
    @Autowired
    private MiddleController upperController;

    private ResultBox resultBox;

    /**
     * 因为构造器被执行将优先于依赖注入发生，
     * 所以构造器中不能使用任何需依赖注入的字段
     *
     * Spring 的注入不受访问权限的限制，
     * 因此这里可以使用 protected
     *
     * @since 2021-8-7
     */
    protected ResultBoxLinker() {
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
        // 此方法无需实现
    }

    /**
     * 此方法为包可见
     *
     * @since 2021年9月25日
     */
    static ResultBoxLinker getLinker() {
        return CentralDatabase.getSpringContext()
                .getBean("resultBox", ResultBoxLinker.class);
    }

    /**
     * 此方法为包可见
     *
     * @since 2021年9月25日
     */
    void bindLinker(ResultBox resultBox) {
        this.resultBox = resultBox;
        this.resultBox.setLinker(this);
    }

    /**
     * 此方法为包可见
     * 此方法必须要先于本类的其它方法被调用
     * 使用本方法可以免除使用方法 bindLinker、getLinker
     *
     * @since 2021年9月26日
     */
    static void linking(ResultBox resultBox) {
        ResultBoxLinker.getLinker().bindLinker(resultBox);
    }

    @Override
    public void receive(Url url, String str) {
        Platform.runLater(() -> {
            switch (url.getFirstLevelDirectory()) {
                case "append":
                    this.resultBox.append(str);
                    break;
                case "setText":
                    this.resultBox.setText(str);
                    break;
                case "cleanAllContent":
                    this.resultBox.cleanAllContent();
                    break;
            }
        });
    }
}
