package src.javafxmvc.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import src.javafxmvc.model.domain.Aluno;
import src.javafxmvc.model.domain.Exercicio;
import src.javafxmvc.model.domain.Funcionario;
import src.javafxmvc.model.domain.Treino;
import src.javafxmvc.model.domain.TreinoExercicio;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.dao.FuncionarioDAO;
import src.javafxmvc.model.dao.TreinoDAO;
import src.javafxmvc.model.dao.TreinoExercicioDAO;
import src.javafxmvc.model.dao.ExercicioDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;

public class AnchorPaneTreinoDialogRelatorioController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ComboBox comboBoxMusculo;

    @FXML
    private TableView<TreinoExercicio> tableViewExercicio;

    @FXML
    private TableColumn<TreinoExercicio, String> tableColumnExercicio;

    @FXML
    private TableColumn<TreinoExercicio, String> tableColumnAluno;

    @FXML
    private TableColumn<TreinoExercicio, String> tableColumnFuncionario;

    @FXML
    private TableColumn<TreinoExercicio, String> tableColumnDataInicio;

    @FXML
    private TableColumn<TreinoExercicio, String> tableColumnDataFinal;

    @FXML
    private Label labelErroMusculo;

    @FXML
    private Button buttonBuscar; 
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ExercicioDAO exercicioDAO = new ExercicioDAO();
    private final TreinoExercicioDAO treinoExercicioDAO = new TreinoExercicioDAO();

    private Stage dialogStage;

    private List<Exercicio> listExercicios;
    private List<TreinoExercicio> listTreinoExercicios;
    private ObservableList<Exercicio> observableListExercicios;
    private ObservableList<TreinoExercicio> observableListTreinoExercicios;

    private Treino treino;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        treinoExercicioDAO.setConnection(connection);
        exercicioDAO.setConnection(connection);

        listExercicios = new ArrayList<>();
        listTreinoExercicios = new ArrayList<>();

        loadComboboxExercicio();
    }

    @FXML
    private void handleButtonBuscar() {
        Exercicio exercicio = (Exercicio) comboBoxMusculo.getSelectionModel().getSelectedItem();
        if(validate()){
            listTreinoExercicios = treinoExercicioDAO.findByMusculo(exercicio);
            
            tableColumnExercicio.setCellValueFactory(new PropertyValueFactory<>("exercicioTipo"));
            tableColumnAluno.setCellValueFactory(new PropertyValueFactory<>("treinoNomeAluno"));
            tableColumnFuncionario.setCellValueFactory(new PropertyValueFactory<>("treinoNomeFuncionario"));
            tableColumnDataInicio.setCellValueFactory(new PropertyValueFactory<>("treinoDataInicio"));
            tableColumnDataFinal.setCellValueFactory(new PropertyValueFactory<>("treinoDataFinal"));
            
            observableListTreinoExercicios = FXCollections.observableArrayList(listTreinoExercicios);
            
            tableViewExercicio.setItems(observableListTreinoExercicios);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, escolha o exercicio que deseja acrescentar", null);
            alert.show();
        }
        
    }
    
    @FXML
    private void handleButtonImprimir() {
        // Lógica para abrir o pdf
        
    }

    private void loadComboboxExercicio(){
        listExercicios = exercicioDAO.list();
        observableListExercicios = FXCollections.observableArrayList(listExercicios);
        
        comboBoxMusculo.setItems(observableListExercicios);
    }

    private void loadTableViewExercicio(){
        // tableColumnExercicio.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        // tableColumnAluno.setCellValueFactory(new PropertyValueFactory<>("musculo"));
        // tableColumnFuncionario.setCellValueFactory(new PropertyValueFactory<>("numSeries"));
        // tableColumnDataInicio.setCellValueFactory(new PropertyValueFactory<>("numRepeticoes"));
        // tableColumnDataFinal.setCellValueFactory(new PropertyValueFactory<>("numRepeticoes"));
        
        // observableListExercicios = FXCollections.observableArrayList(listExercicios);
        
        // tableViewExercicio.setItems(observableListExercicios);
    }
    
    public Stage getDialogStage(){
        return dialogStage;
    }

    public void setDialogStage(Stage dialoStage){
        this.dialogStage = dialoStage;
    }
   
    public boolean isButtonConfirmar(){
        return true;
    }

    private boolean validate() {
        Exercicio exercicio = (Exercicio) comboBoxMusculo.getSelectionModel().getSelectedItem();

        int error = 0;

        if(exercicio == null){
            labelErroMusculo.setText("Selecione algum grupo muscular!");
            error++;
        }

        if(error > 0){
            return false;
        }else {
            return true;
        }
        
    }

    public void cleanErrors() {
        labelErroMusculo.setText(null);
    }
}