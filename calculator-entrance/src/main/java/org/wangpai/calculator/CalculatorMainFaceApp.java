package org.wangpai.calculator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import org.wangpai.calculator.model.universal.CentralDatabase;
import org.wangpai.calculator.view.mainface.CalculatorMainFaceLinker;
import org.wangpai.calculator.view.wrapped.SceneWrapped;

/**
 * 此类必须位于一个独立的文件，必须声明为 public 类
 *
 * 本项目没有使用 Java 的模块化，本类的方法 main 不能直接作为程序的入口类
 *
 * @since 2021年9月24日
 */
public class CalculatorMainFaceApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /**
         * 注意：此路径是以 resource 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resource/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(
                CalculatorMainFaceLinker.class.getResource("CalculatorMainFace.fxml"));
        Scene scene = new SceneWrapped(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        /**
         * 注意：不要使用 “new ImageIcon(String path)” 来获取图片。
         * 因为它的读取路径是以 System.getProperty("user.dir") 为基路径，
         * 而这个路径是本工程的绝对路径，还不是本模块的绝对路径！
         * 此外，这个路径也会随模块打成 JAR 包而改变
         *
         * 方法 getResourceAsStream 的路径是以资源目录 resources 为基准的，
         * 且不受模块的限制。这于 xxx.class 中 xxx 是哪个模块的哪个类无关
         */
        var imageStream = CalculatorMainFaceApp.class.getClassLoader()
                .getResourceAsStream("image/calculatorImage.png");
        stage.getIcons().add(new Image(imageStream));
        stage.setTitle("王牌计算器");

        stage.setOnCloseRequest(event -> {
            CentralDatabase.multithreadingClosed();
            Platform.exit();
        });

        System.out.println("UI 启动用时：" + (System.currentTimeMillis() - CentralDatabase.startTime) + "ms");
    }

    public static void main(String[] args) {
        launch();
    }
}
