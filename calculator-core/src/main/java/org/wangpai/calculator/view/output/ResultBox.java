package org.wangpai.calculator.view.output;

import org.springframework.context.annotation.Lazy;
import org.wangpai.calculator.view.CalculatorMainPanel;
import org.wangpai.calculator.view.base.TextBox;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

import java.awt.Font;

/**
 * @since 2021-7-24
 */
@Lazy
@Scope("singleton")
@Component("resultBox")
public final class ResultBox extends TextBox implements InitializingBean {
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
    protected ResultBox() {
        super();
    }

    /**
     * Bean 的初始化方法
     *
     * @since 2021-8-7
     */
    @Override
    public void afterPropertiesSet() {
        super.textArea.setFont(new Font("Dialog", 0, 18));
        super.textArea.setEditable(false);
    }

    /**
     * @since 2021-8-7
     */
    @Override
    public ResultBox setText(String msg) {
        super.setText(msg);
        this.setBarAtTheBottom(); // 将滚动条拨到最下
        return this;
    }

    /**
     * @since 2021-8-7
     */
    @Override
    public ResultBox append(String msg) {
        super.append(msg);
        this.setBarAtTheBottom(); // 将滚动条拨到最下
        return this;
    }
}
