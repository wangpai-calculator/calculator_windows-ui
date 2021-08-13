package org.wangpai.calculator.view.input;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.TerminalLinker;


import javax.annotation.Resource;


/**
 * @since 2021-7-24
 */
@Lazy
@Scope("singleton")
@Component("inputBox")
public final class InputBoxLinker extends TerminalLinker {
    @Resource(name = "calculatorMainFace")
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
    public void receive(Url url, String str) {
        switch (url.getFirstLevelDirectory()) {
            case "insert":
                this.inputBox.insert(str);
                break;
            case "leftShift":
                this.inputBox.leftShift();
                break;
            case "rightShift":
                this.inputBox.rightShift();
                break;
            case "delete":
                this.inputBox.delete();
                break;
            case "selectAll":
                this.inputBox.selectAll();
                break;
            case "setText":
                this.inputBox.setText(str);
                break;
            case "cleanAllContent":
                this.inputBox.cleanAllContent();
                break;
        }
    }
}
