package org.jlereback.knitknot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 450);

        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("KnitKnot");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("JavaDuke.png"))));


        startPopup(controller, stage);

        stage.show();
    }

    public void startPopup(Controller controller, Stage stage) throws IOException {
        Stage popupStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("popupView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 200, 229);

        controller.setPopupStage(popupStage);

        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(stage.getOwner());

        popupStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}