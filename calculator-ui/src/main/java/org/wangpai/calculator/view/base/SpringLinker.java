package org.wangpai.calculator.view.base;

import org.springframework.beans.factory.InitializingBean;
import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.Url;

/**
 * @since 2021年9月25日
 */
public abstract class SpringLinker implements InitializingBean, MiddleController {
    /**
     * 实现类还应设置的方法为：
     * - package static XXXLinker getLinker();
     *
     * - package void bindLinker(XXX xxx); // xxx extends FxComponent
     */

    protected abstract MiddleController getUpperController();

    @Override
    public void passUp(Url url, Object data, MiddleController lowerController) {
        this.getUpperController().passUp(url, data, this);
    }

    @Override
    public abstract void passDown(Url url, Object data, MiddleController upperController);

}
