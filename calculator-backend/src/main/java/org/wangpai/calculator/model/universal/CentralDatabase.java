package org.wangpai.calculator.model.universal;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @since 2021-9-27
 */
public class CentralDatabase {
    // 锁定辅助对象
    private static final Object LOCK = new Object();

    // 这里使用了单例模式中的“双重检查锁定”
    private volatile static AbstractXmlApplicationContext springContext;

    private volatile static Map container;

    /**
     * 注意：记得在程序结束时，如果 executor 不为 null， 使用 “executor.shutdown();” 来回收资源
     *
     * @since 2021-9-28
     */
    private volatile static ExecutorService executor;

    /**
     * 注意：记得在程序结束时，如果 tasks 不为 null，对于每一个 task，使用 “task.cancel();” 来回收资源
     *
     * @since 2021-9-28
     */
    private volatile static List<Task<Integer>> tasks;

    /**
     * 程序运行开始时间（单位：ms）
     *
     * @since 2021-9-28
     */
    public static final long startTime = System.currentTimeMillis();

    /**
     * 此方法是强同步方法，此方法不能返回 null，因此不能新建线程来创建 Spring 容器。
     * 使用此方法而不是直接使用类字段是为了进行“懒加载”
     *
     * 这里使用了单例模式中的“双重检查锁定”
     *
     * @since 2021-9-28
     */
    public static AbstractXmlApplicationContext getSpringContext() {
        // 第一重判断
        if (CentralDatabase.springContext == null) {
            // 上锁
            synchronized (LOCK) {
                // 第二重判断
                if (CentralDatabase.springContext == null) {
                    System.out.println("开始初始化 Spring Bean。时间："
                            + (System.currentTimeMillis() - CentralDatabase.startTime) + "ms");

                    // Spring 的此路径在 IntelliJ IDEA 中是以资源目录 resources 为基准的
                    CentralDatabase.springContext =
                            new ClassPathXmlApplicationContext("spring/beanScan.xml");

                    System.out.println("Spring Bean 初始化结束。时间："
                            + (System.currentTimeMillis() - CentralDatabase.startTime) + "ms");
                }
            }
        }
        return CentralDatabase.springContext;
    }

    /**
     * 使用此方法而不是直接使用类字段是为了进行“懒加载”
     *
     * 这里使用了单例模式中的“双重检查锁定”
     *
     * @since 2021-9-27
     * @lastModified 2021-9-28
     */
    public static Map getContainer() {
        // 第一重判断
        if (CentralDatabase.container == null) {
            // 上锁
            synchronized (LOCK) {
                // 第二重判断
                if (CentralDatabase.container == null) {
                    CentralDatabase.container = new HashMap();
                }
            }
        }
        return CentralDatabase.container;
    }

    /**
     * 使用此方法而不是直接使用类字段是为了进行“懒加载”
     *
     * 这里使用了单例模式中的“双重检查锁定”
     *
     * @since 2021-9-28
     */
    public static ExecutorService getExecutor() {
        // 第一重判断
        if (CentralDatabase.executor == null) {
            // 上锁
            synchronized (LOCK) {
                // 第二重判断
                if (CentralDatabase.executor == null) {
                    // 设置初始线程个数，大致为 6
                    CentralDatabase.executor = Executors.newFixedThreadPool(6);
                }
            }
        }
        return CentralDatabase.executor;
    }

    /**
     * 使用此方法而不是直接使用类字段是为了进行“懒加载”
     *
     * @since 2021-9-28
     */
    public static List<Task<Integer>> getTasks() {
        // 第一重判断
        if (CentralDatabase.tasks == null) {
            // 上锁
            synchronized (LOCK) {
                // 第二重判断
                if (CentralDatabase.tasks == null) {
                    CentralDatabase.tasks = new ArrayList<>();
                }
            }
        }

        return CentralDatabase.tasks;
    }

    /**
     * 记得在打算关闭应用时调用此方法。
     * 不能在应用正在运行时调用此方法
     *
     * 作用：回收本类所有关于多线程的资源
     *
     * @since 2021-9-28
     */
    public static void multithreadingClosed() {
        if (CentralDatabase.tasks != null) {
            for (var task : CentralDatabase.tasks) {
                if (!task.isDone()) {
                    task.cancel();
                }
            }
        }

        if (CentralDatabase.executor != null) {
            CentralDatabase.executor.shutdown();
        }
    }
}

