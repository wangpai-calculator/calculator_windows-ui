package org.wangpai.calculator;

import org.wangpai.calculator.view.CalculatorFrame;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.IOException;

/**
 * 程序启动入口
 *
 * @since 2021-8-8
 */
public class CalculatorApplication {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            // Spring 的此路径在 IntelliJ IDEA 中是以资源目录 resources 为基准的
            var ctx = new ClassPathXmlApplicationContext("resources/spring/beanScan.xml");
            // 获取 CalculatorFrame 对象
            var frame = ctx.getBean("calculatorFrame", CalculatorFrame.class);

            /**
             * 注意：不要使用"new ImageIcon(String path)"来获取图片。
             * 因为它的读取路径是以 System.getProperty("user.dir") 为基路径，
             * 而这个路径是本工程的绝对路径，还不是本模块的绝对路径！
             * 此外，这个路径也会随模块打成 JAR 包而改变
             *
             * 方法 getResourceAsStream 的路径是以资源目录 resources 为基准的
             */
            var imageStream = frame.getClass().getClassLoader()
                    .getResourceAsStream("resources/image/calculatorImage.png");
            Image image = null;
            try {
                image = ImageIO.read(imageStream);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            frame.setIconImage(image);

            frame.setTitle("wangpai-calculator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
