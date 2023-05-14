package src.javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import src.javafxmvc.model.domain.CheckOut;

import java.sql.Connection;

import src.javafxmvc.model.dao.CheckOutDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPaneCheckOutController implements Initializable {

    @FXML
    private TableView<CheckOut> tableViewCheckOuts;

    @FXML
    private TableColumn<CheckOut, String> tableColumnCOData;

    @FXML
    private TableColumn<CheckOut, String> tableColumnCOHora;

    @FXML
    private TableColumn<CheckOut, String> tableColumnCOIdCI;


    @FXML
    private Button buttonRemover;

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonCadastrar;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CheckOutDAO checkoutDAO = new CheckOutDAO();

    private List<CheckOut> listCheckOuts;
    private ObservableList<CheckOut> observableListCheckOuts;
        

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkoutDAO.setConnection(connection);

        loadTableViewCheckOuts();
    }

    public void loadTableViewCheckOuts() {
        tableColumnCOData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnCOHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        tableColumnCOIdCI.setCellValueFactory(new PropertyValueFactory<>("checkin_id"));
        
        listCheckOuts = checkoutDAO.list();

        /* Populate TableView */
        observableListCheckOuts = FXCollections.observableArrayList(listCheckOuts);
        tableViewCheckOuts.setItems(observableListCheckOuts);
    }

    @FXML
    public void handleButtonRegister() throws IOException {
        CheckOut checkout = new CheckOut();
        boolean buttonConfirmarClicked = showDialog(checkout, 0);
        if (buttonConfirmarClicked) {
            checkoutDAO.insert(checkout);
            showConfirmationAlert(0);
            loadTableViewCheckOuts();
        }
    }

    @FXML
    public void handleButtonUpdate() throws IOException {
        CheckOut checkout = tableViewCheckOuts.getSelectionModel().getSelectedItem(); //Obtendo checkout selecionado
        if (checkout != null) {
            boolean buttonConfirmarClicked = showDialog(checkout, 1);
            if (buttonConfirmarClicked) {
                checkoutDAO.update(checkout);
                showConfirmationAlert(1);
                loadTableViewCheckOuts();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um checkout na tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonDelete() throws IOException {        
        CheckOut checkout = tableViewCheckOuts.getSelectionModel().getSelectedItem();
        if (checkout != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Tem certeza que deseja remover este checkout?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                checkoutDAO.delete(checkout);
                showConfirmationAlert(2);
                loadTableViewCheckOuts();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um checkout na tabela!");
            alert.show();
        }
    }

    public boolean showDialog(CheckOut checkout, int button) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneFuncDialogController.class.getResource("../view/AnchorPaneCheckOutDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("CheckOut");  

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o checkout no Controller.
        AnchorPaneCheckOutDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setTitle(button);
        controller.setCheckOut(checkout);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmed();

    }

    public void showConfirmationAlert(int button) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String title;
        String content;

        switch (button) {
            case 0:
                title = "CheckOut Cadastrado";
                content = "CheckOut cadastrado com sucesso!";
                break;
            case 1:
                title = "CheckOut Alterado";
                content = "CheckOut alterado com sucesso!";
                break;
            case 2:
                title = "CheckOut Apagado";
                content = "CheckOut apagado com sucesso!";
                break;
            default:
                title = "Operação Concluída";
                content = "Operação concluída com sucesso!";
                break;
        }
        
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
}
