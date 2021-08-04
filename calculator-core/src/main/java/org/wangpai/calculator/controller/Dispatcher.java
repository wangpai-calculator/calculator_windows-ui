package org.wangpai.calculator.controller;

import org.wangpai.calculator.service.ComputingCenter;
import org.wangpai.calculator.view.CalculatorMainPanel;

/**
 * @since 2021-7-27
 */
public class Dispatcher implements MiddleController {
    private CalculatorMainPanel singletonController;

    @Override
    public void sendUp(Url url, Object data, MiddleController lowerController) {
        if (this.singletonController == null
                && lowerController.getClass() == CalculatorMainPanel.class) {
            this.singletonController = (CalculatorMainPanel) lowerController;
        }

        this.sendDown(url, data, null);
    }

    @Override
    public void sendDown(Url url, Object data, MiddleController upperController) {
        switch (url.getFirstLevelDirectory()) {
            case "view":
                this.singletonController.sendDown(url.generateLowerUrl(), data, this);
                break;
            case "service":
                new ComputingCenter().sendDown(url.generateLowerUrl(), data, this);
                break;
        }
    }
}
