package org.wangpai.calculator.view.wrapped;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCombination.Modifier;
import java.util.List;
import org.wangpai.calculator.model.universal.CentralDatabase;

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
        TaskAboutBindShortcut bindShortcutTask = new TaskAboutBindShortcut();
        bindShortcutTask.task();
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

        private void bindShortcuts() {
            var container = CentralDatabase.getContainer();
            var functionButtons = (List<Button>) container.get("functionButtons");
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
            scene.getAccelerators().put(kc, () -> button.getOnAction().handle(null));
        }
    }
}

