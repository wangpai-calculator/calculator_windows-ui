package org.wangpai.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.mathlab.exp.exposed.CalculatorBackgroundFx;

/**
 * @since 2021-8-1
 */
@Lazy
@Scope("singleton")
@Service("calculatorService")
@Slf4j
public final class CalculatorService implements InitializingBean {
    @Qualifier("computingCenter")
    @Autowired
    private TerminalController controller;

    private CalculatorBackgroundFx calculator;

    protected CalculatorService() {
        super();
    }

    /**
     * 此构造器供不使用 Spring 进行依赖注入时使用本类
     *
     * @param controller 上层控制器对象
     */
    public CalculatorService(TerminalController controller) {
        super();
        this.controller = controller;
    }

    /**
     * @since 2022-8-24
     */
    @Override
    public void afterPropertiesSet() {
        this.calculator = new CalculatorBackgroundFx()
                .setAutoCalculateAction(data -> {
                    this.sendPromptMsg((String) data);
                })
                .setErrorOccurredAction(data -> {
                    this.sendExceptionMsg((String) data);
                })
                .setProcessUpdatedAction(data -> {
                    this.sendCalculationProcess((String) data);
                })
                .setDefaultPromptMsgAction(data -> {
                    this.sendNoSyntaxErrorDetectedPromptMsg((String) data);
                })
                .setResultCameOutAction(data -> {
                    // 敬请期待
                });
    }

    /**
     * @since 2021-8-1
     * @lastModified 2022-8-24
     */
    private void sendPromptMsg(String msg) {
        try {
            this.controller.send(new Url("/view/promptMsgBox/setText"),
                    System.lineSeparator() + msg);
        } catch (Exception exception) {
            log.error("异常：", exception);
        }
    }

    /**
     * @since 2021-8-1
     * @lastModified 2022-8-24
     */
    private void sendExceptionMsg(String exceptionMsg) {
        try {
            String promptMsgPrefix = System.lineSeparator() + System.lineSeparator();
            this.controller.send(new Url("/view/promptMsgBox/setText"), promptMsgPrefix + exceptionMsg);
        } catch (Exception sendException) {
            log.error("异常：", exceptionMsg);
        }
    }

    /**
     * @since 2021-8-1
     * @lastModified 2022-8-24
     */
    private void sendNoSyntaxErrorDetectedPromptMsg(String msg) {
        try {
            this.controller.send(new Url("/view/promptMsgBox/setText"),
                    System.lineSeparator() + msg);
        } catch (Exception exception) {
            log.error("异常：", exception);
        }
    }

    /**
     * @since 2021-8-1
     * @lastModified 2022-8-24
     */
    private void sendCalculationProcess(String processMsg) {
        try {
            String processMsgPrefix = System.lineSeparator() + System.lineSeparator();
            this.controller.send(new Url("/view/resultBox/append"), processMsgPrefix + processMsg);
        } catch (Exception exception) {
            log.error("异常：", exception);
        }
    }

    /**
     * @since 2021-8-4
     * @lastModified 2022-8-24
     */
    public void readExpression(final String expression) {
        this.calculator.readExpression(expression);
    }
}
