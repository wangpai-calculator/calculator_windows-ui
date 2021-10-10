package org.wangpai.calculator.view.mainface;

import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.CalculatorException;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.base.SpringLinker;
import org.wangpai.calculator.view.control.ButtonGroupLinker;
import org.wangpai.calculator.view.input.InputBoxLinker;
import org.wangpai.calculator.view.output.PromptMsgBoxLinker;
import org.wangpai.calculator.view.output.ResultBoxLinker;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Lazy
@Scope("singleton")
@Controller("calculatorMainFace")
@Slf4j
public class CalculatorMainFaceLinker extends SpringLinker implements MiddleController {
    @Qualifier("dispatcher")
    @Autowired
    private MiddleController upperController;

    @Qualifier("buttonGroup")
    @Autowired
    private ButtonGroupLinker buttonGroup;

    @Qualifier("inputBox")
    @Autowired
    private InputBoxLinker inputBox;

    @Qualifier("resultBox")
    @Autowired
    private ResultBoxLinker resultBox;

    @Qualifier("promptMsgBox")
    @Autowired
    private PromptMsgBoxLinker promptMsgBox;

    private CalculatorMainFace calculatorMainFace;

    @Override
    public void afterPropertiesSet() {
        // 敬请期待
    }

    /**
     * 此方法为包可见
     *
     * @since 2021-9-27
     */
    static CalculatorMainFaceLinker getLinker() {
        return CentralDatabase.getSpringContext()
                .getBean("calculatorMainFace", CalculatorMainFaceLinker.class);
    }

    /**
     * 此方法为包可见
     *
     * @since 2021-9-27
     */
    void bindLinker(CalculatorMainFace calculatorMainFace) {
        this.calculatorMainFace = calculatorMainFace;
        this.calculatorMainFace.setLinker(this);
    }

    /**
     * 此方法为包可见
     * 此方法必须要先于本类的其它方法被调用
     * 使用本方法可以免除使用方法 bindLinker、getLinker
     *
     * @since 2021-9-27
     */
    static void linking(CalculatorMainFace calculatorMainFace) {
        CalculatorMainFaceLinker.getLinker().bindLinker(calculatorMainFace);
    }


    @Override
    protected MiddleController getUpperController() {
        return this.upperController;
    }

    @Override
    public Object passDown(Url url, Object data, MiddleController upperController) throws CalculatorException {
        Object response = null;
        switch (url.getFirstLevelDirectory()) {
            case "inputBox":
                response = this.inputBox.passDown(url.generateLowerUrl(), data, this);
                break;
            case "promptMsgBox":
                response = this.promptMsgBox.passDown(url.generateLowerUrl(), data, this);
                break;
            case "resultBox":
                response = this.resultBox.passDown(url.generateLowerUrl(), data, this);
                break;

            default:
                log.error("错误：使用了未定义的 Url");
                break;
        }
        return response;
    }
}
