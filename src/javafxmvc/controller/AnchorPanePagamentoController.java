package src.javafxmvc.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import src.javafxmvc.model.domain.Pagamento;

import java.sql.Connection;

import src.javafxmvc.model.dao.PagamentoDAO;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPanePagamentoController implements Initializable {

    @FXML
    private TableView<Pagamento> tableViewPagamentos;

    @FXML
    private TableColumn<Pagamento, String> tableColumnPagData;

    @FXML
    private TableColumn<Pagamento, String> tableColumnPagValor;

    @FXML
    private TableColumn<Pagamento, String> tableColumnPagIdAluno;

    @FXML
    private TableColumn<Pagamento, String> tableColumnPagNomeAluno;


    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();


    private List<Pagamento> listPagamentos;
    private ObservableList<Pagamento> observableListPagamentos;
        

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pagamentoDAO.setConnection(connection);
        alunoDAO.setConnection(connection);

        listPagamentos = pagamentoDAO.list();

        loadTableViewPagamentos();
    }

    public void loadTableViewPagamentos() {
        tableColumnPagData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnPagValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tableColumnPagIdAluno.setCellValueFactory(new PropertyValueFactory<>("aluno_id"));
        tableColumnPagNomeAluno.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pagamento, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pagamento, String> param) {
                String nomeAluno = alunoDAO.findById(param.getValue().getAluno_id()).getNome();
                return new SimpleStringProperty(nomeAluno);
            }
        });
        

        /* Populate TableView */
        observableListPagamentos = FXCollections.observableArrayList(listPagamentos);
        tableViewPagamentos.setItems(observableListPagamentos);
    }

}
