package org.wangpai.calculator.view.input;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.view.base.SpringLinker;
import org.wangpai.calculator.view.base.TextBox;

import java.net.URL;
import java.util.ResourceBundle;

public class InputBox extends TextBox {
    @Getter(AccessLevel.PACKAGE)
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initSuperTextArea(this.textArea);
        InputBoxLinker.linking(this);

        var controller = this.getController();
        this.textArea.textProperty().addListener(new ChangeListener<String>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                controller.send(new Url("/service/expression"),
                        InputBox.removeUselessChar(newValue));

//                System.out.println("observable = " + observable + ", oldValue = " + oldValue + ", newValue = " + newValue);
            }
        });
    }
}
