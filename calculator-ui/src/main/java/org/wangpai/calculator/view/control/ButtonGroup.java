package org.wangpai.calculator.view.control;

import lombok.extern.slf4j.Slf4j;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.application.Platform;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.ConflictException;
import org.wangpai.calculator.model.symbol.enumeration.Symbol;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.model.universal.Function;
import org.wangpai.calculator.model.universal.Multithreading;
import org.wangpai.calculator.view.base.FxComponent;
import org.wangpai.calculator.view.base.SpringLinker;

@Slf4j
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
     * 当方法 setButtonsStyle 完成调用时，此值为 true
     *
     * 使用 volatile 是为了在多线程中避免使用锁，因此必须自行保证对此值操作的原子性
     *
     * @since 2021-10-10
     */
    private volatile boolean buttonInitIsFinished;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("开始初始化 ButtonGroup。时间：{}ms", System.currentTimeMillis() - CentralDatabase.startTime);

        var buttonGroup = this;
        buttonGroup.setButtonsStyle(); // 设置按钮文本、击键颜色等

        // 懒执行
        Multithreading.execute(new Function() {
            @Override
            public void run() {
                // 初始化 controller
                ButtonGroupLinker.linking(buttonGroup);

                /**
                 * 如果方法 setButtonsStyle 还没有完成调用，一直等待直到其调用为止
                 */
                while (!buttonInitIsFinished) {
                    try {
                        Thread.sleep(0); // 触发线程调度。防止 CPU 一直执行此循环从而导致死锁
                    } catch (InterruptedException exception) {
                        log.error("发生了非自定义异常：", exception, exception);
                    }
                    continue;
                }
                Platform.runLater(() -> {
                    buttonGroup.setPracticalButtons(); // 设置非功能键
                    buttonGroup.setFunctionButtons(); // 设置功能键
                    buttonGroup.setConcealedFunctions(); // 设置特殊隐藏功能

                    log.info("ButtonGroup 初始化完成。时间：{}ms", System.currentTimeMillis() - CentralDatabase.startTime);
                });
            }
        });
    }

    /**
     * 设置按钮文本、击键颜色
     *
     * @since 2021-10-10
     */
    private void setButtonsStyle() {
        final int rowLength = labels.length;
        final int columnLength = labels[0].length;
        this.buttons = new Button[rowLength][columnLength];
        this.functionButtons = new ArrayList<>();
        this.practicalButton = new ArrayList<>();
        this.concealedFunctions = new ArrayList<>();

        /**
         * 将这些对象置于全局容器中，供其它地方的类使用
         */
        var container = CentralDatabase.getContainer();
        String[] keys = {"buttons", "functionButtons", "practicalButton", "concealedFunctions"};
        Object[] values = {this.buttons, this.functionButtons,
                this.practicalButton, this.concealedFunctions};
        for (int order = 0; order < keys.length; ++order) {
            if (container.containsKey(keys[order])) {
                try {
                    throw new ConflictException("键" + keys[order] + "已存在");
                } catch (Exception exception) {
                    log.error("异常：", exception);
                }
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

        this.buttonInitIsFinished = true;
    }

    /**
     * 设置非功能键
     *
     * @since 2021-10-10
     */
    private void setPracticalButtons() {
        for (var button : this.practicalButton) {
            button.setOnAction(actionEvent -> {
                try {
                    controller.send(new Url("/view/inputBox/insert"), button.getText());
                } catch (Exception exception) {
                    log.error("异常：", exception);
                }
            });
        }// for-each
    }

    /**
     * 设置功能键
     *
     * @since 2021-10-10
     */
    private void setFunctionButtons() {
        for (var button : this.functionButtons) {
            button.setOnAction(actionEvent -> {
                String str = button.getText();
                try {
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

                        default:
                            log.error("错误：出现了意料之外的符号：{}", str);
                            break;
                    }
                } catch (Exception exception) {
                    log.error("异常：", exception);
                }
            });
        } // for-each
    }

    /**
     * 设置特殊隐藏功能
     *
     * @since 2021-9-28
     */
    private void setConcealedFunctions() {
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
        concealedButton.setOnAction(actionEvent -> {
            try {
                controller.send(new Url("/view/inputBox/focus"), "");
            } catch (Exception exception) {
                log.error("异常：", exception);
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
