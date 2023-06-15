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
    // Declaração dos elementos da interface gráfica
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

    // Listas e objetos para manipulação dos dados
    private List<Funcionario> listFuncionarios;
    private ObservableList<Funcionario> observableListFuncionarios;
        
    // Método de inicialização do controller, executado quando a view é carregada
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configura as conexões com o banco de dados
        funcionarioDAO.setConnection(connection);

        // Carrega os funcionários na table view
        loadTableViewFuncionarios();
    }

    // Método que carrega a tableview funcionários
    public void loadTableViewFuncionarios() {
        // Define as propriedades das colunas da tabela
        tableColumnFuncNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnFuncCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tableColumnFuncEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tableColumnFuncTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tableColumnFuncUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        
        // Obtém a lista de funcionários do banco de dados
        listFuncionarios = funcionarioDAO.list();

        // Popula a TableView 
        observableListFuncionarios = FXCollections.observableArrayList(listFuncionarios);
        tableViewFuncionarios.setItems(observableListFuncionarios);
    }

    // Método que chama o diálogo para a inserção
    @FXML
    public void handleButtonRegister() throws IOException {
        Funcionario funcionario = new Funcionario();
        // Exibe uma caixa de diálogo para inserir os dados do funcionário
        boolean buttonConfirmarClicked = showDialog(funcionario, 0);
        if (buttonConfirmarClicked) {
            // Salva o funcionário no banco de dados
            funcionarioDAO.insert(funcionario);
            showConfirmationAlert(0);
            loadTableViewFuncionarios();
        }
    }

    // Método que chama o diálogo para a atualização 
    @FXML
    public void handleButtonUpdate() throws IOException {
        // Obtendo funcionário selecionado
        Funcionario funcionario = tableViewFuncionarios.getSelectionModel().getSelectedItem(); //Obtendo funcionario selecionado
        if (funcionario != null) {
            // Exibe uma caixa de diálogo para atualizar os dados do funcionário
            boolean buttonConfirmarClicked = showDialog(funcionario, 1);
            if (buttonConfirmarClicked) {
                funcionarioDAO.update(funcionario);
                showConfirmationAlert(1);
                loadTableViewFuncionarios();
            }
        // Exibe um alerta caso o funcionário seja null
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um funcionário na tabela!");
            alert.show();
        }
    }

    // Método responsável por apagar um funcionário 
    @FXML
    public void handleButtonDelete() throws IOException {
        // Obtendo funcionário selecionado
        Funcionario funcionario = tableViewFuncionarios.getSelectionModel().getSelectedItem();
        if (funcionario != null) {
            // Exibe um alert de confirmação
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Tem certeza que deseja remover este funcionário?");
            // Aguarda a resposta do usuário 
            Optional<ButtonType> result = alert.showAndWait();
            // Apaga o funcionário caso seja confirmado 
            if (result.isPresent() && result.get() == ButtonType.OK) {
                funcionarioDAO.delete(funcionario);
                showConfirmationAlert(2);
                loadTableViewFuncionarios();
            }
        // Exibe um alerta caso o funcionário seja null
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um funcionario na tabela!");
            alert.show();
        }
    }

    // Método responsável por carregar o diálogo
    public boolean showDialog(Funcionario funcionario, int button) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneFuncDialogController.class.getResource("../view/AnchorPaneFuncDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Funcionario");  

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        
        AnchorPaneFuncDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        // Setando o título de acordo com o botão selecionado
        controller.setTitle(button);
        // Setando o funcionário no Controller.
        controller.setFuncionario(funcionario);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmed();

    }

    // Método responsável por carregar o alert de sucesso para as operações
    public void showConfirmationAlert(int button) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String title;
        String content;

        // Define de acordo com o botão, o título e o conteúdo do alert
        switch (button) {
            case 0: // Caso o botão seja de inserir
                title = "Funcionário Cadastrado";
                content = "Funcionário cadastrado com sucesso!";
                break;
            case 1: // Caso o botão seja de alterar
                title = "Funcionário Alterado";
                content = "Funcionário alterado com sucesso!";
                break;
            case 2: // Caso o botão seja de apagar
                title = "Funcionário Apagado";
                content = "Funcionário apagado com sucesso!";
                break;
            default:
                title = "Operação Concluída";
                content = "Operação concluída com sucesso!";
                break;
        }
        
        // Setando o título e o conteúdo
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Exibe o alert
        alert.showAndWait();

        // Aguarda a confirmação do usuário para fechar
        if (alert.getResult() == ButtonType.OK) {
            alert.close();
        }
    }
}
