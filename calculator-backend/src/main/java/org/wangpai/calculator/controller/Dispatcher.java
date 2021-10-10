package org.wangpai.calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.wangpai.calculator.exception.CalculatorException;

/**
 * @since 2021-7-27
 */
@Lazy
@Scope("singleton")
@Controller("dispatcher")
@Slf4j
public class Dispatcher implements MiddleController {
    @Qualifier("calculatorMainFace")
    @Autowired
    private MiddleController calculatorMainFace;

    @Qualifier("computingCenter")
    @Autowired
    private MiddleController computingCenter;

    @Override
    public Object passUp(Url url, Object data, MiddleController lowerController) throws CalculatorException {
        return this.passDown(url, data, null);
    }

    /**
     * @since 2021-7-27
     * @lastModified 2021-8-8
     */
    @Override
    public Object passDown(Url url, Object data, MiddleController upperController) throws CalculatorException {
        Object response = null;
        switch (url.getFirstLevelDirectory()) {
            case "view":
                response = this.calculatorMainFace.passDown(url.generateLowerUrl(), data, this);
                break;
            case "service":
                response = this.computingCenter.passDown(url.generateLowerUrl(), data, this);
                break;

            default:
                log.error("错误：使用了未定义的 Url");
                break;
        }
        return response;
    }
}
