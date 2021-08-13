package org.wangpai.calculator.model.universal;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CentralDatabase {
    private static AbstractXmlApplicationContext springContext;

    public static AbstractXmlApplicationContext getSpringContext() {
        if (CentralDatabase.springContext == null) {
            // Spring 的此路径在 IntelliJ IDEA 中是以资源目录 resources 为基准的
            CentralDatabase.springContext =
                    new ClassPathXmlApplicationContext("spring/beanScan.xml");
        }
        return CentralDatabase.springContext;
    }
}
