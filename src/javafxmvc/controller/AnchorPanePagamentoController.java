package src.javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
        
        listPagamentos = pagamentoDAO.list();

        /* Populate TableView */
        observableListPagamentos = FXCollections.observableArrayList(listPagamentos);
        tableViewPagamentos.setItems(observableListPagamentos);
    }

    @FXML
    public void handleButtonRegister() throws IOException {
        Pagamento pagamento = new Pagamento();
        boolean buttonConfirmarClicked = showDialog(pagamento, 0);
        if (buttonConfirmarClicked) {
            pagamentoDAO.insert(pagamento);
            showConfirmationAlert(0);
            loadTableViewPagamentos();
        }
    }

    @FXML
    public void handleButtonUpdate() throws IOException {
        Pagamento pagamento = tableViewPagamentos.getSelectionModel().getSelectedItem(); //Obtendo pagamento selecionado
        if (pagamento != null) {
            boolean buttonConfirmarClicked = showDialog(pagamento, 1);
            if (buttonConfirmarClicked) {
                pagamentoDAO.update(pagamento);
                showConfirmationAlert(1);
                loadTableViewPagamentos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um pagamento na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonDelete() throws IOException {
        Pagamento pagamento = tableViewPagamentos.getSelectionModel().getSelectedItem();
        if (pagamento != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Tem certeza que deseja remover este pagamento?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                pagamentoDAO.delete(pagamento);
                showConfirmationAlert(2);
                loadTableViewPagamentos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um pagamento na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonPrint() throws IOException {
        Pagamento pagamento = tableViewPagamentos.getSelectionModel().getSelectedItem(); //Obtendo pagamento selecionado
        if (pagamento != null) {
            
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um pagamento na Tabela!");
            alert.show();
        }
    }

    public boolean showDialog(Pagamento pagamento, int button) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPanePagamentoDialogController.class.getResource("../view/AnchorPanePagamentoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Pagamento");  

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o pagamento no Controller.
        AnchorPanePagamentoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage); 
        controller.setTitle(button);
        controller.setPagamento(pagamento);


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
                title = "Pagamento Registrado";
                content = "Seu pagamento foi registrado com sucesso!";
                break;
            case 1:
                title = "Pagamento Alterado";
                content = "Seu pagamento foi alterado com sucesso!";
                break;
            case 2:
                title = "Pagamento Apagado";
                content = "Seu pagamento foi apagado com sucesso!";
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