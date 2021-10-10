package org.wangpai.calculator.view.output;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.model.universal.Function;
import org.wangpai.calculator.model.universal.Multithreading;
import org.wangpai.calculator.view.base.TextBox;

import static org.wangpai.calculator.view.output.PromptMsgBoxState.INIT_TEXT;

@Slf4j
public class PromptMsgBox extends TextBox {

    @FXML
    private VBox textareaVBox;

    private TextArea textArea;

    /**
     * 提示框的状态
     *
     * @since 2021-10-12
     */
    @Setter
    @Getter
    private PromptMsgBoxState state = INIT_TEXT;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var promptMsgBox = this;
        // 懒执行
        Multithreading.execute(new Function() {
            @Override
            public void run() {
                log.info("开始初始化 PromptMsgBox。时间：{}ms", System.currentTimeMillis() - CentralDatabase.startTime);

                PromptMsgBoxLinker.linking(promptMsgBox);

                Platform.runLater(() -> {
                    // 从 VBox 中取出 TextArea
                    var node = textareaVBox.getChildren().get(0);
                    if (node instanceof TextArea) {
                        promptMsgBox.textArea = (TextArea) node;
                    }
                    promptMsgBox.initSuperTextArea(promptMsgBox.textArea);
                    promptMsgBox.setFocusPriority(false);

                    log.info("PromptMsgBox 初始化完成。时间：{}ms", System.currentTimeMillis() - CentralDatabase.startTime);
                });
            }
        });
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