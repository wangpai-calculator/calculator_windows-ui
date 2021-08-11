package org.wangpai.calculator.view.input;

import org.wangpai.calculator.view.CalculatorMainPanel;

import javax.swing.JFrame;
import java.awt.EventQueue;

/**
 * @since 2021-7-24
 */
class InputBox_Test {
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            var frame = new InputBox_Test.TestFrame();
            frame.setTitle("TestFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

        System.out.println(InputBox.removeUselessChar("234234.623*6345-234/1234+234*(254-45.23542)=\n"));

    }

    static class TestFrame extends JFrame {
        public TestFrame() {
//            super();
//            CalculatorMainPanel mockPanel= (CalculatorMainPanel) CalculatorMainPanel.create();
//            JPanel buttonGroup = (JPanel) InputBox.create(mockPanel);
//            super.setLayout(new BorderLayout());
//            super.add(buttonGroup);
//            pack();
        }
    }
}
