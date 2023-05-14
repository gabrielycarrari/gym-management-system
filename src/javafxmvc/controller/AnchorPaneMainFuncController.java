package src.javafxmvc.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import src.javafxmvc.Main;

public class AnchorPaneMainFuncController implements Initializable {

    @FXML
    private Button buttonDashboard;

    @FXML
    private Button buttonAlunos;

    @FXML
    private Button buttonTreinos;

    @FXML
    private Button buttonPagamentos;

    @FXML
    private Button buttonLogOut;

    @FXML 
    private ImageView imageViewLogo;

    @FXML
    private AnchorPane anchorPane;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImages();

        try {
            switchAnchorPaneDashboard();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToLoginFunc() throws IOException {
        Main.setRoot("view/VBoxLoginFunc");
    }

    public void loadImages() {
        try {
            Image image = new Image(new FileInputStream("src/javafxmvc/images/logo-icon-brand.png"));
            imageViewLogo.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        setImageToButton(buttonDashboard, "icon-dashboard.png");
        setImageToButton(buttonAlunos, "icon-alunos.png");
        setImageToButton(buttonTreinos, "icon-treinos.png");
        setImageToButton(buttonPagamentos, "icon-pagamentos.png");
        setImageToButton(buttonLogOut, "icon-logout.png");
    }

    public void setImageToButton(Button button, String img) {
        Image image = new Image("src/javafxmvc/images/" + img);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(18);
        imageView.setFitHeight(18);
        imageView.setEffect(null); // no filter

        button.setGraphic(imageView);

        // Add an event listener for mouse enter
        button.setOnMouseEntered(e -> {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(1.0);
            imageView.setEffect(colorAdjust);
        });

        // Add an event listener for mouse exit
        button.setOnMouseExited(e -> {
            imageView.setEffect(null); // no filter
        });
    }

    public void switchAnchorPaneDashboard() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/AnchorPaneDashboard.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    public void switchAnchorPaneAluno() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/AnchorPaneAluno.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    public void switchAnchorPaneTreino() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/AnchorPaneTreino.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    public void switchAnchorPanePagamento() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/AnchorPanePagamento.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    public void switchAnchorPaneCheckOut() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/AnchorPaneCheckOut.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    public void switchAnchorPaneCheckIn() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/AnchorPaneCheckIn.fxml"));
        anchorPane.getChildren().setAll(a);
    }
}
