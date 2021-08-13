package org.wangpai.calculator.view;

import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.view.base.SpringLinker;
import org.wangpai.calculator.view.control.ButtonGroupLinker;
import org.wangpai.calculator.view.input.InputBoxLinker;
import org.wangpai.calculator.view.output.PromptMsgBoxLinker;
import org.wangpai.calculator.view.output.ResultBoxLinker;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;

@Lazy
@Scope("singleton")
@Controller("calculatorMainFace")
public class CalculatorMainFaceLinker extends SpringLinker implements MiddleController {
    @Resource(name = "dispatcher")
    private MiddleController upperController;

    @Resource(name = "buttonGroup")
    private ButtonGroupLinker buttonGroup;

    @Resource(name = "inputBox")
    private InputBoxLinker inputBox;

    @Resource(name = "resultBox")
    private ResultBoxLinker resultBox;

    @Resource(name = "promptMsgBox")
    private PromptMsgBoxLinker promptMsgBox;

    @Override
    public void afterPropertiesSet() {
        // 敬请期待
    }

    @Override
    protected MiddleController getUpperController() {
        return this.upperController;
    }

    @Override
    public void passDown(Url url, Object data, MiddleController upperController) {
        switch (url.getFirstLevelDirectory()) {
            case "inputBox":
                this.inputBox.passDown(url.generateLowerUrl(), data, this);
                break;
            case "promptMsgBox":
                this.promptMsgBox.passDown(url.generateLowerUrl(), data, this);
                break;
            case "resultBox":
                this.resultBox.passDown(url.generateLowerUrl(), data, this);
                break;
        }
    }
}
