package org.wangpai.calculator.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
//import org.wangpai.calculator.CalculatorApplication;

import java.io.IOException;

/**
 * 此类必须位于一个独立的文件，而不能各其它 public 类放一起
 *
 * @since 2021年9月24日
 */
public class CalculatorMainFaceApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                CalculatorMainFaceApp.class.getResource("CalculatorMainFace.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

//        /**
//         * 注意：不要使用"new ImageIcon(String path)"来获取图片。
//         * 因为它的读取路径是以 System.getProperty("user.dir") 为基路径，
//         * 而这个路径是本工程的绝对路径，还不是本模块的绝对路径！
//         * 此外，这个路径也会随模块打成 JAR 包而改变
//         *
//         * 方法 getResourceAsStream 的路径是以资源目录 resources 为基准的
//         */
//        var imageStream = Application.class.getClassLoader()
//                .getResourceAsStream("image/calculatorImage.png");
//        stage.getIcons().add(new Image(imageStream));
//        stage.setTitle("王牌计算器");
    }

    public static void main(String[] args) {
        launch();
    }
}
