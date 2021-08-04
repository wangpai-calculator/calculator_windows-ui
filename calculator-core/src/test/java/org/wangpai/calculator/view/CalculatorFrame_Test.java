package org.wangpai.calculator.view;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 * @since 2021-8-1
 */
class CalculatorFrame_Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = CalculatorFrame.create();
            frame.setTitle("TestFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}