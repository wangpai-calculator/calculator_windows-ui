package org.wangpai.calculator.controller;

import org.wangpai.mathlab.exception.MathlabException;

/**
 * 终端控制器。实现终端控制器的类一般也要同时实现中间控制器（MiddleController）。
 *
 * 它要做 4 件事情：
 * - 将来自具体控件的信息交给【方法 passUp】来处理。【方法 send】
 * - 将来自【方法 send】的信息向上传递。【方法 passUp】
 * - 将上级控制器向下传递的信息交给【方法 receive】来处理。【方法 passDown】
 * - 将来自【方法 passDown】的信息发送给具体控件。【方法 receive】
 *
 * @since 2021-8-2
 */
public interface TerminalController extends Controller {
    /**
     * 此处的方法 send、receive 都是针对最终目标来说的接收与发送，而不是针对控制器自身来说的
     *
     * 最高层控制器的这两个方法不准被使用（如果最高层控制器不同时是终端控制器的话）
     */

    /**
     * 此函数由非控制器的发送方调用，不能由终端控制器自己调用
     */
    Object send(Url url, Object data) throws MathlabException;

    /**
     * 含义：发送给最终目标的函数。最终目标是接收者
     *
     * 用法：
     * 此方法只能由终端控制器自己的方法 send、passDown 调用，不能由其它控制器调用。
     * 此方法将根据 data 的类型将数据转发给相应的更具体的 receive 方法
     */
    Object receive(Url url, Object data) throws MathlabException;
}
