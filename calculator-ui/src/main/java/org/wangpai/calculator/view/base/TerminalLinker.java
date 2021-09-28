package org.wangpai.calculator.view.base;

import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;

public abstract class TerminalLinker extends SpringLinker implements TerminalController {
    /**
     * 实现类还应设置的方法为：
     * - package static XXXLinker getLinker();
     *
     * - package void bindLinker(XXX xxx); // xxx extends FxComponent
     */

    @Override
    public void passDown(Url url, Object data, MiddleController upperController) {
        receive(url, data); // 注意：此处不使用 url.generateLowerUrl()
    }

    @Override
    public void send(Url url, Object data) {
        this.passUp(url, data, null);
    }

    @Override
    public void receive(Url url, Object data) {
        if (data instanceof String) {
            this.receive(url, (String) data);
        } else {
            // 待子类酌情实现
        }
    }

    /**
     * 注意，如果此方法涉及更新 JavaFX 的 UI，则要将更新 UI 的代码置于以下代码中：
     * Platform.runLater(() -> {...});
     * 这里为了保护 JavaFX UI 的线程安全
     *
     * @since 2021-9-28
     */
    public abstract void receive(Url url, String str);
}
