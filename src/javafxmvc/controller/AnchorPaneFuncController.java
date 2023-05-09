package src.javafxmvc.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
}
