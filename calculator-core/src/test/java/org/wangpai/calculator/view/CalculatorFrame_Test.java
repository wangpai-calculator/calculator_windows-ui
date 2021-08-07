package org.wangpai.calculator.view;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.Arrays;

/**
 * @since 2021-8-1
 */
class CalculatorFrame_Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            // 加载包中的所有 bean
            var ctx = new ClassPathXmlApplicationContext("beanScan.xml");
            // 获取一个 CalculatorFrame 对象
            var frame = ctx.getBean("calculatorFrame", CalculatorFrame.class);

            frame.setTitle("TestFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

		System.out.println("已加载的 Bean：" +
			Arrays.toString(ctx.getBeanDefinitionNames()));
        });
    }
}
