package org.wangpai.calculator.view;

import javax.swing.*;
import java.awt.*;

/**
 * @since 2021-8-1
 */
class CalculatorMainPanel_Test {
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            var frame = new CalculatorMainPanel_Test.TestFrame();
            frame.setTitle("TestFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    static class TestFrame extends JFrame {
        public TestFrame() {
//            super();
//
//            JPanel buttonGroup = (JPanel) CalculatorMainPanel.create();
//            super.setLayout(new BorderLayout());
//            super.add(buttonGroup);
//
//            this.setPreferredSize(new Dimension(1000, 600));
//
//            pack();
        }
    }
}
