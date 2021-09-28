package org.wangpai.calculator.view.input;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.SneakyThrows;
import java.net.URL;
import java.util.ResourceBundle;

import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.TextBox;

public class InputBox extends TextBox {
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initSuperTextArea(this.textArea);
        InputBoxLinker.linking(this);
        this.setFocusPriority(true);

        var controller = this.getController();
        this.textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            // 开新线程来完成下面的操作
            Task<Integer> task = new Task<>() {
                @SneakyThrows
                @Override
                protected Integer call() {
                    controller.send(new Url("/service/expression"),
                            InputBox.removeUselessChar(newValue));
                    return null;
                }
            };

            CentralDatabase.getTasks().add(task);
            CentralDatabase.getExecutor().execute(task);
        });
    }
}
