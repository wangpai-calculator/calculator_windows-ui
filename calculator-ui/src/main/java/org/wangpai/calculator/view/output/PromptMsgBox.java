package org.wangpai.calculator.view.output;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import org.wangpai.calculator.view.base.TextBox;

public class PromptMsgBox extends TextBox {
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
        this.setFocusPriority(false);
    }

    /**
     * @since 2021-8-7
     */
    @Override
    public PromptMsgBox setText(String msg) {
        super.setText(msg);
        this.setBarAtTheBottom(); // 将滚动条拨到最下
        return this;
    }

    /**
     * @since 2021-8-7
     */
    @Override
    public PromptMsgBox append(String msg) {
        super.append(msg);
        this.setBarAtTheBottom(); // 将滚动条拨到最下
        return this;
    }
}