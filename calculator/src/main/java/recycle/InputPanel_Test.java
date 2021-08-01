package recycle;

import javax.swing.*;

import java.awt.*;

class InputPanel_Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = new TestFrame();
            frame.setTitle("TestFrame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class TestFrame extends JFrame {
    public static final int TEXTAREA_ROWS = 8;
    public static final int TEXTAREA_COLUMNS = 20;

    public TestFrame() {
        super();
//        var inputPanel = new InputPanel(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
//        super.add(inputPanel);
        pack();
    }

}

