package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
        primaryStage.getIcons().add(new Image("dictionary-512.png"));
        primaryStage.setTitle("Dictionary");
        primaryStage.setScene(new Scene(root, 750, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
