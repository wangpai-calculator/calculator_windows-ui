package org.wangpai.calculator.view.control;

import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.TerminalLinker;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @since 2021-8-1
 */
@Lazy
@Scope("singleton")
@Component("buttonGroup")
public final class ButtonGroupLinker extends TerminalLinker {
    @Qualifier("calculatorMainFace")
    @Autowired
    private MiddleController upperController;

    private ButtonGroup buttonGroup;

    /**
     * 因为构造器被执行将优先于依赖注入发生，
     * 所以构造器中不能使用任何需依赖注入的字段
     *
     * Spring 的注入不受访问权限的限制，
     * 因此这里可以使用 protected
     *
     * @since 2021-8-7
     */
    protected ButtonGroupLinker() {
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
     * @lastModified 2021-9-26
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
    static ButtonGroupLinker getLinker() {
        return CentralDatabase.getSpringContext()
                .getBean("buttonGroup", ButtonGroupLinker.class);
    }

    /**
     * 此方法为包可见。此方法必须要先于本类的其它方法被调用
     *
     * @since 2021年9月25日
     */
    void bindLinker(ButtonGroup buttonGroup) {
        this.buttonGroup = buttonGroup;
        this.buttonGroup.setLinker(this);
    }

    /**
     * 此方法为包可见
     * 此方法必须要先于本类的其它方法被调用
     * 使用本方法可以免除使用方法 bindLinker、getLinker
     *
     * @since 2021年9月26日
     */
    static void linking(ButtonGroup buttonGroup) {
        ButtonGroupLinker.getLinker().bindLinker(buttonGroup);
    }

    @Override
    public void receive(Url url, Object data) {
        // 此方法无需实现
    }

    @Override
    public void receive(Url url, String str) {
        // 此方法无需实现
    }
}
