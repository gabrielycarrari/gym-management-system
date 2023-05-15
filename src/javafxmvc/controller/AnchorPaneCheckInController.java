package src.javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
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
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.dao.CheckInDAO;
import src.javafxmvc.model.dao.CheckOutDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;
import src.javafxmvc.model.domain.Aluno;
import src.javafxmvc.model.domain.CheckIn;


public class AnchorPaneCheckInController implements Initializable {

    @FXML
    private TableView<CheckIn> tableViewCheckIns;

    @FXML
    private TableColumn<CheckIn, String> tableColumnCIData;

    @FXML
    private TableColumn<CheckIn, String> tableColumnCIHora;

    @FXML
    private TableColumn<CheckIn, String> tableColumnCIIdAluno;

    @FXML
    private TableColumn<CheckIn, String> tableColumnCINomeAluno;
    
    
    @FXML
    private Button buttonRemover;

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonCadastrar;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CheckInDAO checkinDAO = new CheckInDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final CheckOutDAO checkOutDAO = new CheckOutDAO();

    private List<CheckIn> listCheckIns;
    private ObservableList<CheckIn> observableListCheckIns;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkinDAO.setConnection(connection);
        alunoDAO.setConnection(connection);
        checkOutDAO.setConnection(connection);

        loadTableViewCheckIns();
    }

    public void loadTableViewCheckIns() {
        tableColumnCIData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnCIHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        tableColumnCIIdAluno.setCellValueFactory(new PropertyValueFactory<>("aluno_id"));
        tableColumnCINomeAluno.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CheckIn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CheckIn, String> param) {
                String nomeAluno = alunoDAO.findById(param.getValue().getAluno_id()).getNome();
                return new SimpleStringProperty(nomeAluno);
            }
        });

        listCheckIns = checkinDAO.list();

        /* Populate TableView */
        observableListCheckIns = FXCollections.observableArrayList(listCheckIns);
        tableViewCheckIns.setItems(observableListCheckIns);
    }

    @FXML
    public void handleButtonRegister() throws IOException {
        CheckIn checkin = new CheckIn();
        boolean buttonConfirmarClicked = showDialog(checkin, 0);
        if (buttonConfirmarClicked) {
            
            Aluno aluno = alunoDAO.findObjectById(checkin.getAluno_id());
            Integer pontos = aluno.getPontos() + 5;
            aluno.setPontos(pontos);

            alunoDAO.update(aluno);

            checkinDAO.insert(checkin);
            showConfirmationAlert(0);
            loadTableViewCheckIns();
        }
    }

    @FXML
    public void handleButtonUpdate() throws IOException {
        CheckIn checkin = tableViewCheckIns.getSelectionModel().getSelectedItem(); //Obtendo checkin selecionado
        if (checkin != null) {
            boolean buttonConfirmarClicked = showDialog(checkin, 1);
            if (buttonConfirmarClicked) {
                checkinDAO.update(checkin);
                showConfirmationAlert(1);
                loadTableViewCheckIns();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um checkin na tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonDelete() throws IOException {        
        CheckIn checkin = tableViewCheckIns.getSelectionModel().getSelectedItem();

        if (checkin != null) {
            if(checkOutDAO.search(checkin.getIdCheckIn())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Existe um check-out realizado por este aluno nesta data. É preciso apaga-lo antes de excluir o check-in!");
                alert.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Tem certeza que deseja remover este checkin?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    checkinDAO.delete(checkin);
                    showConfirmationAlert(2);
                    loadTableViewCheckIns();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um checkin na tabela!");
            alert.show();
        }
    }

    public boolean showDialog(CheckIn checkin, int button) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneFuncDialogController.class.getResource("../view/AnchorPaneCheckInDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("CheckIn");  

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o checkin no Controller.
        AnchorPaneCheckInDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setTitle(button);
        controller.setCheckIn(checkin);

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
                title = "CheckIn Cadastrado";
                content = "CheckIn cadastrado com sucesso!";
                break;
            case 1:
                title = "CheckIn Alterado";
                content = "CheckIn alterado com sucesso!";
                break;
            case 2:
                title = "CheckIn Apagado";
                content = "CheckIn apagado com sucesso!";
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
