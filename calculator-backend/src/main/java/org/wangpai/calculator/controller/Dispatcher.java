package org.wangpai.calculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @since 2021-7-27
 */
@Lazy
@Scope("singleton")
@Controller("dispatcher")
public class Dispatcher implements MiddleController {
    @Qualifier("calculatorMainFace")
    @Autowired
    private MiddleController calculatorMainFace;

    @Qualifier("computingCenter")
    @Autowired
    private MiddleController computingCenter;

    @Override
    public void passUp(Url url, Object data, MiddleController lowerController) {
        this.passDown(url, data, null);
    }

    /**
     * @since 2021-7-27
     * @lastModified 2021-8-8
     */
    @Override
    public void passDown(Url url, Object data, MiddleController upperController) {
        switch (url.getFirstLevelDirectory()) {
            case "view":
                this.calculatorMainFace.passDown(url.generateLowerUrl(), data, this);
                break;
            case "service":
                this.computingCenter.passDown(url.generateLowerUrl(), data, this);
                break;

            default:
                break;
        }
    }
}
