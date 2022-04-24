package org.wangpai.calculator.view.mainface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.view.base.FxComponent;
import org.wangpai.calculator.view.base.SpringLinker;

public class CalculatorMainFace implements FxComponent {
    private TerminalController controller;

    @FXML
    private VBox inputVbox;

    @FXML
    private VBox buttongroupVbox;

    @FXML
    private GridPane promptmsgboxGridPane;

    @FXML
    private GridPane resultboxGridPane;

    /**
     * @since 2021-9-27
     */
    @Override
    public void setLinker(SpringLinker linker) {
        if (linker instanceof TerminalController) {
            this.controller = (TerminalController) linker;
        }
    }

    /**
     * @since 2021-9-27
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 敬请期待
    }
}
