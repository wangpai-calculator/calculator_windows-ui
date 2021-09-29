package org.wangpai.calculator.view.input;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.SneakyThrows;
import java.net.URL;
import java.util.ResourceBundle;

import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.model.universal.Function;
import org.wangpai.calculator.model.universal.Multithreading;
import org.wangpai.calculator.view.base.TextBox;

public class InputBox extends TextBox {
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var inputBox = this;
        // 懒执行
        Multithreading.execute(new Function() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    System.out.println("开始初始化 InputBox。时间："
                            + (System.currentTimeMillis() - CentralDatabase.startTime) + "ms");

                    inputBox.initSuperTextArea(inputBox.textArea);
                    inputBox.setFocusPriority(true);
                    InputBoxLinker.linking(inputBox);
                    var controller = inputBox.getController();
                    inputBox.textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                        // 开新线程来完成下面的操作
                        Multithreading.execute(new Function() {
                            @SneakyThrows
                            @Override
                            public void run() {
                                controller.send(new Url("/service/expression"),
                                        InputBox.removeUselessChar(newValue));
                            }
                        });
                    });

                    System.out.println("InputBox 初始化完成。时间："
                            + (System.currentTimeMillis() - CentralDatabase.startTime) + "ms");
                });
            }
        });
    }
}
