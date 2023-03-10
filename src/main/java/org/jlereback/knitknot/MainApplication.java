package org.jlereback.knitknot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 570, 472);

        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("KnitKnot");
        scene.getStylesheets().add(String.valueOf(MainApplication.class.getResource("main.css")));
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("JavaDuke.png"))));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}