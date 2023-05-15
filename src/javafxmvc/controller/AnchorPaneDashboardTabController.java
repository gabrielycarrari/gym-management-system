package src.javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class AnchorPaneDashboardTabController implements Initializable {
    
    @FXML
    private AnchorPane anchorPaneGraficos;
    @FXML
    private AnchorPane anchorPaneRelatorios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane a = new AnchorPane();
        AnchorPane b = new AnchorPane();
        try {
            a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/AnchorPaneGrafico.fxml"));
            b = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/AnchorPaneRelatorio.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        anchorPaneGraficos.getChildren().setAll(a);
        anchorPaneRelatorios.getChildren().setAll(b);
    }
}
