package org.wangpai.calculator.view.base;

import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;
import org.wangpai.calculator.exception.CalculatorException;

public abstract class TerminalLinker extends SpringLinker implements TerminalController {
    /**
     * 实现类还应设置的方法为：
     * - package static XXXLinker getLinker();
     *
     * - package void bindLinker(XXX xxx); // xxx extends FxComponent
     */

    @Override
    public Object passDown(Url url, Object data, MiddleController upperController) throws CalculatorException {
        return receive(url, data); // 注意：此处不使用 url.generateLowerUrl()
    }

    @Override
    public Object send(Url url, Object data) throws CalculatorException {
        return this.passUp(url, data, null);
    }
}
