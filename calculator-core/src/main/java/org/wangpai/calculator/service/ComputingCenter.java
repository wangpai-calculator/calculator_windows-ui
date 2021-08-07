package org.wangpai.calculator.service;

import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @since 2021-8-1
 */
@Scope("singleton")
@Controller("computingCenter")
public class ComputingCenter implements TerminalController, MiddleController {
    @Resource(name = "dispatcher")
    private MiddleController upperController;

    @Override
    public void sendUp(Url url, Object data, MiddleController lowerController) {
        upperController.sendUp(url, data, this);
    }

    /**
     * 此方法一定先被调用
     */
    @Override
    public void sendDown(Url url, Object data, MiddleController upperController) {
        receive(url, data); // 注意：此处不使用 url.generateLowerUrl()
    }

    @Override
    public void send(Url url, Object data) {
        sendUp(url, data, null);
    }

    @Override
    public void receive(Url url, Object data) {
        if (data instanceof String) {
            this.receive(url, (String) data);
        } else {
            // 敬请期待
        }
    }

    private void receive(Url url, String str) {
        switch (url.getFirstLevelDirectory()) {
            case "expression":
                new CalculatorService(this).readExpression(str);
                break;

            default:
                break;
        }
    }
}
