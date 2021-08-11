package org.wangpai.calculator.view.control;


import javax.swing.JFrame;
import java.awt.EventQueue;

/**
 * @since 2021-8-1
 */
class ButtonGroup_Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = new TestFrame();
            frame.setTitle("TestFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    static class TestFrame extends JFrame {
        public TestFrame() {
            super();
//
//            ButtonGroup buttonGroup = new ButtonGroup();
//            buttonGroup.afterPropertiesSet();

//            CalculatorMainPanel mockPanel = new CalculatorMainPanel();
//            JPanel buttonGroup = (JPanel) ButtonGroup.create(mockPanel);

//            super.setLayout(new BorderLayout());
//            super.add(buttonGroup);
//            pack();
        }

    }
}
