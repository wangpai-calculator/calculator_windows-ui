package org.wangpai.calculator.view;

import javax.swing.*;
import java.awt.*;

/**
 * @since 2021-8-1
 */
public final class CalculatorFrame extends JFrame {
    private JComponent mainPanel;

    protected CalculatorFrame() {
        super();
    }

    public static CalculatorFrame create() {
        var frame = new CalculatorFrame();
        frame.mainPanel = CalculatorMainPanel.create();
        frame.setLayout(new BorderLayout());
        frame.add(frame.mainPanel);
        frame.setPreferredSize(new Dimension(1500, 800));
        frame.pack();

        return frame;
    }

}
