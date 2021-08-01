package org.wangpai.calculator.controller;

public interface MiddleController extends Controller {
    /**
     * 此函数只能由下层控制器的方法 sendUp，或本层终端控制器的方法 send 来调用。
     * 对于最高层控制器，此方法只能调用自己的方法 sendDown
     * 对于终端控制器，调用本方法传入的第三个实参应为 null
     */
    void sendUp(Url url, Object data, MiddleController lowerController);

    /**
     * 此函数只能由上层控制器的方法 sendDown，或最高层控制器的 sendUp 方法来调用。
     * 对于终端控制器，此方法只能调用自己的方法 receive
     */
    void sendDown(Url url, Object data, MiddleController upperController);
}
