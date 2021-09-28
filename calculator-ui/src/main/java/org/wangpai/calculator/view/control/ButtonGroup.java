package org.wangpai.calculator.view.control;

import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.ConflictException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.FxComponent;
import org.wangpai.calculator.view.base.SpringLinker;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class ButtonGroup implements FxComponent {
    private TerminalController controller;

    @FXML
    private GridPane gridPane;

    private Button[][] buttons;

    // 功能键
    private List<Button> functionButtons;

    // 有实际意义的按钮，非功能键
    private List<Button> practicalButton;

    // 一些未设置成实际按钮的功能。此功能也可以设计成快捷键来使用
    private List<Button> concealedFunctions;

    /**
     * 规定：
     * 1. 第一排按钮为功能键
     * 2. 没有在 Symbol 中定义，且不为 null 的键为功能键
     * 3. 名称为 null 的按键为未定义按键。未定义按键不会触发任何事件
     */
    private final String[][] labels = new String[][]{
            {"❮", "❯", "✅", "☒"},
            {Symbol.SEVEN.toString(),
                    Symbol.EIGHT.toString(),
                    Symbol.NINE.toString(),
                    Symbol.DIVIDE.toString()},
            {Symbol.FOUR.toString(),
                    Symbol.FIVE.toString(),
                    Symbol.SIX.toString(),
                    Symbol.MULTIPLY.toString()},
            {Symbol.ONE.toString(),
                    Symbol.TWO.toString(),
                    Symbol.THREE.toString(),
                    Symbol.SUBTRACT.toString()},
            {Symbol.ZERO.toString(),
                    Symbol.DOT.toString(),
                    "⟲",
                    Symbol.ADD.toString()},
            {Symbol.LEFT_BRACKET.toString(),
                    Symbol.RIGHT_BRACKET.toString(),
                    "⟳",
                    Symbol.EQUAL.toString()}};

    /**
     * 此方法源自接口，无法设置为包可见
     *
     * @since 2021年9月25日
     */
    @Override
    public void setLinker(SpringLinker linker) {
        if (linker instanceof TerminalController) {
            this.controller = (TerminalController) linker;
        }
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ButtonGroupLinker.linking(this);

        final int rowLength = labels.length;
        final int columnLength = labels[0].length;
        this.buttons = new Button[rowLength][columnLength];
        this.functionButtons = new ArrayList<>();
        this.practicalButton = new ArrayList<>();
        this.concealedFunctions =new ArrayList<>();

        /**
         * 将这些对象置于全局容器中，供其它地方的类使用
         */
        var container = CentralDatabase.getContainer();
        String[] keys = {"buttons", "functionButtons", "practicalButton", "concealedFunctions"};
        Object[] values = {this.buttons, this.functionButtons, this.practicalButton, this.concealedFunctions};
        for (int order = 0; order < keys.length; ++order) {
            if (container.containsKey(keys[order])) {
                throw new ConflictException("键" + keys[order] + "已存在");
            }
            container.put(keys[order], values[order]);
        }

        var children = gridPane.getChildren();
        Iterator iterator = children.iterator();
        for (int row = 0; row < rowLength; ++row) {
            for (int column = 0; column < columnLength; ++column) {
                var button = (Button) iterator.next();
                var label = labels[row][column];

                button.setText(label);
                // 设置击键时的颜色变化。击键后按键变为灰色
                button.setOnMousePressed(event -> button.setStyle("-fx-background-color: grey"));
                // 将 Style 设置为空串时，JavaFX 会为之恢复之前的 Style
                button.setOnMouseReleased(event -> button.setStyle(""));

                this.buttons[row][column] = button;
                if (Symbol.getEnum(label) != null) {
                    this.practicalButton.add(button);
                } else if (!label.equals("null")) {
                    this.functionButtons.add(button);
                } else {
                    // 未定义按键不触发任何事件
                }
            }
        }

        for (var button : this.practicalButton) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @SneakyThrows // 此处不能简写为 lambda 表达式
                @Override
                public void handle(ActionEvent actionEvent) {
                    controller.send(new Url("/view/inputBox/insert"), button.getText());
                }
            });
        }// for-each

        for (var button : this.functionButtons) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @SneakyThrows // 此处不能简写为 lambda 表达式
                @Override
                public void handle(ActionEvent actionEvent) {
                    String str = button.getText();
                    switch (str) {
                        case "❮":
                            controller.send(new Url("/view/inputBox/leftShift"), str);
                            break;
                        case "❯":
                            controller.send(new Url("/view/inputBox/rightShift"), str);
                            break;
                        case "✅":
                            controller.send(new Url("/view/inputBox/selectAll"), str);
                            break;
                        case "☒":
                            controller.send(new Url("/view/inputBox/delete"), str);
                            break;
                        case "⟲":
                            controller.send(new Url("/view/inputBox/undo"), str);
                            break;
                        case "⟳":
                            controller.send(new Url("/view/inputBox/redo"), str);
                            break;
                    }
                }
            });
        } // for-each

        // 设置特殊隐藏功能
        this.setConcealedFunctions();
    }

    /**
     * 设置特殊隐藏功能
     *
     * @since 2021-9-28
     */
    private void setConcealedFunctions(){
        this.setFocusFunction();
    }

    /**
     * 设置聚焦功能
     *
     * @since 2021-9-28
     */
    private void setFocusFunction() {
        Button concealedButton = new Button("focus");
        this.concealedFunctions.add(concealedButton);
        concealedButton.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows // 此处不能简写为 lambda 表达式
            @Override
            public void handle(ActionEvent actionEvent) {
                controller.send(new Url("/view/inputBox/focus"), "");
            }
        });
    }

    /**
     * 注意：Scene 一般等到它之中的所有组件初始化之后才初始化，
     * 因此，在本组件刚初始化时，Scene 可能为 null
     */
    private Scene getScene() {
        return this.gridPane.getScene();
    }
}
