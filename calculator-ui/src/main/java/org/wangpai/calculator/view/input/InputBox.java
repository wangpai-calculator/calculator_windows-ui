package org.wangpai.calculator.view.input;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.model.universal.Function;
import org.wangpai.calculator.model.universal.Multithreading;
import org.wangpai.calculator.view.base.TextBox;

@Slf4j
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
                log.info("开始初始化 InputBox。时间：{}ms", System.currentTimeMillis() - CentralDatabase.START_TIME);

                InputBoxLinker.linking(inputBox);

                Platform.runLater(() -> {
                    inputBox.initSuperTextArea(inputBox.textArea);
                    inputBox.setFocusPriority(true);
                    inputBox.textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                        // 开新线程来完成下面的操作
                        Multithreading.execute(new Function() {
                            @Override
                            public void run() {
                                try {
                                    controller.send(new Url("/service/expression"),
                                            InputBox.removeUselessChar(newValue));
                                } catch (Exception exception) {
                                    log.error("异常：", exception);
                                }
                            }
                        });
                    });

                    log.info("InputBox 初始化完成。时间：{}ms", System.currentTimeMillis() - CentralDatabase.START_TIME);
                });
            }
        });
    }
}
