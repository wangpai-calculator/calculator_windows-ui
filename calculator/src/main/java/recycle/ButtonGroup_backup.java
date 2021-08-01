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
//public class ButtonGroup_backup extends JPanel {
//    private static final int BUTTON_WIDTH = 100;
//    private static final int BUTTON_HEIGHT = 100;
//
//    private final GridBagLayout gb = new GridBagLayout();
//
//
//    public ButtonGroup_backup() {
//        super();
////        setLayout(new BorderLayout());
//
////        super.setLayout(new GridLayout(5, 4));
//
////        var symbols = UnderlyingSymbol.values();
////        for (int order = 0; order < 9; ++order) {
////            addButton(symbols[order].getSymbol(), new MockAction());
////        }
//
////        addButtons();
//
//    }
//
//    public static final int TEXT_ROWS = 10;
//    public static final int TEXT_COLUMNS = 40;
//
//    public static JPanel create() {
//        var buttonGroup = new ButtonGroup_backup();
//        buttonGroup.setLayout(buttonGroup.gb);
//
////        var buttons = new DigitalButtons();
////        buttons.setLayout(new GridLayout(3, 3));
////        buttons.setPreferredSize(new Dimension(150, 150));
//
//        buttonGroup.addInputBox();
//        buttonGroup.addButtons();
//        buttonGroup.addOutputBox();
//
//
//        return buttonGroup;
//
////        return null;
//    }
//
//    private void addInputBox(){
//        var inputText = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
//        inputText.setEnabled(true);
//        inputText.setLineWrap(true);
//        inputText.setBorder(BorderFactory.createEtchedBorder());
//
//        var scrollPane = new JScrollPane(inputText);
////        inputText.setLineWrap(true);
//
//
//        gb.setConstraints(scrollPane,
//                new Gbc(0, 0, 4, 2)
//                        .setFill(Gbc.BOTH)
//                        .setWeight(400, 200)
//                        .setAnchor(Gbc.NORTHWEST).setInsets(5));
//
//        super.add(scrollPane);
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
//    private void addButtons() {
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
//        for (int row = 0; row < rowLength; ++row) {
//            for (int column = 0; column < columnLength; ++column) {
//                System.out.println(labels[row][column]);
//                addButton(labels[row][column], new MockAction(), new Gbc(column, 1+row+10)
//                        .setWeight(100,100)
//                        .setAnchor(Gbc.SOUTHWEST)
//                        .setFill(Gbc.BOTH));
//            }
//        }
//
//    }
//
//    private void addOutputBox(){
//        var outputtext = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
//        outputtext.setEnabled(true);
//        outputtext.setLineWrap(true);
//        outputtext.setBorder(BorderFactory.createEtchedBorder());
//
//        var scrollPane = new JScrollPane(outputtext);
//        outputtext.setLineWrap(true);
//
//        gb.setConstraints(scrollPane,
//                new Gbc(4, 0, 4, 8+10)
//                        .setFill(Gbc.BOTH)
//                        .setWeight(400, 800)
//                        .setAnchor(Gbc.EAST).setInsets(5));
//
//
//
//        super.add(scrollPane);
//    }
//
////    private void addButtons() {
//////        var gbcThreeButtons = new GridBagConstraints();
//////        var buttonGroup = new ButtonGroup();
////
//////        var
////        var gbcDigitalButtons = new GridBagConstraints();
////        gbcDigitalButtons.weightx = 1;
////        gbcDigitalButtons.weighty = 1;
////        gbcDigitalButtons.fill = GridBagConstraints.BOTH;
//////        gb.setConstraints(digitalButtons,gbcDigitalButtons);
////
////        addButton(UnderlyingSymbol.SEVEN.getSymbol(), new MockAction(), gbcDigitalButtons);
////        addButton(UnderlyingSymbol.EIGHT.getSymbol(), new MockAction(), gbcDigitalButtons);
////        addButton(UnderlyingSymbol.NINE.getSymbol(), new MockAction(), gbcDigitalButtons);
////
////        var gbcRowLastButtons = new GridBagConstraints();
////        gbcRowLastButtons.weightx = 1;
////        gbcRowLastButtons.weighty = 1;
////        gbcRowLastButtons.gridwidth = GridBagConstraints.REMAINDER;
////        gbcRowLastButtons.fill = GridBagConstraints.BOTH;
////        addButton("/", new MockAction(), gbcRowLastButtons);
////
////        addButton(UnderlyingSymbol.FOUR.getSymbol(), new MockAction(), gbcDigitalButtons);
////        addButton(UnderlyingSymbol.FIVE.getSymbol(), new MockAction(), gbcDigitalButtons);
////        addButton(UnderlyingSymbol.SIX.getSymbol(), new MockAction(), gbcDigitalButtons);
////
////        addButton("x", new MockAction(), gbcRowLastButtons);
////
////        addButton(UnderlyingSymbol.ONE.getSymbol(), new MockAction(), gbcDigitalButtons);
////        addButton(UnderlyingSymbol.TWO.getSymbol(), new MockAction(), gbcDigitalButtons);
////        addButton(UnderlyingSymbol.THREE.getSymbol(), new MockAction(), gbcDigitalButtons);
////
////        addButton("-", new MockAction(), gbcRowLastButtons);
////
////        addButton(UnderlyingSymbol.ZERO.getSymbol(), new MockAction(), gbcDigitalButtons);
////        addButton(".", new MockAction(), gbcDigitalButtons);
////        addButton("=", new MockAction(), gbcDigitalButtons);
////        addButton("+", new MockAction(), gbcRowLastButtons);
////        this.setLayout(this.gb);
////
////    }
//
//
////    public static JPanel create() {
////        var buttonGroup = new ButtonGroup();
////        var gb = new GridBagLayout();
////        var gbcDigitalButtons = new GridBagConstraints();
////        var gbcThreeButtons = new GridBagConstraints();
////
////        var digitalButtons = DigitalButtons.create();
////        var threeButtons = ThreeButtons.create();
////
////        gbcDigitalButtons.weightx=1;
////        gbcDigitalButtons.weighty=1;
////        gbcDigitalButtons.gridwidth=GridBagConstraints.REMAINDER;
////        gbcDigitalButtons.anchor=GridBagConstraints.NORTHWEST;
////        gb.setConstraints(digitalButtons,gbcDigitalButtons);
////
////        gbcThreeButtons.weightx=1;
////        gbcThreeButtons.weighty=1;
////        gbcThreeButtons.gridheight=0;
////        gbcThreeButtons.anchor=GridBagConstraints.SOUTHWEST;
////        gb.setConstraints(threeButtons,gbcThreeButtons);
////
////        buttonGroup.setLayout(gb);
////
//////        buttonGroup.setLayout(new GridLayout(2, 1));
////
////        buttonGroup.add(DigitalButtons.create());
////
////        buttonGroup.add(ThreeButtons.create());
////
////        return buttonGroup;
////    }
////
//
//
//    private void addButton(String label, ActionListener listener, GridBagConstraints gbc) {
//        JButton button = new JButton(label);
//        button.addActionListener(listener);
//        gb.setConstraints(button, gbc);
//        super.add(button);
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
