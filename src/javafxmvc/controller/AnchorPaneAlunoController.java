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

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    private List<Aluno> listAlunos;
    private ObservableList<Aluno> observableListAlunos;
        

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alunoDAO.setConnection(connection);
        
        listAlunos = alunoDAO.list();

        loadTableViewAlunos();
    }

    public void loadTableViewAlunos() {
        tableColumnAlunoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnAlunoCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tableColumnAlunoEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tableColumnAlunoGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        tableColumnAlunoPontos.setCellValueFactory(new PropertyValueFactory<>("pontos"));
        tableColumnAlunoPlano.setCellValueFactory(new PropertyValueFactory<>("plano_id"));
        
        /* Populate TableView */
        observableListAlunos = FXCollections.observableArrayList(listAlunos);
        tableViewAlunos.setItems(observableListAlunos);
    }

}
