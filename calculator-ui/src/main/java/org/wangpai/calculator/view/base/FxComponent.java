package org.wangpai.calculator.view.base;

import javafx.fxml.Initializable;
import org.wangpai.calculator.controller.TerminalController;

public interface FxComponent extends Initializable {
    /**
     * @since 2021年9月25日
     */
    void setLinker(SpringLinker linker);
}
