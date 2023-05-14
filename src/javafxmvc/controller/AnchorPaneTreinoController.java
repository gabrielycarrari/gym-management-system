package src.javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import src.javafxmvc.model.dao.TreinoDAO;
import src.javafxmvc.model.dao.TreinoExercicioDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;
import src.javafxmvc.model.domain.Exercicio;
import src.javafxmvc.model.domain.Treino;

public class AnchorPaneTreinoController implements Initializable {

    @FXML
    private TableView<Treino> tableViewTreino;

    @FXML
    private TableColumn<Treino, String> tableColumnAluno;

    @FXML
    private TableColumn<Treino, String> tableColumnDataInicio;

    @FXML
    private TableColumn<Treino, String> tableColumnDataFinal;

    @FXML
    private TableColumn<Treino, String> tableColumnFuncionario;

    @FXML
    private Button buttonInserir; 

    @FXML
    private Button buttonAlterar; 

    @FXML
    private Button buttonRemover; 

    @FXML
    private Button buttonRelatorio; 

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final TreinoDAO treinoDAO = new TreinoDAO();
    private final TreinoExercicioDAO treinoExercicioDAO = new TreinoExercicioDAO();


    private List<Treino> listTreino;
    private ObservableList<Treino> observableListTreino;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        treinoDAO.setConnection(connection);
        
        listTreino = treinoDAO.list();

        loadTableViewTreino();
    }

    public void loadTableViewTreino(){
        tableColumnAluno.setCellValueFactory(new PropertyValueFactory<>("alunoNome"));
        tableColumnDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        tableColumnDataFinal.setCellValueFactory(new PropertyValueFactory<>("dataFinal"));
        tableColumnFuncionario.setCellValueFactory(new PropertyValueFactory<>("funcionarioNome"));
        
        observableListTreino = FXCollections.observableArrayList(listTreino);
        
        tableViewTreino.setItems(observableListTreino);
    }

    @FXML
    public void handleButtonInserir() throws IOException {
        boolean confirmInsert = showInserirTreino();
        
        if(confirmInsert){
            listTreino = treinoDAO.list();
            loadTableViewTreino();
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException {
        Treino treino = tableViewTreino.getSelectionModel().getSelectedItem();
        boolean confirmInsert = false;
        if(treino != null){
            confirmInsert = showAlterarTreino(treino);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, escolha o treino que deseja atualizar", null);
            alert.show();
        }
        
        if(confirmInsert){
            listTreino = treinoDAO.list();
            loadTableViewTreino();
        }
    }

    @FXML
    public void handleButtonRemover() {
        Treino treino = tableViewTreino.getSelectionModel().getSelectedItem();
        if(treino != null){
            try {
                connection.setAutoCommit(false);
                treinoExercicioDAO.setConnection(connection);
                treinoExercicioDAO.deleteByIdTreino(treino);
                treinoDAO.delete(treino);
                connection.commit();

                listTreino = treinoDAO.list();
                loadTableViewTreino();
            } catch (Exception ex) {
                try {
                    connection.rollback();
                    listTreino = treinoDAO.list();
                    loadTableViewTreino();
                } catch (SQLException ex1) {
                    Logger.getLogger(AnchorPaneTreinoDialogController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(AnchorPaneTreinoDialogController.class.getName()).log(Level.SEVERE, null, ex);            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, escolha o treino que deseja remover", null);
            alert.show();
        }
    }

    @FXML
    public void handleButtonRelatorio() throws IOException  {
       showRelatorioTreino();
    }

    public boolean showInserirTreino() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneTreinoDialogController.class.getResource("../view/AnchorPaneTreinoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastrar novo treino");
        dialogStage.setResizable(false);

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AnchorPaneTreinoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();
        
        return controller.isButtonConfirmar();
    }

    public boolean showAlterarTreino(Treino treino) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        System.out.println("??");
        loader.setLocation(AnchorPaneTreinoDialogAlterarController.class.getResource("../view/AnchorPaneTreinoDialogAlterar.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        System.out.println("??");
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Alterar treino");
        dialogStage.setResizable(false);
        System.out.println("skjdksd");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AnchorPaneTreinoDialogAlterarController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setTreino(treino);

        dialogStage.showAndWait();
        
        return controller.isButtonConfirmar();
    }

    public boolean showRelatorioTreino() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AnchorPaneTreinoDialogRelatorioController.class.getResource("../view/AnchorPaneTreinoDialogRelatorio.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Relatorio treino");
        dialogStage.setResizable(false);

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AnchorPaneTreinoDialogRelatorioController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();
        
        return controller.isButtonConfirmar();
    }

}
