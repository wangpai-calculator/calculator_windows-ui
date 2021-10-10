package org.wangpai.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.wangpai.calculator.exception.CalculatorException;

/**
 * @since 2021-8-1
 */
@Lazy
@Scope("singleton")
@Controller("computingCenter")
@Slf4j
public class ComputingCenter implements TerminalController, MiddleController {
    @Qualifier("dispatcher")
    @Autowired
    private MiddleController upperController;

    @Qualifier("calculatorService")
    @Autowired
    private CalculatorService calculatorService;

    @Override
    public Object passUp(Url url, Object data, MiddleController lowerController) throws CalculatorException {
        return upperController.passUp(url, data, this);
    }

    /**
     * 此方法一定先被调用
     */
    @Override
    public Object passDown(Url url, Object data, MiddleController upperController) throws CalculatorException {
        return receive(url, data); // 注意：此处不使用 url.generateLowerUrl()
    }

    @Override
    public Object send(Url url, Object data) throws CalculatorException {
        return passUp(url, data, null);
    }

    @Override
    public Object receive(Url url, Object data) throws CalculatorException {
        Object response = null;
        if (data instanceof String) {
            response = this.receive(url, (String) data);
        } else {
            // 敬请期待
        }
        return response;
    }

    private Object receive(Url url, String str) {
        Object response = null;
        switch (url.getFirstLevelDirectory()) {
            case "expression":
                calculatorService.readExpression(str);
                break;

            default:
                log.error("错误：使用了未定义的 Url");
                break;
        }
        return response;
    }
}
