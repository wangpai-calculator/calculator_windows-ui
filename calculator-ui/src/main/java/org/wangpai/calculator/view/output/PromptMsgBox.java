package org.wangpai.calculator.view.output;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;
import org.wangpai.calculator.view.base.TextBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PromptMsgBox extends TextBox {
    @Getter(AccessLevel.PACKAGE)
    @FXML
    private VBox textareaVBox;

    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 从 VBox 中取出 TextArea
        var node = textareaVBox.getChildren().get(0);
        if (node instanceof TextArea) {
            this.textArea = (TextArea) node;
        }
        super.initSuperTextArea(this.textArea);
        PromptMsgBoxLinker.linking(this);
    }

    /**
     * @since 2021-8-7
     */
    @Override
    public PromptMsgBox setText(String msg) {
        this.textArea.setText(msg);
        this.setBarAtTheBottom(); // 将滚动条拨到最下
        return this;
    }

    /**
     * @since 2021-8-7
     */
    @Override
    public PromptMsgBox append(String msg) {
        if (this.getText().equals("")) {
            this.textArea.setText(msg);
        } else {
            this.textArea.appendText(msg);
        }
        this.setBarAtTheBottom(); // 将滚动条拨到最下
        return this;
    }
}