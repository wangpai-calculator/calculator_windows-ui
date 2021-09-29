package org.wangpai.calculator.view.wrapped;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import java.util.List;
import lombok.SneakyThrows;
import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.model.universal.Function;
import org.wangpai.calculator.model.universal.Multithreading;

/**
 * Scene 的包装类。此类主要是用来完成一些初始化好 Scene 后才能进行的配置
 *
 * @since 2021-9-27
 */
public class SceneWrapped extends Scene {
    private Scene scene = this;

    public SceneWrapped(Parent parent) {
        super(parent);

        this.afterInitScene();
    }

    private void afterInitScene() {
        // 懒执行
        Multithreading.execute(new Function() {
            @Override
            public void run() {
                System.out.println("开始执行 afterInitScene()。时间："
                        + (System.currentTimeMillis() - CentralDatabase.startTime) + "ms");

                TaskAboutBindShortcut bindShortcutTask = new TaskAboutBindShortcut();
                bindShortcutTask.task();

                System.out.println("afterInitScene() 执行结束。时间："
                        + (System.currentTimeMillis() - CentralDatabase.startTime) + "ms");
            }
        });
    }

    private class TaskAboutBindShortcut {
        public TaskAboutBindShortcut() {
            super();
        }

        /**
         * 此方法为包可见
         *
         * @since 2021-9-28
         */
        void task() {
            this.bindShortcuts();
        }

        @SneakyThrows
        private void bindShortcuts() {
            var container = CentralDatabase.getContainer();
            List<Button> functionButtons;
            /**
             * 同步请求资源
             *
             * 此过程不能位于主线程执行，原因可能是主线程优先级太大，
             * 一直尝试获取资源会使其它线程得不到运行，从而导致死锁
             */
            do {
                functionButtons = (List<Button>) container.get("functionButtons");
                Thread.sleep(100);
            } while (functionButtons == null);

            for (var button : functionButtons) {
                switch (button.getText()) {
                    case "❮":
                        // 没有设定的必要
                        break;
                    case "❯":
                        // 没有设定的必要
                        break;
                    case "✅":
                        // 此快捷键系统已自动设定为 “Ctrl+A”，无需自行设定
                        break;
                    case "☒":
                        // 没有设定的必要
                        break;
                    case "⟲":
                        // 此快捷键系统已自动设定为 “Ctrl+Z”，无需自行设定
                        break;
                    case "⟳":
                        this.bindShortcut(button, KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
                        break;
                }
            }

            var concealedButtons = (List<Button>) container.get("concealedFunctions");
            for (var button : concealedButtons) {
                switch (button.getText()) {
                    case "focus":
                        // 因为无法设置只有 Modifier，而没有 KeyCode 的快捷键，所以此键无法方便的设置
                        break;
                }
            }
        }

        /**
         * 注意：设置快捷键时，无法设置只有 Modifier，而没有 KeyCode 的快捷键。
         * 且 KeyCode 不能设为存在于 KeyCombination 中的键
         *
         * @since 2021-9-28
         */
        private void bindShortcut(Button button, KeyCode keyCode, Modifier... modifiers) {
            KeyCombination kc = new KeyCodeCombination(keyCode, modifiers);
            Platform.runLater(() -> {
                scene.getAccelerators().put(kc, () -> button.getOnAction().handle(null));
            });
        }
    }
}

