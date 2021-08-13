package org.wangpai.calculator.view.input;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 此类必须位于一个独立的文件，而不能各其它 public 类放一起
 *
 * @since 2021年9月24日
 */
public class InputBoxApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                InputBoxApp.class.getResource("InputBox.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
