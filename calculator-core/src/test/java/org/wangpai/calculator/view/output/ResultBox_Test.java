package org.wangpai.calculator.view.output;


import org.wangpai.calculator.view.CalculatorMainPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @since 2021-7-24
 */
class ResultBox_Test {
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            var frame = new ResultBox_Test.TestFrame();
            frame.setTitle("TestFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    static class TestFrame extends JFrame {
        public TestFrame() {
            super();
//            CalculatorMainPanel mockPanel= (CalculatorMainPanel) CalculatorMainPanel.create();
//            ResultBox panel = ResultBox.create(mockPanel);
//            super.setLayout(new BorderLayout());
//            super.add(panel);
//            pack();
//
//            panel.setText("hello, my world.");

        }

    }
}
