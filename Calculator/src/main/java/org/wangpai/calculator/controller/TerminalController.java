package org.wangpai.calculator.controller;

public interface TerminalController extends Controller{
    /**
     * 此处的方法 send、receive 都是针对最终目标来说的接收与发送，而不是针对控制器自身来说的
     *
     * 最高层控制器的这两个方法不准被使用（如果最高层控制器不同时是终端控制器的话）
     */

    /**
     * 此函数由非控制器的发送方调用，不能由终端控制器自己调用
     */
    void send(Url url, Object data);

    /**
     * 含义：发送给最终目标的函数。最终目标是接收者
     *
     * 用法：此方法只能由终端控制器自己的方法 send、sendDown 调用，不能由其它控制器调用
     */
    void receive(Url url, Object data);
}
