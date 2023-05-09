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
import src.javafxmvc.model.domain.Funcionario;

import java.sql.Connection;

import src.javafxmvc.model.dao.FuncionarioDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPaneFuncController implements Initializable {

    @FXML
    private TableView<Funcionario> tableViewFuncionarios;

    @FXML
    private TableColumn<Funcionario, String> tableColumnFuncNome;

    @FXML
    private TableColumn<Funcionario, String> tableColumnFuncCpf;

    @FXML
    private TableColumn<Funcionario, String> tableColumnFuncEndereco;

    @FXML
    private TableColumn<Funcionario, String> tableColumnFuncTipo;

    @FXML
    private TableColumn<Funcionario, String> tableColumnFuncUsuario;

    @FXML
    private Button buttonRemover;

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonCadastrar;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    private List<Funcionario> listFuncionarios;
    private ObservableList<Funcionario> observableListFuncionarios;
        

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        funcionarioDAO.setConnection(connection);
        
        listFuncionarios = funcionarioDAO.list();

        loadTableViewFuncionarios();
    }

    public void loadTableViewFuncionarios() {
        tableColumnFuncNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnFuncCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tableColumnFuncEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tableColumnFuncTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tableColumnFuncUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        
        /* Populate TableView */
        observableListFuncionarios = FXCollections.observableArrayList(listFuncionarios);
        tableViewFuncionarios.setItems(observableListFuncionarios);
    }

    @FXML
    public void handleButtonRegister() throws IOException {
        Funcionario funcionario = new Funcionario();
        boolean buttonConfirmarClicked = showDialog(funcionario, 0);
        if (buttonConfirmarClicked) {
            funcionarioDAO.insert(funcionario);
            loadTableViewFuncionarios();
        }
    }

    @FXML
    public void handleButtonUpdate() throws IOException {
        Funcionario funcionario = tableViewFuncionarios.getSelectionModel().getSelectedItem(); //Obtendo funcionario selecionado
        if (funcionario != null) {
            boolean buttonConfirmarClicked = showDialog(funcionario, 1);
            if (buttonConfirmarClicked) {
                funcionarioDAO.update(funcionario);
                loadTableViewFuncionarios();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um funcionario na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonDelete() throws IOException {
        Funcionario funcionario = tableViewFuncionarios.getSelectionModel().getSelectedItem();
        if (funcionario != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Tem certeza que deseja remover este funcionário?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                funcionarioDAO.delete(funcionario);
                loadTableViewFuncionarios();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um funcionario na Tabela!");
            alert.show();
        }
    }

    public boolean showDialog(Funcionario funcionario, int button) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneFuncDialogController.class.getResource("../view/AnchorPaneFuncDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Funcionario");  

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o funcionario no Controller.
        AnchorPaneFuncDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setTitle(button);
        controller.setFuncionario(funcionario);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmed();

    }
}
