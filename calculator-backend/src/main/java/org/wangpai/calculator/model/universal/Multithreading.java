package org.wangpai.calculator.model.universal;

import javafx.concurrent.Task;

/**
 * @since 2021-10-3
 */
public class Multithreading {
    /**
     * @since 2021-10-3
     */
    public static void execute(Function function) {
        // 开新线程来完成下面的操作
        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() {
                function.run();
                return null;
            }
        };
        CentralDatabase.getTasks().add(task);
        CentralDatabase.getExecutor().execute(task);
    }
}
