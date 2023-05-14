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

public class AnchorPaneTreinoDialogAlterarController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ComboBox comboBoxAluno;

    @FXML
    private ComboBox comboBoxFuncionario;

    @FXML
    private ComboBox comboBoxExercicio;

    @FXML
    private DatePicker datePickerDataInicio;

    @FXML
    private DatePicker datePickerDataFinal;

    @FXML
    private TableView<Exercicio> tableViewExercicio;

    @FXML
    private TableColumn<Exercicio, String> tableColumnTipo;

    @FXML
    private TableColumn<Exercicio, String> tableColumnMusculo;

    @FXML
    private TableColumn<Exercicio, String> tableColumnSerie;

    @FXML
    private TableColumn<Exercicio, String> tableColumnRepeticoes;

    @FXML
    private Label labelErroDataInicio;

    @FXML
    private Label labelErroDataFinal;

    @FXML
    private Label labelErroAluno;

    @FXML
    private Label labelErroFuncionario;

    @FXML
    private Button buttonAdicionar; 

    @FXML
    private Button buttonRemover; 

    @FXML
    private Button buttonConfirmar; 

    @FXML
    private Button buttonCancelar; 
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final TreinoDAO treinoDAO = new TreinoDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private final ExercicioDAO exercicioDAO = new ExercicioDAO();
    private final TreinoExercicioDAO treinoExercicioDAO = new TreinoExercicioDAO();

    private Stage dialogStage;

    private List<Aluno> listAlunos;
    private List<Funcionario> listFuncionarios;
    private List<Exercicio> listExercicios;
    private List<Exercicio> listExerciciosDoTreino;
    private ObservableList<Aluno> observableListAlunos;
    private ObservableList<Funcionario> observableListFuncionarios;
    private ObservableList<Exercicio> observableListExercicios;
    private ObservableList<Exercicio> observableListExerciciosDoTreino;

    private Treino treino;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        treinoDAO.setConnection(connection);
        alunoDAO.setConnection(connection);
        funcionarioDAO.setConnection(connection);
        exercicioDAO.setConnection(connection);

        listExerciciosDoTreino = new ArrayList<>();

        loadComboboxAluno();
        loadComboboxFuncionario();
        loadComboboxExercicio();
        loadTableViewExercicio();
    }

    @FXML
    private void handleAdicionarExercicio() {
        Exercicio exercicio = (Exercicio) comboBoxExercicio.getSelectionModel().getSelectedItem();
        if(exercicio != null){

            listExerciciosDoTreino.add(exercicio);
    
            tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            tableColumnMusculo.setCellValueFactory(new PropertyValueFactory<>("musculo"));
            tableColumnSerie.setCellValueFactory(new PropertyValueFactory<>("numSeries"));
            tableColumnRepeticoes.setCellValueFactory(new PropertyValueFactory<>("numRepeticoes"));
            
            observableListExerciciosDoTreino = FXCollections.observableArrayList(listExerciciosDoTreino);
            
            tableViewExercicio.setItems(observableListExerciciosDoTreino);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, escolha o exercicio que deseja acrescentar", null);
            alert.show();
        }
        
    }
    
    @FXML
    private void handleRemoverExercicio() {
        Exercicio exercicio = (Exercicio) tableViewExercicio.getSelectionModel().getSelectedItem();
        if(exercicio != null){

            listExerciciosDoTreino.remove(exercicio);
    
            tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            tableColumnMusculo.setCellValueFactory(new PropertyValueFactory<>("musculo"));
            tableColumnSerie.setCellValueFactory(new PropertyValueFactory<>("numSeries"));
            tableColumnRepeticoes.setCellValueFactory(new PropertyValueFactory<>("numRepeticoes"));
            
            observableListExerciciosDoTreino = FXCollections.observableArrayList(listExerciciosDoTreino);
            
            tableViewExercicio.setItems(observableListExerciciosDoTreino);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, escolha o exercicio que deseja remover", null);
            alert.show();
        }
        
    }

    @FXML
    private void handleButtonConfirmar() {
        cleanErrors();

        if(validate()){
            int idTreino = treino.getIdTreino();
            Treino novoTreino = new Treino();
            Aluno aluno = (Aluno) comboBoxAluno.getSelectionModel().getSelectedItem();
            Funcionario funcionario = (Funcionario) comboBoxFuncionario.getSelectionModel().getSelectedItem();

            novoTreino.setIdTreino(idTreino);
            novoTreino.setAlunoNome(aluno.getNome());
            novoTreino.setIdAluno(aluno.getIdAluno());
            novoTreino.setFuncionarioNome(funcionario.getNome());
            novoTreino.setIdFuncionario(funcionario.getIdFuncionario());
            novoTreino.setDataInicio(datePickerDataInicio.getValue());
            novoTreino.setDataFinal(datePickerDataFinal.getValue());

            // Salvar o treino no banco de dados ou realizar outra ação necessária
            try {
                connection.setAutoCommit(false);
                treinoDAO.update(treino);

                Boolean treinoValido = treinoDAO.findByPeriodo(treino.getIdTreino(), treino);

                if(treinoValido){
                    treinoExercicioDAO.setConnection(connection);
                    treinoExercicioDAO.deleteByIdTreino(treino);
                    for(int i = 0; i < listExerciciosDoTreino.size(); i++){
                        TreinoExercicio treinoExercicio = new TreinoExercicio();
                        treinoExercicio.setIdTreino(treino.getIdTreino());
                        treinoExercicio.setIdExercicio(listExerciciosDoTreino.get(i).getIdExercicio());
                        
                        treinoExercicioDAO.insert(treinoExercicio);
                    }
    
                    connection.commit();
    
                    isButtonConfirmar();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Não foi possível alterar o treino, verifique as datas e tente novamente!", null);
                    alert.show();
                }

            } catch (Exception ex) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(AnchorPaneTreinoDialogController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(AnchorPaneTreinoDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleButtonCancelar() {
        cleanErrors();

        dialogStage.close();
    }

    private void loadComboboxAluno(){
        listAlunos = alunoDAO.list();
        observableListAlunos = FXCollections.observableArrayList(listAlunos);
        
        comboBoxAluno.setItems(observableListAlunos);
    }

    private void loadComboboxFuncionario(){
        listFuncionarios = funcionarioDAO.list();
        observableListFuncionarios = FXCollections.observableArrayList(listFuncionarios);
        
        comboBoxFuncionario.setItems(observableListFuncionarios);
    }

    private void loadComboboxExercicio(){
        listExercicios = exercicioDAO.list();
        observableListExercicios = FXCollections.observableArrayList(listExercicios);
        
        comboBoxExercicio.setItems(observableListExercicios);
    }

    private void loadTableViewExercicio(){
        tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tableColumnMusculo.setCellValueFactory(new PropertyValueFactory<>("musculo"));
        tableColumnSerie.setCellValueFactory(new PropertyValueFactory<>("numSeries"));
        tableColumnRepeticoes.setCellValueFactory(new PropertyValueFactory<>("numRepeticoes"));
        
        observableListExerciciosDoTreino = FXCollections.observableArrayList(listExerciciosDoTreino);
        
        tableViewExercicio.setItems(observableListExerciciosDoTreino);
    }
    
    public Stage getDialogStage(){
        return dialogStage;
    }

    public void setDialogStage(Stage dialoStage){
        this.dialogStage = dialoStage;
    }

    public void setTreino(Treino treino){
        this.treino = treino;
        this.datePickerDataInicio.setValue(treino.getDataInicio());
        this.datePickerDataFinal.setValue(treino.getDataFinal());

        alunoDAO.setConnection(connection);
        Aluno aluno = alunoDAO.findById(treino.getIdAluno());
        this.comboBoxAluno.setValue(aluno);

        funcionarioDAO.setConnection(connection);
        Funcionario funcionario = funcionarioDAO.findById(treino.getIdFuncionario());
        this.comboBoxFuncionario.setValue(funcionario);

        treinoExercicioDAO.setConnection(connection);
        listExerciciosDoTreino = treinoExercicioDAO.findByIdTreino(treino.getIdTreino());
        loadTableViewExercicio();
    }
   
    public boolean isButtonConfirmar(){
        return true;
    }

    private boolean validate() {
        LocalDate dataInicio = datePickerDataInicio.getValue();
        LocalDate dataFinal =  datePickerDataFinal.getValue();
        Aluno aluno = (Aluno) comboBoxAluno.getSelectionModel().getSelectedItem();
        Funcionario funcionario = (Funcionario) comboBoxFuncionario.getSelectionModel().getSelectedItem();

        int error = 0;

        if(dataInicio == null){
            labelErroDataInicio.setText("Data de inicio não pode ser nula!");
            error++;
        }
        if(dataFinal == null){
            labelErroDataFinal.setText("Data final não pode ser nula!");
            error++;
        }
        if (dataInicio != null && dataFinal != null){
            if (dataFinal.isBefore(dataInicio)){
                labelErroDataFinal.setText("Data inválida!\nData de final deve ser depois da data de inicial!");
                error++;
            }
        }
        if(aluno == null){
            labelErroAluno.setText("Selecione algum aluno!");
            error++;
        }

        if(funcionario == null){
            labelErroFuncionario.setText("Selecione algum funcionário!");
            error++;
        }

        if(error > 0){
            return false;
        }else {
            return true;
        }
        
    }

    public void cleanErrors() {
        labelErroDataInicio.setText(null);
        labelErroDataFinal.setText(null);
        labelErroAluno.setText(null);
        labelErroFuncionario.setText(null);
    }
}