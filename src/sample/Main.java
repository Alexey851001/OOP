package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private String fxmlResourcePath = "sample.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(fxmlResourcePath));
        primaryStage.setTitle("Lab2");
        primaryStage.setScene(new Scene(root, 650, 450));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
