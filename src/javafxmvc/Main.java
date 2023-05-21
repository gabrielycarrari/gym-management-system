package src.javafxmvc;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/AnchorPaneVenda.fxml"));
        
        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Cantina");
        stage.getIcons().add(new Image("src/javafxmvc/images/gym-icon.png"));
        stage.setHeight(720);
        stage.setWidth(1280);
        stage.setResizable(false);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        scene.setRoot(fxmlLoader.load());
    }

    public static void main(String[] args) {
        launch(args);
    }    
}