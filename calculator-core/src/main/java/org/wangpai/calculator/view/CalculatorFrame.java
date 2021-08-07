package org.wangpai.calculator.view;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

import javax.swing.*;
import java.awt.*;

/**
 * 使用单例模式就是不用担心循环注入的问题
 *
 * @since 2021-8-1
 */
@Scope("singleton")
@Component("calculatorFrame")
public final class CalculatorFrame extends JFrame implements InitializingBean {
    @Resource(name = "calculatorMainPanel")
    private JComponent mainPanel;

    /**
     * 因为构造器被执行将优先于依赖注入发生，
     * 所以构造器中不能使用任何需依赖注入的字段
     *
     * Spring 的注入不受访问权限的限制，
     * 因此这里可以使用 protected
     *
     * @since 2021-8-7
     */
    protected CalculatorFrame() {
        super();
    }

    /**
     * Bean 的初始化方法
     *
     * @since 2021-8-7
     */
    @Override
    public void afterPropertiesSet() {
        this.setLayout(new BorderLayout());
        this.add(this.mainPanel);
        this.setPreferredSize(new Dimension(1500, 800));
        this.pack();
    }
}
