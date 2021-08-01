//package recycle;
//
//import org.wangpai.calculator.model.symbol.enumeration.Symbol;
//import org.wangpai.calculator.view.control.Gbc;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//@Deprecated
//public class ButtonGroup_dup extends JPanel {
////    private static final int BUTTON_WIDTH = 100;
////    private static final int BUTTON_HEIGHT = 100;
//
//
//
//    public ButtonGroup_dup() {
//        super();
//    }
//
//    public static final int TEXT_ROWS = 10;
//    public static final int TEXT_COLUMNS = 40;
//
//    public static JPanel create() {
//        GridBagLayout gb = new GridBagLayout();
//        var buttonGroup = new ButtonGroup_dup();
//        buttonGroup.setLayout(gb);
//
////        var buttons = new DigitalButtons();
////        buttons.setLayout(new GridLayout(3, 3));
////        buttons.setPreferredSize(new Dimension(150, 150));
//
//        var inputBox = buttonGroup.createInputBox();
//        gb.setConstraints(inputBox,
//                new Gbc(0, 0, 4, 2)
//                        .setFill(Gbc.BOTH)
//                        .setWeight(400, 200)
//                        .setAnchor(Gbc.NORTHWEST)
//                        .setInsets(10));
//        buttonGroup.add(inputBox);
//
//        var buttons=buttonGroup.createButtons();
//        gb.setConstraints(buttons,
//                new Gbc(0, 10, 4, 2)
//                        .setWeight(100,100)
//                        .setAnchor(Gbc.SOUTHWEST)
//                        .setFill(Gbc.BOTH)
//                        .setInsets(10));
//        buttonGroup.add(buttons);
//
//
//        var outputBox=buttonGroup.createOutputBox();
//        gb.setConstraints(outputBox,
//                new Gbc(4, 0, 4, 18)
//                        .setFill(Gbc.BOTH)
//                        .setWeight(400, 200)
//                        .setAnchor(Gbc.NORTHWEST)
//                        .setInsets(10));
//        buttonGroup.add(outputBox);
//
//
//
//        return buttonGroup;
//
////        return null;
//    }
//
//    public JComponent createInputBox(){
//        var inputText = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
//        inputText.setEnabled(true);
//        inputText.setLineWrap(true);
//        inputText.setBorder(BorderFactory.createEtchedBorder());
//
//        var scrollPane = new JScrollPane(inputText);
////        inputText.setLineWrap(true);
//
//        GridBagLayout gb = new GridBagLayout();
//
//        gb.setConstraints(scrollPane,
//                new Gbc(0, 0, 4, 2)
//                        .setFill(Gbc.BOTH)
//                        .setWeight(400, 200));
//
//        var inputBox=new JPanel();
//        inputBox.setLayout(gb);
//        inputBox.add(scrollPane);
//
//        return inputBox;
//    }
//
////    private void addInputText() {
////        var inputText = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
////        inputText.setEnabled(true);
////        inputText.setLineWrap(true);
////        inputText.setBorder(BorderFactory.createEtchedBorder());
////        var gbcgbcInputText = new GridBagConstraints();
////        gbcgbcInputText.weightx = 1;
////        gbcgbcInputText.weighty = 1;
////        gbcgbcInputText.gridwidth = GridBagConstraints.REMAINDER;
////        gbcgbcInputText.fill = GridBagConstraints.BOTH;
////        gb.setConstraints(inputText, gbcgbcInputText);
////        super.add(inputText);
////
////    }
//
//    private JComponent createButtons() {
//        final String[][] labels = new String[][]{
//                {"C0", "C1", "C2", "C3"},
//                {Symbol.SEVEN.getSymbol(),
//                        Symbol.EIGHT.getSymbol(),
//                        Symbol.NINE.getSymbol(),
//                        Symbol.DIVIDE.getSymbol()},
//                {Symbol.FOUR.getSymbol(),
//                        Symbol.FIVE.getSymbol(),
//                        Symbol.SIX.getSymbol(),
//                        Symbol.MULTIPLY.getSymbol()},
//                {Symbol.ONE.getSymbol(),
//                        Symbol.TWO.getSymbol(),
//                        Symbol.THREE.getSymbol(),
//                        Symbol.SUBTRACT.getSymbol()},
//                {Symbol.ZERO.getSymbol(),
//                        Symbol.DOT.getSymbol(),
//                        "null",
//                        Symbol.ADD.getSymbol()},
//                {Symbol.LEFT_BRACKET.getSymbol(),
//                        Symbol.RIGHT_BRACKET.getSymbol(),
//                        "null",
//                        Symbol.EQUAL.getSymbol()}};
//
//        final int rowLength=labels.length;
//        final int columnLength = labels[0].length;
//
//        var panel=new JPanel();
//        GridBagLayout gb = new GridBagLayout();
//
//        for (int row = 0; row < rowLength; ++row) {
//            for (int column = 0; column < columnLength; ++column) {
//                System.out.println(labels[row][column]);
//                addButton(labels[row][column], new MockAction(),gb, new Gbc(column, row).setWeight(100, 100)
//                        .setFill(Gbc.BOTH), panel);
//            }
//        }
//
//        panel.setLayout(gb);
//
//        return panel;
//
//    }
//
//    private JComponent createOutputBox(){
//        var outputText = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
//        outputText.setEnabled(true);
//        outputText.setLineWrap(true);
//        outputText.setBorder(BorderFactory.createEtchedBorder());
//
//        var scrollPane = new JScrollPane(outputText);
//        outputText.setLineWrap(true);
//
//        GridBagLayout gb = new GridBagLayout();
//        gb.setConstraints(scrollPane,
//                new Gbc(0, 0, 4, 8+10)
//                        .setFill(Gbc.BOTH)
//                        .setWeight(400, 800));
//
//        var outputBox=new JPanel();
//        outputBox.setLayout(gb);
//        outputBox.add(scrollPane);
//
//
//        return outputBox;
//    }
//
//
//
//    private void addButton(String label, ActionListener listener, GridBagLayout gb,GridBagConstraints gbc,JPanel panel) {
//        JButton button = new JButton(label);
//        button.addActionListener(listener);
//        gb.setConstraints(button, gbc);
//        panel.add(button);
//    }
//
//    static class MockAction implements ActionListener
//    {
//        public void actionPerformed(ActionEvent event)
//        {
//            System.out.println(event.getActionCommand());
//        }
//    }
//}
