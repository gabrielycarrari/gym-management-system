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
import src.javafxmvc.model.domain.Aluno;

import java.sql.Connection;

import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPaneAlunoController implements Initializable {

    @FXML
    private TableView<Aluno> tableViewAlunos;

    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoNome;

    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoCpf;

    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoEndereco;

    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoGenero;

    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoPontos;

    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoPlano;

    @FXML
    private Button buttonRemover;

    @FXML
    private Button buttonAlterar;

    @FXML
    private Button buttonCadastrar;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    private List<Aluno> listAlunos;
    private ObservableList<Aluno> observableListAlunos;
        

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alunoDAO.setConnection(connection);
        
        loadTableViewAlunos();
    }

    public void loadTableViewAlunos() {
        tableColumnAlunoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnAlunoCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tableColumnAlunoEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tableColumnAlunoGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        tableColumnAlunoPontos.setCellValueFactory(new PropertyValueFactory<>("pontos"));
        tableColumnAlunoPlano.setCellValueFactory(new PropertyValueFactory<>("plano_id"));

        listAlunos = alunoDAO.list();
        
        /* Populate TableView */
        observableListAlunos = FXCollections.observableArrayList(listAlunos);
        tableViewAlunos.setItems(observableListAlunos);
    }

    @FXML
    public void handleButtonRegister() throws IOException {
        Aluno aluno = new Aluno();

        boolean buttonConfirmarClicked = showDialog(aluno, 0);
        
        if (buttonConfirmarClicked) {
            alunoDAO.insert(aluno);
            showConfirmationAlert(0);
            loadTableViewAlunos();
        }
    }

    @FXML
    public void handleButtonUpdate() throws IOException {
        Aluno aluno = tableViewAlunos.getSelectionModel().getSelectedItem(); // Obtendo aluno selecionado
        if (aluno != null) {
            boolean buttonConfirmarClicked = showDialog(aluno, 1);
            if (buttonConfirmarClicked) {
                alunoDAO.update(aluno);
                showConfirmationAlert(1);
                loadTableViewAlunos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um aluno na tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonDelete() throws IOException {
        // Verificar se existe este aluno está associado a algum treino
        
        Aluno aluno = tableViewAlunos.getSelectionModel().getSelectedItem();
        if (aluno != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Tem certeza que deseja remover este aluno?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                alunoDAO.delete(aluno);
                showConfirmationAlert(2);
                loadTableViewAlunos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um aluno na tabela!");
            alert.show();
        }
    }

    public boolean showDialog(Aluno aluno, int button) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneFuncDialogController.class.getResource("../view/AnchorPaneAlunoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Aluno");  

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o aluno no Controller.
        AnchorPaneAlunoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setTitle(button);
        controller.setAluno(aluno);

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
                title = "Aluno Cadastrado";
                content = "Aluno cadastrado com sucesso!";
                break;
            case 1:
                title = "Aluno Alterado";
                content = "Aluno alterado com sucesso!";
                break;
            case 2:
                title = "Aluno Apagado";
                content = "Aluno apagado com sucesso!";
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
