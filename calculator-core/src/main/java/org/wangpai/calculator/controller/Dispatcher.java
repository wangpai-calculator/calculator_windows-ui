package org.wangpai.calculator.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.wangpai.calculator.service.ComputingCenter;
import org.wangpai.calculator.view.CalculatorMainPanel;

import javax.annotation.Resource;

/**
 * @since 2021-7-27
 */
@Scope("singleton")
@Controller("dispatcher")
public class Dispatcher implements MiddleController {
    @Resource(name = "calculatorMainPanel")
    private MiddleController calculatorMainPanel;

    @Resource(name = "computingCenter")
    private MiddleController computingCenter;

    @Override
    public void sendUp(Url url, Object data, MiddleController lowerController) {
        this.sendDown(url, data, null);
    }

    /**
     * @since 2021-7-27
     * @lastModified 2021-8-8
     */
    @Override
    public void sendDown(Url url, Object data, MiddleController upperController) {
        switch (url.getFirstLevelDirectory()) {
            case "view":
                this.calculatorMainPanel.sendDown(url.generateLowerUrl(), data, this);
                break;
            case "service":
                this.computingCenter.sendDown(url.generateLowerUrl(), data, this);
                break;

            default:
                break;
        }
    }
}
