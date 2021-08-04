package org.wangpai.calculator.controller;


/**
 * 中间控制器。它分为两种：
 * - 非中央控制器。它只做 2 件事情：
 * + + 将下级控制器向上传递的信息直接原封不动地向上传递。【方法 sendUp】
 * + + 将上级控制器向下传递的信息继续向下传递。它需要决定将信息向下发送给哪个下级控制器。【方法 sendDown】
 * - 中央控制器。它只做 2 件事情：
 * + + 将下级控制器向上传递的信息准备向下传递。【方法 sendUp】
 * + + 决定将向下传递的信息传递给哪个下级控制器。【方法 sendDown】
 *
 * @since 2021-8-2
 */
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
