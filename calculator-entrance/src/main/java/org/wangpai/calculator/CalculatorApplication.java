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
        CalculatorMainFaceApp.main(args);
        log.info("******** 应用后台程序退出：{}ms ********", System.currentTimeMillis() - CentralDatabase.startTime);
    }
}






