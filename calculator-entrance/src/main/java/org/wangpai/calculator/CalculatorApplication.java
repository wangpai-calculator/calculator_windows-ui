package org.wangpai.calculator;

import lombok.extern.slf4j.Slf4j;
import org.wangpai.calculator.model.universal.CentralDatabase;

/**
 * 程序启动入口
 *
 * @since 2021-8-8
 * @lastModified 2021-9-26
 */
@Slf4j
public class CalculatorApplication {
    public static void main(String[] args) {
        // 此函数 main 将在线程 main 中执行

        /**
         * 此方法内的用户的代码将在线程 JavaFX Application Thread 中执行，
         * 且此方法将阻塞本线程 main，直到 JavaFX 线程结束
         */
        CalculatorMainFaceApp.main(args);

        log.info("******** 应用后台程序退出：{}ms ********", System.currentTimeMillis() - CentralDatabase.START_TIME);
    }
}






