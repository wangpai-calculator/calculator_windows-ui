package org.wangpai.calculator;

import org.wangpai.calculator.view.CalculatorFrame;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.JFrame;
import java.awt.EventQueue;

/**
 * 程序启动入口
 *
 * @since 2021-8-8
 */
public class CalculatorApplication {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            // 加载包中的所有 bean
            var ctx = new ClassPathXmlApplicationContext("beanScan.xml");
            // 获取一个 CalculatorFrame 对象
            var frame = ctx.getBean("calculatorFrame", CalculatorFrame.class);

            frame.setTitle("TestFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
